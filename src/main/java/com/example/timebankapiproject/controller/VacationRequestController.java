package com.example.timebankapiproject.controller;

import com.example.timebankapiproject.DTOs.VacationRequestDTO;
import com.example.timebankapiproject.DTOs.VacationRequestPostDTO;
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

    //GET

    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    public List<VacationRequestDTO> getVacations() {
        return vacationRequestService.getAllVacations().stream().map(vacationRequest -> modelMapper.map(vacationRequest, VacationRequestDTO.class)).collect(Collectors.toList());
    }

    @CrossOrigin
    @GetMapping("/id/{vacation_id}")
    public ResponseEntity<VacationRequestDTO> getVacationRequestById(@PathVariable("vacation_id") int vacationRequestId) {
        VacationRequestModel vacationRequestModel =  vacationRequestService.getVacationById(vacationRequestId);
        VacationRequestDTO vacationRequestDTO  = modelMapper.map(vacationRequestModel, VacationRequestDTO.class);
        return new ResponseEntity<>(vacationRequestDTO, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/approved")
    @ResponseStatus(HttpStatus.OK)
    public List<VacationRequestDTO> getApprovedVacationRequests() {
        return vacationRequestService.getAllApprovedVacations().stream().map(vacationRequest -> modelMapper.map(vacationRequest, VacationRequestDTO.class)).collect(Collectors.toList());
    }

    @CrossOrigin
    @GetMapping("/{user_id}")
    public ResponseEntity<List<VacationRequestDTO>> getUserVacationRequests(@PathVariable("user_id") String id) {
        List<VacationRequestDTO> userVacations = vacationRequestService.getVacationRequestsByUserId(id).stream().map(vacationRequest -> modelMapper.map(vacationRequest, VacationRequestDTO.class)).collect(Collectors.toList());

        return ResponseEntity.ok().body(userVacations);
    }
    //POST
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
    @CrossOrigin
    @DeleteMapping("/{vacation_id}")
    public void  deleteAVacationRequest(@PathVariable("vacation_id") int vacationRequestId) {
        vacationRequestService.deleteVacationRequest(vacationRequestId);
    }

}
