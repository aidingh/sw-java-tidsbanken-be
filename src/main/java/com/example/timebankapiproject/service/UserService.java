package com.example.timebankapiproject.service;

import com.example.timebankapiproject.models.UserModel;
import com.example.timebankapiproject.models.VacationRequestModel;
import com.example.timebankapiproject.repository.UserRepository;
import com.example.timebankapiproject.repository.VacationRequestRepository;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class UserService {



    private final String clientId = "gcpFQMIuEMTPdur0XhbRekUKWLSsLF3K";
    private final String clientSecret = "RTEExKEXK603fBA11Y4s22IsaBV95PE3YvvK3A6fVwa-_ms16Gp9JHvmjLQiq0dN";
    private final String managementApiAudience = "https://dev-377qri7m.eu.auth0.com/api/v2/";
    private final String roleIdAdmin = "rol_Osy55j9CI34DLcQF";


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VacationRequestRepository vacationRequestRepository;

    public ResponseEntity <List<UserModel>> getAllUsers (){
        List<UserModel> users = userRepository.findAll();
        if(!users.isEmpty())
            return new ResponseEntity<>(users, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    public ResponseEntity <UserModel> getUserById(String userId){

        UserModel user;

        if(userRepository.existsById(userId)){
            user = userRepository.findById(userId).get();
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public UserModel createUserInDatabase(UserModel userModel){
        UserModel user = new UserModel();

        if(userModel != null){
            user = userRepository.save(userModel);
            return user;
        } else
            return user;
    }

    public ResponseEntity<String> createUserInAuth0(UserModel user){
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

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate
                .postForEntity("https://dev-377qri7m.eu.auth0.com/api/v2/users", entity, String.class);
        return result;
    }

    //TODO: Update user in auth0 as well
    public ResponseEntity<UserModel> updateUser(UserModel userModel){
        Optional<UserModel> userData = userRepository.findById(userModel.getId());

        if(userData.isPresent()){
            UserModel user = userData.get();
            user.setFirstName(userModel.getFirstName());
            user.setEmail(userModel.getEmail());
            return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //TODO Delete User in auth0 as well
    public ResponseEntity<String> deleteUser(String userId){
       if(userRepository.existsById(userId)){
           userRepository.deleteById(userId);
           return new ResponseEntity<String>(userId,HttpStatus.OK);
       } else
           return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<VacationRequestModel>> getUserVacationRequestsById(String userId){

        List <VacationRequestModel> allVacRequests;

        if(userRepository.existsById(userId)){
            UserModel userModel = userRepository.findById(userId).get();
            allVacRequests = userModel.getVacationRequestModels();
            return new ResponseEntity<>(allVacRequests, HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
}
