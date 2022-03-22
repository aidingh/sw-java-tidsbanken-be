package com.example.timebankapiproject.DTOs;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VacationRequestPostDTO {
    private String title;

    private LocalDate startPeriod;

    private LocalDate endPeriod;
}
