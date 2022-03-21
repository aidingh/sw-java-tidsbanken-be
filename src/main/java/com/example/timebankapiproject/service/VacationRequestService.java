package com.example.timebankapiproject.service;

import com.example.timebankapiproject.enums.VacationRequestStatus;
import com.example.timebankapiproject.models.UserModel;
import com.example.timebankapiproject.models.VacationRequestModel;
import com.example.timebankapiproject.repository.UserRepository;
import com.example.timebankapiproject.repository.VacationRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VacationRequestService {

    @Autowired
    private VacationRequestRepository vacationRequestRepository;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity <List<VacationRequestModel>> getAllVacations () {
        List<VacationRequestModel> vacations = vacationRequestRepository.findAll();
        if(!vacations.isEmpty()){
            return new ResponseEntity<>(vacations,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    public ResponseEntity <VacationRequestModel> createVacationRequest(VacationRequestModel vacationRequestModel,String id){
        VacationRequestModel vacationRequest = null;

        if(vacationRequestModel != null){
            Optional<UserModel> userOptional = userRepository.findById(id);
            UserModel user;

            if(userOptional.isPresent()){
                vacationRequest = vacationRequestRepository.save(vacationRequestModel);
                user = userRepository.findById(id).get();
                user.setVacationRequestModel(vacationRequest);
                userRepository.save(user);
            }

            return new ResponseEntity<>(vacationRequest, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity <VacationRequestModel> createVacationRequestTest(VacationRequestModel vacationRequestModel,
                                                                           String id){

        if(userRepository.existsById(id)){
            vacationRequestRepository.save(vacationRequestModel);
        }

    }

    public ResponseEntity <List<VacationRequestModel>> getAllApprovedVacations() {
        List<VacationRequestModel> vacations = vacationRequestRepository.findAll();

        List<VacationRequestModel> approvedVacations = vacations.stream().filter(element ->
                element.getStatus() == VacationRequestStatus.APPROVED
        ).collect(Collectors.toList());

        if(!approvedVacations.isEmpty()){
            return new ResponseEntity<>(approvedVacations,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    public List<VacationRequestModel> getVacationRequestsByUserId(String id){
        Optional<UserModel> userOptional = userRepository.findById(id);

        if(userOptional.isPresent())
        {
            return userOptional.get().getVacationRequestModels();
        }

        return null;
    }
}
