package com.example.timebankapiproject.controller;

import com.example.timebankapiproject.DTOs.VacationRequestDTO;
import com.example.timebankapiproject.models.VacationRequestModel;
import com.example.timebankapiproject.service.VacationRequestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/vacation")
public class VacationRequestController {

    @Autowired
    private ModelMapper modelMapper;

    private final VacationRequestService vacationRequestService;

    public VacationRequestController(VacationRequestService vacationRequestService) {
        this.vacationRequestService = vacationRequestService;
    }

    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    public List<VacationRequestDTO> getVacations(){
        return vacationRequestService.getAllVacations().stream().map(vacationRequest -> modelMapper.map(vacationRequest, VacationRequestDTO.class)).collect(Collectors.toList());
    }

    @CrossOrigin
    @PostMapping("/{user_id}/create")
    public ResponseEntity <VacationRequestDTO> createVacationRequest(
            @RequestBody VacationRequestDTO VacationRequestDTO,
            @PathVariable String user_id){

        VacationRequestModel VacationRequest = modelMapper.map(VacationRequestDTO, VacationRequestModel.class);
        VacationRequestModel vacation = vacationRequestService.createVacationRequest(VacationRequest,user_id);
        VacationRequestDTO vacationRequestDTOResponse = modelMapper.map(vacation,VacationRequestDTO.class);
        return new ResponseEntity<>(vacationRequestDTOResponse, HttpStatus.CREATED);
    }

    //TODO Mer DTOS borde fixas
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

    @DeleteMapping("/{vacationRequest_id}/delete")
    public ResponseEntity<VacationRequestModel> deleteVacationRequest(@PathVariable("vacationRequest_id") Integer id){
        return  vacationRequestService.deleteVacationRequestById(id);
    }
}
