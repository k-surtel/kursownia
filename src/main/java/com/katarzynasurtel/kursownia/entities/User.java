package com.katarzynasurtel.kursownia.entities;

import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.swing.tree.RowMapper;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idUser;
    @NotEmpty
    private String name;
    private String surname;
    @NotEmpty(message = "First name is a required field")
    private String login;
    @NotEmpty
    private String password;
    @OneToMany(mappedBy="author")
    private List<Course> myCourses;
    @ManyToMany
    @JoinTable(
            name = "subscriptions",
            joinColumns = { @JoinColumn(name = "idUser") },
            inverseJoinColumns = {@JoinColumn(name = "idCourse")}
    )
    private List<Course> subbedCourses;

    public User() {}
    public User(String name, String surname, String login, String password) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
    }

    public Integer getIdUser() { return idUser; }
    public void setIdUser(Integer idUser) { this.idUser = idUser; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public List<Course> getMyCourses() {return  myCourses; }
    public void setMyCourses(List<Course> myCourses) { this.myCourses = myCourses; }
    public List<Course> getSubbedCourses() { return subbedCourses; }
    public void setSubbedCourses(List<Course> subbedCourses) { this.subbedCourses = subbedCourses; }

}
