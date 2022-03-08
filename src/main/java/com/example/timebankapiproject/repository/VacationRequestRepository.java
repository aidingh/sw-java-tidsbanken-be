package com.example.timebankapiproject.repository;

import com.example.timebankapiproject.models.VacationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacationRequestRepository extends JpaRepository<VacationRequest,Integer> {
}
