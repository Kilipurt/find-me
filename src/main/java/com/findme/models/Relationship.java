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
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
public class Relationship implements Serializable {

    @Id
    @SequenceGenerator(name = "RELATIONSHIP_SEQ", sequenceName = "RELATIONSHIP_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RELATIONSHIP_SEQ")
    private Long id;

    @OneToOne()
    @JoinColumn(name = "USER_FROM")
    private User userFrom;

    @OneToOne()
    @JoinColumn(name = "USER_TO")
    private User userTo;

    private String status;

    private Date lastStatusChange;
}