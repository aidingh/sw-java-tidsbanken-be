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

    /**
     * Fetches all the vacation requests from the database.
     * @return all vacation requests
     */
    public List<VacationRequestModel> getAllVacations() {
        return vacationRequestRepository.findAll();
    }

    /**
     * Returns a vacation request by id.
     * @param vacationRequestId vacation request id.
     * @return a vacation request on success or ResourceNotFoundException.
     */
    public VacationRequestModel getVacationById(int vacationRequestId) {
        VacationRequestModel vacationRequestModel;
        if (vacationRequestRepository.existsById(vacationRequestId)) {
            vacationRequestModel = vacationRequestRepository.findById(vacationRequestId).orElse(null);
            return vacationRequestModel;
        } else throw new ResourceNotFoundException("VacationRequestModel");
    }

    /**
     * Creates a new vacation request only if the user exists in the database
     * @param vacationRequestModel vacation request
     * @param id user id
     * @return the created vacation request on success or null.
     */
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

    /**
     * Fetches all the approved vacation requests.
     * @return a list of all approved vacation requests on success or status code 204 with null.
     */
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

    /**
     * Returns a list of vacation requests of the corresponding user.
     * @param id user id
     * @return the corresponding vacation request by user id or null.
     */
    public List<VacationRequestModel> getVacationRequestsByUserId(String id) {
        Optional<UserModel> userOptional = userRepository.findById(id);
        return userOptional.map(UserModel::getVacationRequestModels).orElse(null);
    }

    /**
     * Deletes (cascading) a vacation request or throws ResourceNotFoundException
     * @param vacationRequestId vacation request id.
     */
    public void deleteVacationRequest(int vacationRequestId) {
        VacationRequestModel vacationRequestModel = vacationRequestRepository.findById(vacationRequestId).orElseThrow(() -> new ResourceNotFoundException("VacationRequestModel"));
        vacationRequestRepository.delete(vacationRequestModel);
    }

    /**
     * If the request_id exists update the status and save it in the database.
     * @param vacationRequestModel vacation request object.
     * @param id request id.
     * @return status code 200 in success with the vacation request object or 500 in fail.
     */
    public ResponseEntity<VacationRequestModel> updateVacationRequest(VacationRequestModel vacationRequestModel,
                                                                      Integer id){
        Optional<VacationRequestModel> vacationRequestData = vacationRequestRepository.findById(id);
        if(vacationRequestData.isPresent()){
            VacationRequestModel req = vacationRequestData.get();
            req.setStatus(vacationRequestModel.getStatus());
            VacationRequestModel result = vacationRequestRepository.save(req);
            return new ResponseEntity<>(result,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
