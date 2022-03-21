package com.example.timebankapiproject.controller;

import com.example.timebankapiproject.models.VacationRequestModel;
import com.example.timebankapiproject.service.VacationRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/vacation")
public class VacationRequestController {
    private final VacationRequestService vacationRequestService;

    public VacationRequestController(VacationRequestService vacationRequestService) {
        this.vacationRequestService = vacationRequestService;
    }

    @CrossOrigin
    @GetMapping("/all")
    public ResponseEntity <List<VacationRequestModel>> getVacations(){
        return vacationRequestService.getAllVacations();
    }

    @CrossOrigin
    @PostMapping("/{user_id}/create")
    public ResponseEntity <VacationRequestModel> createVacationRequest(
            @RequestBody VacationRequestModel vacationRequestModel,
            @PathVariable String user_id){
        return vacationRequestService.createVacationRequestTest(vacationRequestModel,user_id);
    }

    @CrossOrigin
    @GetMapping("/approved")
    public ResponseEntity <List<VacationRequestModel>> getApprovedVacationRequests(){
        return vacationRequestService.getAllApprovedVacations();
    }

    @CrossOrigin
    @GetMapping("/{user_id}")
    public ResponseEntity<List<VacationRequestModel>> getUserVacationRequests(@PathVariable("user_id")String id){
        List<VacationRequestModel> userVacations = vacationRequestService.getVacationRequestsByUserId(id);

        return ResponseEntity.ok().body(userVacations);
    }
    /*
    @DeleteMapping("/{vacationRequest_id}/delete")
    public ResponseEntity<VacationRequestModel> deleteVacationRequest(@PathVariable("vacationRequest_id") Integer id){
        return  vacationRequestService.deleteVacationRequestById(id);
    } */
}
