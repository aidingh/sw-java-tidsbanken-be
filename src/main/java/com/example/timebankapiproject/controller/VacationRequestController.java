package com.example.timebankapiproject.controller;

import com.example.timebankapiproject.models.VacationRequestModel;
import com.example.timebankapiproject.service.VacationRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VacationRequestController {
    private final VacationRequestService vacationRequestService;

    public VacationRequestController(VacationRequestService vacationRequestService) {
        this.vacationRequestService = vacationRequestService;
    }

    @GetMapping("/vacation")
    public ResponseEntity <List<VacationRequestModel>> getVacations(){
        return vacationRequestService.getAllVacations();
    }

    @PostMapping("/vacation")
    public ResponseEntity <VacationRequestModel> createVacationRequest(@RequestBody VacationRequestModel vacationRequestModel){
        return vacationRequestService.createVacationRequest(vacationRequestModel);
    }
}
