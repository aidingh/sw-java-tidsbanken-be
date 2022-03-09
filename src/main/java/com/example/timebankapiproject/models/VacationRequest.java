package com.example.timebankapiproject.models;

import com.example.timebankapiproject.enums.VacationRequestStatus;
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
    public int id;
    @Column
    public VacationRequestStatus status;
    @Column
    public Date startPeriod;
    @Column
    public Date endPeriod;
}

