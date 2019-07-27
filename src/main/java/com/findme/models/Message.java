package com.findme.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MESSAGE")
@Getter
@Setter
public class Message {

    @Id
    @SequenceGenerator(name = "MES_SEQ", sequenceName = "MES_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MES_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "DATE_SENT")
    private Date dateSent;

    @Column(name = "DATE_READ")
    private Date dateRead;

    @ManyToOne
    @JoinColumn(name = "USER_FROM")
    private User userFrom;

    @ManyToOne
    @JoinColumn(name = "USER_TO")
    private User userTo;

    @Column(name = "DATE_EDITED")
    private Date dateEdited;

    @Column(name = "DATE_DELETED")
    private Date dateDeleted;
}