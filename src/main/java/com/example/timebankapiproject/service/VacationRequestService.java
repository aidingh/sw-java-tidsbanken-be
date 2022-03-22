package com.example.timebankapiproject.service;

import com.example.timebankapiproject.enums.VacationRequestStatus;
import com.example.timebankapiproject.models.UserModel;
import com.example.timebankapiproject.models.VacationRequestModel;
import com.example.timebankapiproject.repository.UserRepository;
import com.example.timebankapiproject.repository.VacationRequestRepository;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VacationRequestService {

    private final VacationRequestRepository vacationRequestRepository;

    private final UserRepository userRepository;

    public VacationRequestService(VacationRequestRepository vacationRequestRepository, UserRepository userRepository) {
        this.vacationRequestRepository = vacationRequestRepository;
        this.userRepository = userRepository;
    }

    public VacationRequestModel getVacationById(int vacationRequestId) {
        VacationRequestModel vacationRequestModel;
        if (vacationRequestRepository.existsById(vacationRequestId)) {
            vacationRequestModel = vacationRequestRepository.findById(vacationRequestId).orElse(null);
            return vacationRequestModel;
        } else throw new ResourceNotFoundException("VacationRequestModel");
    }

    public List<VacationRequestModel> getAllVacations() {
        return vacationRequestRepository.findAll();
    }


    public VacationRequestModel createVacationRequest(VacationRequestModel vacationRequestModel, String id) {
        if (userRepository.existsById(id)) {
            UserModel user = userRepository.findById(id).orElse(null);
            vacationRequestModel.setUserModel(userRepository.findById(id).orElse(null));
            vacationRequestModel.setStatus(VacationRequestStatus.PENDING);
            VacationRequestModel vacationRequest = vacationRequestRepository.save(vacationRequestModel);

            if (user != null) {
                user.setVacationRequestModel(vacationRequestModel);
                userRepository.save(user);
            }

            return vacationRequest;
        }

        return null;
    }

    public List<VacationRequestModel> getAllApprovedVacations() {
        List<VacationRequestModel> vacations = vacationRequestRepository.findAll();

        List<VacationRequestModel> approvedVacations = vacations.stream().filter(element -> element.getStatus() == VacationRequestStatus.APPROVED).collect(Collectors.toList());

        if (!approvedVacations.isEmpty()) {
            return approvedVacations;
        } else {
            new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return null;
        }
    }

    public List<VacationRequestModel> getVacationRequestsByUserId(String id) {
        Optional<UserModel> userOptional = userRepository.findById(id);
        return userOptional.map(UserModel::getVacationRequestModels).orElse(null);
    }

    public void deleteVacationRequest(int vacationRequestId) {
        VacationRequestModel vacationRequestModel = vacationRequestRepository.findById(vacationRequestId).orElseThrow(() -> new ResourceNotFoundException("VacationRequestModel"));
        vacationRequestRepository.delete(vacationRequestModel);
    }
}
