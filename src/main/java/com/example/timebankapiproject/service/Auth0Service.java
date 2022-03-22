package com.example.timebankapiproject.service;

import com.example.timebankapiproject.models.UserModel;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;

@Service
public class Auth0Service {


    private final String clientId = "gcpFQMIuEMTPdur0XhbRekUKWLSsLF3K";
    private final String clientSecret = "RTEExKEXK603fBA11Y4s22IsaBV95PE3YvvK3A6fVwa-_ms16Gp9JHvmjLQiq0dN";
    private final String managementApiAudience = "https://dev-377qri7m.eu.auth0.com/api/v2/";
    private final String roleIdAdmin = "rol_Osy55j9CI34DLcQF";

    public void changeUserPassword(String email){
        JSONObject request = new JSONObject();
        request.put("client_id", this.clientId);
        request.put("email", email);
        request.put("connection", "Username-Password-Authentication");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.postForEntity("https://dev-377qri7m.eu.auth0.com/dbconnections/change_password", entity, String.class);
    }


    public ResponseEntity<String> createUserInAuth0(UserModel user) throws Exception{
        JSONObject request = new JSONObject();
        request.put("email", user.getEmail());
        request.put("given_name", user.getFirstName());
        request.put("family_name", user.getLastName());
        request.put("connection", "Username-Password-Authentication");
        // TODO ask team how we should do with password creation and getting it to the created user.
        request.put("password", "hej/23vad&och%");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", "Bearer " + getManagementApiToken());

        HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);

        ResponseEntity<String> result = null;

        try{
            RestTemplate restTemplate = new RestTemplate();
            result = restTemplate.postForEntity("https://dev-377qri7m.eu.auth0.com/api/v2/users", entity, String.class);
            return result;
        }
        catch(HttpStatusCodeException e){
            if(e.getStatusCode() == HttpStatus.CONFLICT)
            {
                return new ResponseEntity<>("User already exists.",HttpStatus.CONFLICT);
            }
        }
        return result;
    }
    // TODO what should we update in AUTH0? we are just updating email/name atm.
    public ResponseEntity<String> updateUserInAuth0(UserModel user){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", "Bearer " + getManagementApiToken());

        JSONObject request = new JSONObject();
        request.put("email", user.getEmail());
        request.put("name", user.getEmail());
        request.put("connection", "Username-Password-Authentication");

        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);

        String url = "https://dev-377qri7m.eu.auth0.com/api/v2/users/" +"auth0|"+ user.getId();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        ResponseEntity<String> result = restTemplate
                .exchange(url,HttpMethod.PATCH, entity, String.class);

        return result;
    }

    public ResponseEntity<String> deleteUserInAuth0(String userId){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", "Bearer " + getManagementApiToken());

        JSONObject request = new JSONObject();
        request.put("connection", "Username-Password-Authentication");

        HttpEntity<String> entity = new HttpEntity<>(request.toString(),headers);

        String url = "https://dev-377qri7m.eu.auth0.com/api/v2/users/" +"auth0|"+ userId;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate
                .exchange(url, HttpMethod.DELETE, entity, String.class);

        return result;
    }


    public String getUserIdFromAuth0(ResponseEntity<String> response){
        JSONObject json = new JSONObject(response.getBody().toString());
        JSONArray jsonArray = json.getJSONArray("identities");
        JSONObject nestedJson = new JSONObject(jsonArray.getJSONObject(0));

        System.out.println(json.getJSONArray("identities").getJSONObject(0).get("user_id"));

        String id = json.getJSONArray("identities").getJSONObject(0).get("user_id").toString();

        return id;
    }

    private String getManagementApiToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject requestBody = new JSONObject();
        requestBody.put("client_id", clientId);
        requestBody.put("client_secret", clientSecret);
        requestBody.put("audience", managementApiAudience);
        requestBody.put("grant_type", "client_credentials");

        HttpEntity<String> request = new HttpEntity<String>(requestBody.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        HashMap<String, String> result = restTemplate
                .postForObject("https://dev-377qri7m.eu.auth0.com/oauth/token", request, HashMap.class);

        return result.get("access_token");
    }

    public void giveRoleToAuth0User(String id, String role) {
        HttpResponse<String> roleResponse = null;
        try {
            System.out.println("before");
            roleResponse = Unirest.post( managementApiAudience + "users/auth0|" + id + "/roles")
                    .header("content-type", "application/json")
                    .header("authorization", "Bearer " + getManagementApiToken())
                    .header("cache-control", "no-cache")
                    .body("{ \"roles\": [ \"rol_Osy55j9CI34DLcQF\"] }").asString();
            System.out.println("before");

        } catch (UnirestException e) {
            System.out.println("print stack");
            e.printStackTrace();
        }
        System.out.println("headers " + roleResponse.getHeaders());
        System.out.println("body " + roleResponse.getBody());

    }

    public String getUserRole(String id){
        HttpResponse<JsonNode> response = null;

        try {
            response = Unirest.get(managementApiAudience + "users/auth0|" + id + "/roles")
                    .header("authorization", "Bearer " + getManagementApiToken())
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return response.getBody().toString();
    }


}
