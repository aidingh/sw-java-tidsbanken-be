package com.example.timebankapiproject.models;

import com.example.timebankapiproject.enums.VacationRequestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class VacationRequestModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Column
    private String title;
    @Column
    private VacationRequestStatus status;
    @Column
    private LocalDate startPeriod;
    @Column
    private LocalDate endPeriod;
}

