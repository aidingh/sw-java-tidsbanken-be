package com.example.timebankapiproject.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class VacationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int id;
    @Column
    VacationRequestStatus status;
    @Column
    Date startPeriod;
    @Column
    Date endPeriod;
}

