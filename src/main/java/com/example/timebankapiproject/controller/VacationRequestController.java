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

/**
 * API that handles all functionality for vacation requests.
 */
@RestController
@RequestMapping("api/v1/vacation")

public class VacationRequestController {

    private final ModelMapper modelMapper;

    private final VacationRequestService vacationRequestService;

    public VacationRequestController(VacationRequestService vacationRequestService, ModelMapper modelMapper) {
        this.vacationRequestService = vacationRequestService;
        this.modelMapper = modelMapper;
    }

    /**
     * Fetches all the vacation requests.
     * @return HTTP response status code 200 with a list of all the vacation requests.
     */
    @Operation(summary = "Gets all vacation requests")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    public List<VacationRequestDTO> getVacations() {
        return vacationRequestService.getAllVacations().stream().map(vacationRequest -> modelMapper.map(vacationRequest, VacationRequestDTO.class)).collect(Collectors.toList());
    }

    /**
     * Fetches a vacation request by id.
     * @param vacationRequestId vacation request id.
     * @return HTTP response status code 200 with a vacation request.
     */
    @Operation(summary = "Get a vacation requests")
    @CrossOrigin
    @GetMapping("/id/{vacation_id}")
    public ResponseEntity<VacationRequestDTO> getVacationRequestById(@PathVariable("vacation_id") int vacationRequestId) {
        VacationRequestModel vacationRequestModel =  vacationRequestService.getVacationById(vacationRequestId);
        VacationRequestDTO vacationRequestDTO  = modelMapper.map(vacationRequestModel, VacationRequestDTO.class);
        return new ResponseEntity<>(vacationRequestDTO, HttpStatus.OK);
    }

    /**
     * Creates a new vacation request.
     * @param vacationRequestPostDTO vacationRequest DTO.
     * @param user_id user id.
     * @return status code 201 with the vacation request or null.
     */
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

    /**
     * Fetches all the approved vacation request.
     * @return  status code 200 with the approved vacation request.
     */
    @Operation(summary = "Gets all approved vacation requests")
    @CrossOrigin
    @GetMapping("/approved")
    @ResponseStatus(HttpStatus.OK)
    public List<VacationRequestDTO> getApprovedVacationRequests() {
        return vacationRequestService.getAllApprovedVacations().stream().map(vacationRequest -> modelMapper.map(vacationRequest, VacationRequestDTO.class)).collect(Collectors.toList());
    }

    /**
     * Returns a list of vacation requests objects owned by the corresponding user.
     * @param id user id.
     * @return status code 200 with a list of vacation request.
     */
    @Operation(summary = "Get all vacation requests a specific user has")
    @CrossOrigin
    @GetMapping("/{user_id}")
    public ResponseEntity<List<VacationRequestDTO>> getUserVacationRequests(@PathVariable("user_id") String id) {
        List<VacationRequestDTO> userVacations = vacationRequestService.getVacationRequestsByUserId(id).stream().map(vacationRequest -> modelMapper.map(vacationRequest, VacationRequestDTO.class)).collect(Collectors.toList());
        return ResponseEntity.ok().body(userVacations);
    }

    /**
     * Deletes (cascading) a vacation request.
     * @param vacationRequestId vacation request id.
     */
    @Operation(summary = "Delete a vacation request")
    @CrossOrigin
    @DeleteMapping("/{vacation_id}")
    public void  deleteAVacationRequest(@PathVariable("vacation_id") int vacationRequestId) {
        vacationRequestService.deleteVacationRequest(vacationRequestId);
    }

    /**
     * Partial updates the status of the vacation request of the corresponding request_id.
     * @param vacationRequestModel vacation request object.
     * @param request_id vacation request id.
     * @return HTTP response status code.
     */
    @CrossOrigin
    @PatchMapping("/{request_id}")
    public ResponseEntity <VacationRequestModel> updateVacationRequest(@RequestBody VacationRequestModel vacationRequestModel,
                                                                       @PathVariable("request_id") Integer request_id) {
        return vacationRequestService.updateVacationRequest(vacationRequestModel,request_id);
    }
}
