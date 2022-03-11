package com.example.timebankapiproject.controller;

import com.example.timebankapiproject.models.UserModel;
import com.example.timebankapiproject.models.VacationRequestModel;
import com.example.timebankapiproject.service.UserService;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
    public ResponseEntity <UserModel> getUserById (@PathVariable("user_id") String userId){
        return userService.getUserById(userId);
    }

    @PostMapping("/createUser")
    public ResponseEntity <String> createUser(@RequestBody UserModel userModel) {
        ResponseEntity<String> response = userService.createUserInAuth0(userModel);
        String id = userService.getUserAuth0Id(response);
        userModel.setId(id);
        userService.createUserInDatabase(userModel);
        userService.giveRoleToAuth0User(id,"rol_Osy55j9CI34DLcQF");


        // set and get Id from auth0
        return response;
    }
    @PatchMapping("/user/")
    public ResponseEntity <UserModel> updateUser(@RequestBody UserModel userModel) {
        return userService.updateUser(userModel);
    }

    @DeleteMapping("/user/{user_id}")
    public ResponseEntity <String> deleteUser(@PathVariable("user_id") String userId){
        return userService.deleteUser(userId);
    }

    @GetMapping("/user/{user_id}/requests")
    public ResponseEntity <List<VacationRequestModel>> getUserVacationRequest(@PathVariable("user_id") String userId){
        return userService.getUserVacationRequestsById(userId);
    }
}
