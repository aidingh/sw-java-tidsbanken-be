package com.example.timebankapiproject.service;

import com.example.timebankapiproject.enums.VacationRequestStatus;
import com.example.timebankapiproject.models.UserModel;
import com.example.timebankapiproject.models.VacationRequestModel;
import com.example.timebankapiproject.repository.UserRepository;
import com.example.timebankapiproject.repository.VacationRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public ResponseEntity<VacationRequestModel> getVacationById(int vacationRequestId) {
        VacationRequestModel vacationRequestModel;

        if (vacationRequestRepository.existsById(vacationRequestId)) {
            vacationRequestModel = vacationRequestRepository.findById(vacationRequestId).orElse(null);
            return new ResponseEntity<>(vacationRequestModel, HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    public List<VacationRequestModel> getAllVacations () {
        return vacationRequestRepository.findAll();
    }


    public VacationRequestModel createVacationRequest(VacationRequestModel vacationRequestModel, String id){
        if(userRepository.existsById(id)){
            UserModel user = userRepository.findById(id).orElse(null);
            vacationRequestModel.setUserModel(userRepository.findById(id).orElse(null));
            vacationRequestModel.setStatus(VacationRequestStatus.PENDING);
            VacationRequestModel vacationRequest =  vacationRequestRepository.save(vacationRequestModel);

            if (user != null) {
                user.setVacationRequestModel(vacationRequestModel);
                userRepository.save(user);
            }

            return vacationRequest;
        }

        return null;
    }

    //TODO Mer DTOS borde fixas

    public ResponseEntity <List<VacationRequestModel>> getAllApprovedVacations() {
        List<VacationRequestModel> vacations = vacationRequestRepository.findAll();

        List<VacationRequestModel> approvedVacations = vacations.stream().filter(element ->
                element.getStatus() == VacationRequestStatus.APPROVED
        ).collect(Collectors.toList());

        if (!approvedVacations.isEmpty()) {
            return new ResponseEntity<>(approvedVacations, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    public List<VacationRequestModel> getVacationRequestsByUserId(String id) {
        Optional<UserModel> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            return userOptional.get().getVacationRequestModels();
        }

        return null;
    }

    public ResponseEntity<?> deleteVacationRequest(int vacationRequestId) {
        if(vacationRequestRepository.existsById(vacationRequestId)){
            vacationRequestRepository.deleteById(vacationRequestId);
            return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
