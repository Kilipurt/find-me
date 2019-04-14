package com.findme.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "POST")
public class Post {
    private Long id;
    private String message;
    private Date datePosted;
    private User userPosted;
    private List<User> usersTagged;
    private User userPagePosted;
    private String location;
    //TODO
    //levels permissions

    //TODO
    //comments

    @Id
    @SequenceGenerator(name = "POST_SEQ", sequenceName = "POST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POST_SEQ")
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    @Column(name = "MESSAGE")
    public String getMessage() {
        return message;
    }

    @Column(name = "DATE_POSTED")
    public Date getDatePosted() {
        return datePosted;
    }

    @OneToOne
    @JoinColumn(name = "USER_POSTED")
    public User getUserPosted() {
        return userPosted;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_POST",
            joinColumns = @JoinColumn(name = "POST_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    public List<User> getUsersTagged() {
        return usersTagged;
    }

    @OneToOne
    @JoinColumn(name = "USER_PAGE_POSTED")
    public User getUserPagePosted() {
        return userPagePosted;
    }

    @Column(name = "LOCATION")
    public String getLocation() {
        return location;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public void setUserPosted(User userPosted) {
        this.userPosted = userPosted;
    }

    public void setUsersTagged(List<User> usersTagged) {
        this.usersTagged = usersTagged;
    }

    public void setUserPagePosted(User userPagePosted) {
        this.userPagePosted = userPagePosted;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
