package com.example.timebankapiproject.controller;

import com.example.timebankapiproject.models.Auth0User;
import com.example.timebankapiproject.models.UserModel;
import com.example.timebankapiproject.models.VacationRequestModel;
import com.example.timebankapiproject.service.Auth0Service;
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
    private final Auth0Service auth0Service;

    @Autowired
    public UserController(UserService userService, Auth0Service auth0Service) {
        this.userService = userService;
        this.auth0Service = auth0Service;
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

    @CrossOrigin
    @PostMapping("/createUser")
    public ResponseEntity <String> createUser(@RequestBody UserModel userModel) throws Exception{
        ResponseEntity<String> createdUser = auth0Service.createUserInAuth0(userModel);
        if(createdUser.getStatusCode() == HttpStatus.CONFLICT){
            return createdUser;
        }
        String id = auth0Service.getUserIdFromAuth0(createdUser);

        userModel.setId(id);
        userService.createUserInDatabase(userModel);
        if(userModel.isAdmin())
            auth0Service.giveRoleToAuth0User(id,"rol_Osy55j9CI34DLcQF");
        else{
            auth0Service.giveRoleToAuth0User(id,"rol_XuhdanLYToSuvKig");
        }
        return createdUser;
    }

    @CrossOrigin
    @PatchMapping("/updateUser")
    public ResponseEntity <String> updateUser(@RequestBody Auth0User userAuth0) {
        UserModel user = userService.findUserById(userAuth0.getId());
        user.setEmail(userAuth0.getEmail());
        userService.saveUser(user);

        ResponseEntity<String> updatedUser = auth0Service.updateUserInAuth0(userAuth0);

        return updatedUser;
    }

    @DeleteMapping("/user/{user_id}")
    public ResponseEntity <String> deleteUser(@PathVariable("user_id") String userId){
        ResponseEntity<String> deletedUser = null;

        if(userService.deleteUser(userId))
            deletedUser = auth0Service.deleteUserInAuth0(userId);

        return deletedUser;
    }

    @CrossOrigin
    @GetMapping("/user/{user_id}/requests")
    public ResponseEntity <List<VacationRequestModel>> getUserVacationRequest(@PathVariable("user_id") String userId){
        return userService.getUserVacationRequestsById(userId);
    }

    @CrossOrigin
    @GetMapping("/user/role/{user_id}")
    public ResponseEntity<String> getUserRoleById(@PathVariable("user_id") String userId){
        String userRole = auth0Service.getUserRole(userId);
        return ResponseEntity.ok().body(userRole);
    }

    @CrossOrigin
    @GetMapping("/user/changePassword/{email}")
    public boolean changeUserPassword(@PathVariable("email") String email){

        try {
            auth0Service.changeUserPassword(email);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
