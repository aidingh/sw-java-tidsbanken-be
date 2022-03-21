package com.example.timebankapiproject.models;

import com.example.timebankapiproject.enums.VacationRequestStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import org.apache.catalina.User;
import org.springframework.lang.Nullable;


import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "vacationRequests")
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

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private UserModel userModel;

}

