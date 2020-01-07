package com.mockmock.controllers;

import java.util.regex.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mockmock.mail.MailQueue;
import com.mockmock.mail.MockMail;
import com.mockmock.controllers.model.MockMailDto;
import com.mockmock.controllers.model.SearchParameters;

@RestController
@Service
public class InboxController {

    private MailQueue mailQueue;

    @GetMapping("/inbox")
    public ArrayList<MockMailDto> inbox() {
        final ArrayList<MockMail> inbox = mailQueue.getMailQueue();

        return MockMailDto.mapFrom(inbox);
    }

    @GetMapping("/inbox/{id}")
    public MockMailDto inbox(@PathVariable final long id) {
        final ArrayList<MockMail> inbox = mailQueue.getMailQueue();

        final MockMail foundMail = inbox.stream().filter(mail -> mail.getId() == id).findAny().orElse(null);

        return MockMailDto.mapFrom(foundMail);
    }

    @PutMapping("/inbox/find")
    public ArrayList<MockMailDto> find(@RequestBody final SearchParameters searchParameters) {
        final ArrayList<MockMail> inbox = mailQueue.getMailQueue();

        Stream<MockMail> stream = inbox.stream();

        final Long id = searchParameters.getId();
        final String from = searchParameters.getFrom();
        final String to = searchParameters.getTo();
        final String cc = searchParameters.getCC();
        final String bcc = searchParameters.getBCC();
        final String subject = searchParameters.getSubject();
        final String content = searchParameters.getContent();
        final Date dateFrom = searchParameters.getDateFrom();

        if (id != null) {
            stream = stream.filter(mail -> mail.getId() == id);
        }

        if (from != null) {
            stream = stream.filter(mail -> mail.getFrom().contains(from));
        }

        if (to != null) {
            stream = stream.filter(mail -> Arrays.asList(mail.getTo()).contains(to));
        }

        if (cc != null) {
            stream = stream.filter(mail -> Arrays.asList(mail.getCC()).contains(cc));
        }

        if (bcc != null) {
            stream = stream.filter(mail -> Arrays.asList(mail.getBCC()).contains(bcc));
        }

        if (subject != null) {
            final Pattern p = Pattern.compile(subject, Pattern.CASE_INSENSITIVE);
            stream = stream.filter(mail -> p.matcher(mail.getSubject()).find());
        }

        if (content != null) {
            final Pattern p = Pattern.compile(content, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

            stream = stream.filter(mail -> {
                final String body = mail.getBody();
                final String bodyHtml = mail.getBodyHtml();

                if (body != null) {
                    return p.matcher(body).find();
                } else if (bodyHtml != null) {
                    return p.matcher(bodyHtml).find();
                } else {
                    return false;
                }
            });
        }

        if (dateFrom != null) {
            stream = stream.filter(mail -> {
                final long timeStampFrom = new DateTime(dateFrom).getMillis();
                return mail.getReceivedTime() > timeStampFrom;
            });
        }

        final ArrayList<MockMail> foundMails = (ArrayList<MockMail>) stream.collect(Collectors.toList());

        return MockMailDto.mapFrom(foundMails);
    }

    @Autowired
    public void setMailQueue(final MailQueue mailQueue) {
        this.mailQueue = mailQueue;
    }
}