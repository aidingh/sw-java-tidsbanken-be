package com.example.timebankapiproject.controller;

import com.example.timebankapiproject.DTOs.VacationRequestDTO;
import com.example.timebankapiproject.DTOs.VacationRequestPostDTO;
import com.example.timebankapiproject.models.VacationRequestModel;
import com.example.timebankapiproject.service.VacationRequestService;
import io.swagger.v3.oas.annotations.Operation;
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

    private final ModelMapper modelMapper;

    private final VacationRequestService vacationRequestService;

    public VacationRequestController(VacationRequestService vacationRequestService, ModelMapper modelMapper) {
        this.vacationRequestService = vacationRequestService;
        this.modelMapper = modelMapper;
    }

    //GET

    @Operation(summary = "Gets all vacation requests")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    public List<VacationRequestDTO> getVacations() {
        return vacationRequestService.getAllVacations().stream().map(vacationRequest -> modelMapper.map(vacationRequest, VacationRequestDTO.class)).collect(Collectors.toList());
    }

    @Operation(summary = "Get a vacation requests")
    @CrossOrigin
    @GetMapping("/id/{vacation_id}")
    public ResponseEntity<VacationRequestDTO> getVacationRequestById(@PathVariable("vacation_id") int vacationRequestId) {
        VacationRequestModel vacationRequestModel =  vacationRequestService.getVacationById(vacationRequestId);
        VacationRequestDTO vacationRequestDTO  = modelMapper.map(vacationRequestModel, VacationRequestDTO.class);
        return new ResponseEntity<>(vacationRequestDTO, HttpStatus.OK);
    }

    @Operation(summary = "Gets all approved vacation requests")
    @CrossOrigin
    @GetMapping("/approved")
    @ResponseStatus(HttpStatus.OK)
    public List<VacationRequestDTO> getApprovedVacationRequests() {
        return vacationRequestService.getAllApprovedVacations().stream().map(vacationRequest -> modelMapper.map(vacationRequest, VacationRequestDTO.class)).collect(Collectors.toList());
    }

    @Operation(summary = "Get all vacation requests a specific user has")
    @CrossOrigin
    @GetMapping("/{user_id}")
    public ResponseEntity<List<VacationRequestDTO>> getUserVacationRequests(@PathVariable("user_id") String id) {
        List<VacationRequestDTO> userVacations = vacationRequestService.getVacationRequestsByUserId(id).stream().map(vacationRequest -> modelMapper.map(vacationRequest, VacationRequestDTO.class)).collect(Collectors.toList());

        return ResponseEntity.ok().body(userVacations);
    }
    //POST
    @Operation(summary = "Create a vacation request")
    @CrossOrigin
    @PostMapping("/{user_id}/create")
    public ResponseEntity<VacationRequestDTO> createVacationRequest(
            @RequestBody VacationRequestPostDTO vacationRequestPostDTO,
            @PathVariable String user_id) {

        VacationRequestModel vacationRequestModel = modelMapper.map(vacationRequestPostDTO, VacationRequestModel.class);
        VacationRequestModel vacation = vacationRequestService.createVacationRequest(vacationRequestModel, user_id);
        VacationRequestDTO vacationRequestDTOResponse = modelMapper.map(vacation, VacationRequestDTO.class);
        return new ResponseEntity<>(vacationRequestDTOResponse, HttpStatus.CREATED);
    }

    //DELETE
    @Operation(summary = "Delete a vacation request")
    @CrossOrigin
    @DeleteMapping("/{vacation_id}")
    public void  deleteAVacationRequest(@PathVariable("vacation_id") int vacationRequestId) {
        vacationRequestService.deleteVacationRequest(vacationRequestId);
    }

}
