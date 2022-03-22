package com.example.timebankapiproject.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

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
    @JsonManagedReference
    @OneToMany(mappedBy = "userModel", cascade = CascadeType.ALL)
    public List<VacationRequestModel> vacationRequestModels;

    @JsonGetter("vacationRequestModels")
    public List<String> convertUserModelToStringURI(){
        if (vacationRequestModels != null) {
            return vacationRequestModels.stream().map(vacation -> String.format("/api/v1/vacation/id/%d", vacation.getId())).collect(Collectors.toList());
        }
        return null;
    }

    @Column
    public boolean isAdmin;

    public void setVacationRequestModel(VacationRequestModel vacationRequest){
        this.vacationRequestModels.add(vacationRequest);
    }

    public List<VacationRequestModel> getVacationRequestModels() {
        return vacationRequestModels;
    }

}
