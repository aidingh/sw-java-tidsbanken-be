package com.example.timebankapiproject;

import com.example.timebankapiproject.models.UserModel;
import com.example.timebankapiproject.models.VacationRequest;
import com.example.timebankapiproject.models.VacationRequestStatus;
import com.example.timebankapiproject.repository.UserRepository;
import com.example.timebankapiproject.repository.VacationRequestRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class TimeBankApiProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeBankApiProjectApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(UserRepository repo, VacationRequestRepository vacRepo) {
        return args -> {
            UserModel user = new UserModel();
            user.setAdmin(true);
            user.setEmail("användare.hej@gmail.com");

            user.setName("aidin yaaao");


            VacationRequest vacReq = new VacationRequest();
            vacReq.setStartPeriod(new Date());
            vacReq.setEndPeriod(new Date());
            vacReq.setStatus(VacationRequestStatus.APPROVED);

            VacationRequest vacReq1 = new VacationRequest();
            vacReq1.setStartPeriod(new Date());
            vacReq1.setEndPeriod(new Date());
            vacReq1.setStatus(VacationRequestStatus.APPROVED);

            List<VacationRequest> listOfRequests = new ArrayList<>();
            listOfRequests.add(vacReq1);
            listOfRequests.add(vacReq);
            vacRepo.save(vacReq);
            vacRepo.save(vacReq1);

            user.setVacationRequests(listOfRequests);
            repo.save(user);
        };
    }
}
