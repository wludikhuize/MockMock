package com.mockmock.controllers.model;

import java.util.Date;

public class SearchParameters {
    private Long id;
    private String from;
    private String to;
    private String cc;
    private String bcc;
    private String subject;
    private String content;
    private Date dateFrom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setCC(String cc) {
        this.cc = cc;
    }

    public void setBCC(String bcc) {
        this.bcc = bcc;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFrom() {
        return this.from;
    }

    public String getTo() {
        return this.to;
    }

    public String getCC() {
        return this.cc;
    }

    public String getBCC() {
        return this.bcc;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getContent() {
        return this.content;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }
}
