package com.example.timebankapiproject.service;

import com.example.timebankapiproject.models.UserModel;
import com.example.timebankapiproject.models.VacationRequest;
import com.example.timebankapiproject.repository.UserRepository;
import com.example.timebankapiproject.repository.VacationRequestRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

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

    public ResponseEntity <UserModel> getUserById(Integer userId){

        UserModel user;

        if(userRepository.existsById(userId)){
            user = userRepository.findById(userId).get();
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<UserModel> createUser(UserModel userModel){

        UserModel user;

        if(userModel != null){
            user = userRepository.save(userModel);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } else
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<UserModel> updateUser(UserModel userModel){
        Optional<UserModel> userData = userRepository.findById(userModel.id);

        if(userData.isPresent()){
            UserModel user = userData.get();
            user.setName(userModel.getName());
            user.setEmail(userModel.getEmail());
            return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Integer> deleteUser(Integer userId){
       if(userRepository.existsById(userId)){
           userRepository.deleteById(userId);
           return new ResponseEntity<>(userId,HttpStatus.OK);
       } else
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<VacationRequest>> getUserVacationRequestsById(Integer userId){

        List <VacationRequest> allVacRequests;

        if(userRepository.existsById(userId)){
            UserModel userModel = userRepository.findById(userId).get();
            allVacRequests = userModel.getVacationRequests();
            return new ResponseEntity<>(allVacRequests, HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
