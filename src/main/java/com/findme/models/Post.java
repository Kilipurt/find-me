package com.findme.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import java.util.Date;

@Entity
@Getter
@Setter
public class Post {

    @Id
    @SequenceGenerator(name = "POST_SEQ", sequenceName = "POST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POST_SEQ")
    private Long id;

    private String message;

    private Date datePosted;

    @OneToOne
    @JoinColumn(name = "USER_POSTED")
    private User userPosted;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "USER_POST",
//            joinColumns = @JoinColumn(name = "POST_ID"),
//            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
//    private List<User> usersTagged;

    @OneToOne
    @JoinColumn(name = "USER_PAGE_POSTED")
    private User userPagePosted;

    private String location;

    //TODO
    //levels permissions

    //TODO
    //comments
}