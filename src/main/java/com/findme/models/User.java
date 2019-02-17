package com.findme.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "USERS")
public class User {
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
    //TODO enum
    private String relationshipStatus;
    private String religion;
    //TODO from existed data
    private String school;
    private String university;

    private List<Message> messagesSent;
    private List<Message> messagesReceived;

    //private String[] interests;

    @Id
    @SequenceGenerator(name = "USERS_SEQ", sequenceName = "USERS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_SEQ")
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    @Column(name = "FIRST_NAME")
    public String getFirstName() {
        return firstName;
    }

    @Column(name = "LAST_NAME")
    public String getLastName() {
        return lastName;
    }

    @Column(name = "PHONE")
    public String getPhone() {
        return phone;
    }

    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

    @Column(name = "COUNTRY")
    public String getCountry() {
        return country;
    }

    @Column(name = "CITY")
    public String getCity() {
        return city;
    }

    @Column(name = "AGE")
    public Integer getAge() {
        return age;
    }

    @Column(name = "DATE_REGISTERED")
    public Date getDateRegistered() {
        return dateRegistered;
    }

    @Column(name = "DATE_LAST_ACTIVE")
    public Date getDateLastActive() {
        return dateLastActive;
    }

    @Column(name = "RELATIONSHIP_STATUS")
    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    @Column(name = "RELIGION")
    public String getReligion() {
        return religion;
    }

    @Column(name = "SCHOOL")
    public String getSchool() {
        return school;
    }

    @Column(name = "UNIVERSITY")
    public String getUniversity() {
        return university;
    }

    @OneToMany(mappedBy = "userFrom")
    public List<Message> getMessagesSent() {
        return messagesSent;
    }

    @OneToMany(mappedBy = "userTo")
    public List<Message> getMessagesReceived() {
        return messagesReceived;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public void setDateLastActive(Date dateLastActive) {
        this.dateLastActive = dateLastActive;
    }

    public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public void setMessagesSent(List<Message> messagesSent) {
        this.messagesSent = messagesSent;
    }

    public void setMessagesReceived(List<Message> messagesReceived) {
        this.messagesReceived = messagesReceived;
    }
}
