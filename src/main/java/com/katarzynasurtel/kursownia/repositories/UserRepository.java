package com.katarzynasurtel.kursownia.repositories;

import java.util.List;

import com.katarzynasurtel.kursownia.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByLogin(String login);
    List<User> findAll();
}
