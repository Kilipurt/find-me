package com.findme.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.util.Date;

@Entity
@Getter
@Setter
public class User {

    @Id
    @SequenceGenerator(name = "USERS_SEQ", sequenceName = "USERS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_SEQ")
    private Long id;

    private String firstName;

    private String lastName;

    private String phone;

    private String password;

    //TODO from existed data
    private String country;

    private String city;

    private Integer age;

    private Date dateRegistered;

    private Date dateLastActive;

    private String relationshipStatus;

    private String religion;

    //TODO from existed data
    private String school;

    private String university;

    //private String[] interests;
}