package com.katarzynasurtel.kursownia;

import com.katarzynasurtel.kursownia.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class KursowniaApplication {

    public static void main(String[] args) {
        SpringApplication.run(KursowniaApplication.class, args);
    }

}
