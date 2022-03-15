package com.example.timebankapiproject.controller;

import com.example.timebankapiproject.models.UserModel;
import com.example.timebankapiproject.models.VacationRequestModel;
import com.example.timebankapiproject.service.UserService;
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
    public ResponseEntity <UserModel> getUserById (@PathVariable("user_id") String userId){
        return userService.getUserById(userId);
    }

    @PostMapping("/createUser")
    public ResponseEntity <String> createUser(@RequestBody UserModel userModel) {
        ResponseEntity<String> createdUser = userService.createUserInAuth0(userModel);
        String id = userService.getUserIdFromAuth0(createdUser);

        userModel.setId(id);
        userService.createUserInDatabase(userModel);

        if(userModel.isAdmin())
            userService.giveRoleToAuth0User(id,"rol_Osy55j9CI34DLcQF");

        return createdUser;
    }

    @PatchMapping("/user/")
    public ResponseEntity <UserModel> updateUser(@RequestBody UserModel userModel) {
        return userService.updateUser(userModel);
    }

    @DeleteMapping("/user/{user_id}")
    public ResponseEntity <String> deleteUser(@PathVariable("user_id") String userId){
        return userService.deleteUser(userId);
    }

    @CrossOrigin
    @GetMapping("/user/{user_id}/requests")
    public ResponseEntity <List<VacationRequestModel>> getUserVacationRequest(@PathVariable("user_id") String userId){
        return userService.getUserVacationRequestsById(userId);
    }

    @CrossOrigin
    @GetMapping("/user/role/{user_id}")
    public ResponseEntity<String> getUserRoleById(@PathVariable("user_id") String userId){
        String userRole = userService.getUserRole(userId);
        return ResponseEntity.ok().body(userRole);
    }
}
