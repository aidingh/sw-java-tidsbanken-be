package com.example.timebankapiproject.controller;

import com.example.timebankapiproject.models.UserModel;
import com.example.timebankapiproject.models.VacationRequest;
import com.example.timebankapiproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/user")
    public ResponseEntity <UserModel> createUser(@RequestBody UserModel userModel) {
        return userService.createUser(userModel);
    }
    @PostMapping("/update/user")
    public ResponseEntity <UserModel> updateUser(@RequestBody UserModel userModel) {
        return userService.updateUser(userModel);
    }

    @DeleteMapping("/delete/user/{user_id}")
    public ResponseEntity <Integer> deleteUser(@PathVariable("user_id") Integer userId){
        return userService.deleteUser(userId);
    }

    @GetMapping("/user/{user_id}/requests")
    public ResponseEntity <List<VacationRequest>> getUserVacationRequest(@PathVariable("user_id") Integer userId){
        return userService.getUserVacationRequestsById(userId);
    }
}
