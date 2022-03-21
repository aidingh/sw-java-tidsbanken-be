package com.example.timebankapiproject.DTOs;

import com.example.timebankapiproject.enums.VacationRequestStatus;
import com.example.timebankapiproject.models.UserModel;
import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Data;


import java.time.LocalDate;


@Data
public class VacationRequestDTO {
    private String title;

    private VacationRequestStatus status;

    private LocalDate startPeriod;

    private LocalDate endPeriod;

    private UserModel userModel;

    @JsonGetter("userModel")
    public String convertUserModelToStringURI(){
        if(userModel != null) {
            return "/api/v1/user/" + userModel.getId();
        }else{
            return null;
        }
    }

}
