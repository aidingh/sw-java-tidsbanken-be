package com.example.timebankapiproject;

import com.example.timebankapiproject.enums.VacationRequestStatus;
import com.example.timebankapiproject.models.UserModel;
import com.example.timebankapiproject.models.VacationRequestModel;
import com.example.timebankapiproject.repository.UserRepository;
import com.example.timebankapiproject.repository.VacationRequestRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
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

            user.setFirstName("aidin yaaao");
            user.setId("dasdas7dasdsa7");

            VacationRequestModel vacReq = new VacationRequestModel();
            vacReq.setStartPeriod(LocalDate.of(2022,03,22));
            vacReq.setEndPeriod(LocalDate.of(2022,03,25));
            vacReq.setStatus(VacationRequestStatus.DENIED);
            vacReq.setTitle("Vacation 2");

            VacationRequestModel vacReq1 = new VacationRequestModel();
            vacReq1.setStartPeriod(LocalDate.of(2022,03,10));
            vacReq1.setEndPeriod(LocalDate.of(2022,03,20));
            vacReq1.setTitle("vacation 1");
            vacReq1.setStatus(VacationRequestStatus.APPROVED);

            VacationRequestModel vacReq2 = new VacationRequestModel();
            vacReq2.setStartPeriod(LocalDate.of(2022,03,10));
            vacReq2.setEndPeriod(LocalDate.of(2022,03,20));
            vacReq2.setTitle("vacation 1");
            vacReq2.setStatus(VacationRequestStatus.PENDING);

            List<VacationRequestModel> listOfRequests = new ArrayList<>();
            listOfRequests.add(vacReq1);
            listOfRequests.add(vacReq);
            listOfRequests.add(vacReq2);
            user.setVacationRequestModels(listOfRequests);
            vacRepo.save(vacReq2);
            vacRepo.save(vacReq);
            vacRepo.save(vacReq1);
            repo.save(user);



            user.setVacationRequestModels(listOfRequests);
            repo.save(user);
        };
    }


}
