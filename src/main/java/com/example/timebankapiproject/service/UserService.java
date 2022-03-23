package com.example.timebankapiproject.service;

import com.example.timebankapiproject.models.UserModel;
import com.example.timebankapiproject.models.VacationRequestModel;
import com.example.timebankapiproject.repository.UserRepository;
import com.example.timebankapiproject.repository.VacationRequestRepository;
import org.springframework.stereotype.Service;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final VacationRequestRepository vacationRequestRepository;

    public UserService(UserRepository userRepository, VacationRequestRepository vacationRequestRepository) {
        this.userRepository = userRepository;
        this.vacationRequestRepository = vacationRequestRepository;
    }

    /**
     * Finds all users.
     * @return Response entity with all users
     */
    public ResponseEntity <List<UserModel>> getAllUsers (){
        List<UserModel> users = userRepository.findAll();
        if(!users.isEmpty())
            return new ResponseEntity<>(users, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Finds a specific user by id.
     * @param userId
     * @return Response entity with found user or Response entity with status code Not found.
     */
    public ResponseEntity <UserModel> getUserById(String userId){

        UserModel user;

        if(userRepository.existsById(userId)){
            user = userRepository.findById(userId).get();
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Creates a user in the database.
     * @param userModel User to be created.
     */
    public void createUserInDatabase(UserModel userModel){
        if(userModel != null){
           userRepository.save(userModel);
        }
    }

    /**
     * Finds a user by id and returns the user.
     * @param id
     * @return Returns found user.
     */
    public UserModel findUserById(String id){
        Optional<UserModel> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }
        else{
            return null;
        }
    }

    /**
     * Saves a user to the database.
     * @param user
     */
    public void saveUser(UserModel user){
        userRepository.save(user);
    }

    /**
     * Deletes a user from the database by id.
     * @param userId
     * @return
     */
    public boolean deleteUser(String userId){
        if(userRepository.existsById(userId)){
           userRepository.deleteById(userId);
           return true;
       } else
           return false;
    }

    /**
     * Get all vacationRequests of a user.
     * @param userId Id of the user.
     * @return ResponseEntity with all vacationrequests in the body.
     */
    public ResponseEntity<List<VacationRequestModel>> getUserVacationRequestsById(String userId){

        List <VacationRequestModel> allVacRequests;

        if(userRepository.existsById(userId)){
            UserModel userModel = userRepository.findById(userId).get();
            allVacRequests = userModel.getVacationRequestModels();
            return new ResponseEntity<>(allVacRequests, HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
