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

    public void createUserInDatabase(UserModel userModel){
        if(userModel != null){
           userRepository.save(userModel);
        }
    }
    public UserModel findUserById(String id){
        Optional<UserModel> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }
        else{
            return null;
        }
    }

    public void saveUser(UserModel user){
        userRepository.save(user);
    }

    public boolean deleteUser(String userId){
        if(userRepository.existsById(userId)){
           userRepository.deleteById(userId);
           return true;
       } else
           return false;
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

}
