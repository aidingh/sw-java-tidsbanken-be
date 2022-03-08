package com.example.timebankapiproject.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public int id;
    @Column
    public String name;
    @Column
    public String email;
    @OneToMany
    public List<VacationRequest> vacationRequests;
    @Column
    public boolean isAdmin;

}
