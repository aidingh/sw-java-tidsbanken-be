package com.example.timebankapiproject.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String email;

    //@OneToMany(mappedBy = "userModel", cascade = CascadeType.ALL)
    @Nullable
    @OneToMany(mappedBy = "userModel", cascade = CascadeType.ALL)
    public List<VacationRequestModel> vacationRequestModels;

    @Column
    public boolean isAdmin;

    public void setVacationRequestModel(VacationRequestModel vacationRequest){
        this.vacationRequestModels.add(vacationRequest);
    }

    public List<VacationRequestModel> getVacationRequestModels() {
        return vacationRequestModels;
    }

    public void setVacationRequestModels(List<VacationRequestModel> vacationRequestModels) {
        this.vacationRequestModels = vacationRequestModels;
    }


/*
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<VacationRequestModel> getVacationRequestModels() {
        return vacationRequestModels;
    }

    public void setVacationRequestModels(List<VacationRequestModel> vacationRequestModels) {
        this.vacationRequestModels = vacationRequestModels;
    }



    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
*/

}
