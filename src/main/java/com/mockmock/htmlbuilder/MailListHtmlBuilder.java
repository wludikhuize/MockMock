package com.mockmock.htmlbuilder;

import com.mockmock.mail.MailQueue;
import com.mockmock.mail.MockMail;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Service
public class MailListHtmlBuilder implements HtmlBuilder {
    private ArrayList<MockMail> emails;
    private MailQueue mailQueue;

    @Autowired
    public void setMailQueue(final MailQueue mailQueue) {
        this.mailQueue = mailQueue;
        this.emails = mailQueue.getMailQueue();
    }

    public void setMailQueue(ArrayList<MockMail> mailQueue) {
        // create a shalow copy of the mailQueue to avoid getting concurrency exceptions
        this.emails = new ArrayList<MockMail>(mailQueue);
    }

    public String build() {
        String output = "<div class=\"container\">\n";

        if (emails == null || emails.size() == 0) {
            output += "  <h2>No emails in queue</h2>\n";
        } else {
            String mailText = emails.size() == 1 ? "email" : "emails";
            output += "  <h1>You have " + emails.size() + " " + mailText + "!";
            output += "  <small class=\"deleteLink\"><a class=\"delete\" href=\"/mail/delete/all\">Delete all</a></small>\n";
            output += "  <small class=\"refreshLink\"><a class=\"refresh\" href=\"#\">Disable refresh</a></small></h1>\n";
            output += "  <table class=\"table table-striped\">\n";
            output += "    <thead>\n";
            output += "      <th>Date received</th>\n";
            output += "      <th>From</th>\n";
            output += "      <th>To</th>\n";
            output += "      <th>CC</th>\n";
            output += "      <th>Subject</th>\n";
            output += "      <th>Action</th>\n";
            output += "    </thead>\n";
            output += "    <tbody>\n";
            for (MockMail mail : emails) {
                output += buildMailRow(mail);
            }
            output += "    </tbody>\n";
            output += "  </table>\n";
        }

        output += "<script type=\"text/javascript\">var currentHashcode = '" + this.mailQueue.hashCode()
                + "';</script>";
        output += "</div>\n";

        return output;
    }

    private long getLastTimestamp() {
        if (emails.size() == 0)
            return 0;

        return emails.get(0).getReceivedTime();
    }

    private String buildMailRow(MockMail mail) {
        long receivedTimestamp = mail.getReceivedTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        Date date = new Date(receivedTimestamp);
        String dateOutput = formatter.format(date);

        StringFromHtmlBuilder fromBuilder = new StringFromHtmlBuilder();
        fromBuilder.setMockMail(mail);
        String fromOutput = fromBuilder.build();

        StringRecipientHtmlBuilder toRecipientBuilder = new StringRecipientHtmlBuilder();
        toRecipientBuilder.setMaxLength(27);
        toRecipientBuilder.setMockMail(mail);
        toRecipientBuilder.setRecipientType(MimeMessage.RecipientType.TO);
        String toOutput = toRecipientBuilder.build();

        StringRecipientHtmlBuilder ccRecipientBuilder = new StringRecipientHtmlBuilder();
        ccRecipientBuilder.setMaxLength(27);
        ccRecipientBuilder.setMockMail(mail);
        ccRecipientBuilder.setRecipientType(MimeMessage.RecipientType.CC);
        String ccOutput = ccRecipientBuilder.build();

        String subjectOutput;
        if (mail.getSubject() == null) {
            subjectOutput = "<em>No subject given</em>";
        } else {
            subjectOutput = StringEscapeUtils.escapeHtml(mail.getSubject());
        }

        return "<tr>\n" + "  <td>" + dateOutput + "</td>\n" + " <td>" + fromOutput + "</td>\n" + "  <td>" + toOutput
                + "</td>\n" + "  <td>" + ccOutput + "</td>\n" + "  <td><a title=\""
                + StringEscapeUtils.escapeHtml(mail.getSubject()) + "\" href=\"/view/" + mail.getId() + "\">"
                + subjectOutput + "</a></td>\n" + "  <td><a title=\"Delete this mail\" href=\"/delete/" + mail.getId()
                + "\"><em>Delete</em></a></td>\n" + "</tr>";
    }
}
