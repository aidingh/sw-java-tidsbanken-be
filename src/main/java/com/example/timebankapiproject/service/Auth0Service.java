package com.example.timebankapiproject.service;
import com.example.timebankapiproject.models.Auth0User;
import com.example.timebankapiproject.models.UserModel;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
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

    /**
     * Triggers an email being sent to the provided email adress with a link to change the password.
     * @param email email adress the change-password-link is being sent to.
     */
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

    /**
     * Creates a user on the Auth0 database with the Auth0 management api.
     * Sets a default password to the account that is created.
     * Frontend informs that this password should be changed by the created user immediately.
     * returns a error message if the user already exists.
     * @param user user to be created.
     * @return The created user.
     */
    public ResponseEntity<String> createUserInAuth0(UserModel user) {
        JSONObject request = new JSONObject();
        request.put("email", user.getEmail());
        request.put("given_name", user.getFirstName());
        request.put("family_name", user.getLastName());
        request.put("connection", "Username-Password-Authentication");
        request.put("password", "hej/23vad&och%");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", "Bearer " + getManagementApiToken());

        HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);

        ResponseEntity<String> result = null;

        try {
            RestTemplate restTemplate = new RestTemplate();
            result = restTemplate.postForEntity("https://dev-377qri7m.eu.auth0.com/api/v2/users", entity, String.class);
            return result;
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                return new ResponseEntity<>("User already exists.", HttpStatus.CONFLICT);
            }
        }
        return result;
    }

    /**
     * Updates a users information on Auth0 with the Auth0 management api.
     * @param user contains the new updated user information.
     * @return The updated user.
     */
    public ResponseEntity<String> updateUserInAuth0(Auth0User user){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", "Bearer " + getManagementApiToken());

        JSONObject request = new JSONObject();
        request.put("email", user.getEmail());
        request.put("nickname", user.getNickname());
        request.put("connection", "Username-Password-Authentication");

        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);

        String url = "https://dev-377qri7m.eu.auth0.com/api/v2/users/" + "auth0|" + user.getId();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        ResponseEntity<String> result = restTemplate
                .exchange(url,HttpMethod.PATCH, entity, String.class);
        return result;
    }

    /**
     * Deletes a user from Auth0.
     * @param userId id of user to be deleted
     * @return result from deletion.
     */
    public ResponseEntity<String> deleteUserInAuth0(String userId){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", "Bearer " + getManagementApiToken());

        JSONObject request = new JSONObject();
        request.put("connection", "Username-Password-Authentication");

        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);

        String url = "https://dev-377qri7m.eu.auth0.com/api/v2/users/" + "auth0|" + userId;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate
                .exchange(url, HttpMethod.DELETE, entity, String.class);

        return result;
    }

    /**
     * Extracts the users Auth0 id from a response.
     * @param response
     * @return Users Auth0 id.
     */
    public String getUserIdFromAuth0(ResponseEntity<String> response){
        JSONObject json = new JSONObject(response.getBody());
        String id = json.getJSONArray("identities").getJSONObject(0).get("user_id").toString();

        return id;
    }

    /**
     * Fetches the api token needed to access the Auth0 management api.
     * @return Returns the token.
     */
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

    /**
     *  Gives user on Auth0 a specific role via the Auth0 management api.
     * @param id The Id of the user that will be assigned a role.
     * @param role The role the user will get.
     */
    public void giveRoleToAuth0User(String id, String role) {
        HttpResponse<String> roleResponse = null;
        try {
            roleResponse = Unirest.post(managementApiAudience + "users/auth0|" + id + "/roles")
                    .header("content-type", "application/json")
                    .header("authorization", "Bearer " + getManagementApiToken())
                    .header("cache-control", "no-cache")
                    .body("{ \"roles\": [ \""+role+"\"] }").asString();

        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fethces the role if a user by its id.
     * @param id Auth0 id of the user which role is to be fetched.
     * @return
     */
    public String getUserRole(String id) {
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
