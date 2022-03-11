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
    private String id;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String email;
    @OneToMany
    private List<VacationRequestModel> vacationRequestModels;
    @Column
    private boolean isAdmin;

}
