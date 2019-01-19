package com.findme.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "POST")
public class Post {
    private Long id;
    private String message;
    private Date datePosted;
    private User userPosted;
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
}
