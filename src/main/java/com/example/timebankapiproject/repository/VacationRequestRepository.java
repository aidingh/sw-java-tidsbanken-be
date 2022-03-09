package com.example.timebankapiproject.repository;

import com.example.timebankapiproject.models.VacationRequestModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacationRequestRepository extends JpaRepository<VacationRequestModel,Integer> {
}
