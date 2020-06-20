package com.findme.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
public class Relationship implements Serializable {

    private String status;

    @Id
    @OneToOne()
    @JoinColumn(name = "USER_FROM")
    private User userFrom;

    @Id
    @OneToOne()
    @JoinColumn(name = "USER_TO")
    private User userTo;

    private Date lastStatusChange;
}