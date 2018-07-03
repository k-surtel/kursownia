package com.katarzynasurtel.kursownia.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idCourse;
    @NotEmpty
    private String name;
    @ManyToOne
    @JoinColumn(name = "idUser")
    private User author;
    @ManyToMany
    @JoinTable(
            name = "subscriptions",
            joinColumns = { @JoinColumn(name = "idCourse") },
            inverseJoinColumns = {@JoinColumn(name = "idUser")}
    )
    private List<User> subbedUsers;
    private String description;

    public Course() {}
    public Course(String name, User author, String description) { this.name = name; this.author = author; this.description = description; }

    public Integer getIdCourse() { return idCourse; }
    public void setIdCourse(Integer idCourse) { this.idCourse = idCourse; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
    public List<User> getSubbedUsers() { return subbedUsers; }
    public void setSubbedUsers(List<User> subbedUsers) { this.subbedUsers = subbedUsers; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

}
