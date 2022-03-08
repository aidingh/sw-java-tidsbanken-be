package com.example.timebankapiproject.repository;

import com.example.timebankapiproject.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserModel,Integer> {

}
