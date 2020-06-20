package com.findme.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import java.util.Date;

@Entity
@Getter
@Setter
public class Message {

    @Id
    @SequenceGenerator(name = "MES_SEQ", sequenceName = "MES_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MES_SEQ")
    private Long id;

    private String text;

    private Date dateSent;

    private Date dateRead;

    @ManyToOne
    @JoinColumn(name = "USER_FROM")
    private User userFrom;

    @ManyToOne
    @JoinColumn(name = "USER_TO")
    private User userTo;

    private Date dateEdited;

    private Date dateDeleted;
}