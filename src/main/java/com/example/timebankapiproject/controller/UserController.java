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

    @PatchMapping("/updateUserAuth0")
    public ResponseEntity <String> updateUserAuth0(@RequestBody Auth0User userModel) {
        ResponseEntity<String> updatedUser = auth0Service.updateUserInAuth0(userModel);

        return updatedUser;
    }

    /*@PatchMapping("/updateUser")
    public ResponseEntity <String> updateUser(@RequestBody UserModel userModel) {
        ResponseEntity<String> updatedUser = null;

            if(userService.updateUser(userModel))
                updatedUser = auth0Service.updateUserInAuth0(userModel);

        return updatedUser;
    }*/

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
    @GetMapping("user/changePassword/{mail}")
    public ResponseEntity<String> getPasswordUrl(@PathVariable("mail") String mail){
        ResponseEntity<String> hej = null;
        try {
            hej = auth0Service.change(mail);
            return hej;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hej;
    }
}
