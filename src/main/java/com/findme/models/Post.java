package com.findme.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "POST")
@Getter
@Setter
public class Post {
    @Id
    @SequenceGenerator(name = "POST_SEQ", sequenceName = "POST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POST_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "DATE_POSTED")
    private Date datePosted;

    @OneToOne
    @JoinColumn(name = "USER_POSTED")
    private User userPosted;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_POST",
            joinColumns = @JoinColumn(name = "POST_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    private List<User> usersTagged;

    @OneToOne
    @JoinColumn(name = "USER_PAGE_POSTED")
    private User userPagePosted;

    @Column(name = "LOCATION")
    private String location;

    //TODO
    //levels permissions

    //TODO
    //comments
}