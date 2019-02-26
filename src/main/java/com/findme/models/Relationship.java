package com.findme.models;

import javax.persistence.*;

@Entity
@Table(name = "RELATIONSHIP")
public class Relationship {
    private long id;
    private User userFrom;
    private User userTo;
    private String status;

    @Id
    @SequenceGenerator(name = "REL_STATUS_SEQ", sequenceName = "REL_STATUS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REL_STATUS_SEQ")
    @Column(name = "ID")
    public long getId() {
        return id;
    }

    @OneToOne()
    @JoinColumn(name = "USER_FROM")
    public User getUserFrom() {
        return userFrom;
    }

    @OneToOne()
    @JoinColumn(name = "USER_TO")
    public User getUserTo() {
        return userTo;
    }

    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
