package com.findme.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "RELATIONSHIP")
@Getter
@Setter
public class Relationship implements Serializable {
    @Column(name = "STATUS")
    private String status;

    @Id
    @OneToOne()
    @JoinColumn(name = "USER_FROM")
    private User userFrom;

    @Id
    @OneToOne()
    @JoinColumn(name = "USER_TO")
    private User userTo;

    @Column(name = "LAST_STATUS_CHANGE")
    private Date lastStatusChange;
}