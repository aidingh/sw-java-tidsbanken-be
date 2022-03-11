package com.example.timebankapiproject.controller;

import com.example.timebankapiproject.models.UserModel;
import com.example.timebankapiproject.models.VacationRequestModel;
import com.example.timebankapiproject.service.UserService;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("api/v1")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin
    @GetMapping("/user/all")
    public ResponseEntity<List<UserModel>> getUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity <UserModel> getUserById (@PathVariable("user_id") Integer userId){
        return userService.getUserById(userId);
    }

    public String getManagementApiToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject requestBody = new JSONObject();
        requestBody.put("client_id", "gcpFQMIuEMTPdur0XhbRekUKWLSsLF3K");
        requestBody.put("client_secret", "RTEExKEXK603fBA11Y4s22IsaBV95PE3YvvK3A6fVwa-_ms16Gp9JHvmjLQiq0dN");
        requestBody.put("audience", "https://dev-377qri7m.eu.auth0.com/api/v2/");
        requestBody.put("grant_type", "client_credentials");

        HttpEntity<String> request = new HttpEntity<String>(requestBody.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        HashMap<String, String> result = restTemplate
                .postForObject("https://dev-377qri7m.eu.auth0.com/oauth/token", request, HashMap.class);

        return result.get("access_token");
    }

    @PostMapping("/user")
    public ResponseEntity <String> createUser() {
        JSONObject request = new JSONObject();
        request.put("email", "norman.lewis@email.com");
        request.put("given_name", "Norman");
        request.put("family_name", "Lewis");
        request.put("connection", "Username-Password-Authentication");
        request.put("password", "hej/23vad&och%");


        //userService.createUser(userModel);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        System.out.println(getManagementApiToken());
        headers.set("authorization", "Bearer " + getManagementApiToken());

        HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate
                .postForEntity("https://dev-377qri7m.eu.auth0.com/api/v2/users", entity, String.class);

        return result;
    }
    @PatchMapping("/user/")
    public ResponseEntity <UserModel> updateUser(@RequestBody UserModel userModel) {
        return userService.updateUser(userModel);
    }

    @DeleteMapping("/user/{user_id}")
    public ResponseEntity <Integer> deleteUser(@PathVariable("user_id") Integer userId){
        return userService.deleteUser(userId);
    }

    @GetMapping("/user/{user_id}/requests")
    public ResponseEntity <List<VacationRequestModel>> getUserVacationRequest(@PathVariable("user_id") Integer userId){
        return userService.getUserVacationRequestsById(userId);
    }
}
