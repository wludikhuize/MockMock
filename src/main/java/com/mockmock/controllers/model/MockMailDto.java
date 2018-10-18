package com.mockmock.controllers.model;

import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.mail.internet.MimeMessage;

import com.mockmock.mail.MockMail;

public class MockMailDto {
    private long id;
    private String from;
    private String to;
    private String subject;
    private long receivedTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public long getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(long receivedTime) {
        this.receivedTime = receivedTime;
    }

    public static MockMailDto mapFrom(MockMail mockMail) {
        MockMailDto result = new MockMailDto();

        result.id = mockMail.getId();
        result.from = mockMail.getFrom();
        result.to = mockMail.getTo();
        result.subject = mockMail.getSubject();
        result.receivedTime = mockMail.getReceivedTime();

        return result;
    }

    public static ArrayList<MockMailDto> mapFrom(ArrayList<MockMail> mockMails) {
        ArrayList<MockMailDto> response = (ArrayList<MockMailDto>) mockMails.stream().map(elt -> mapFrom(elt))
                .collect(Collectors.toList());

        return response;
    }
}
