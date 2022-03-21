package com.example.timebankapiproject.DTOs;

import com.example.timebankapiproject.enums.VacationRequestStatus;
import com.example.timebankapiproject.models.UserModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

import java.time.LocalDate;

@RequiredArgsConstructor
public class VacationRequestDTO {
    private String title;

    private VacationRequestStatus status;

    private LocalDate startPeriod;

    private LocalDate endPeriod;

    private UserModel userModel;
}
