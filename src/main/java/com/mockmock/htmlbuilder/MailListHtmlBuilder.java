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
    private ArrayList<MockMail> mailQueue;

    public void setMailQueue(ArrayList<MockMail> mailQueue) {
        // create a shalow copy of the mailQueue to avoid getting concurrency exceptions
        this.mailQueue = new ArrayList<MockMail>(mailQueue);
    }

    public String build() {
        String output = "<div class=\"container\">\n";

        if (mailQueue == null || mailQueue.size() == 0) {
            output += "  <h2>No emails in queue</h2>\n";
        } else {
            String mailText = mailQueue.size() == 1 ? "email" : "emails";
            output += "  <h1>You have " + mailQueue.size() + " " + mailText
                    + "! <small class=\"deleteLink\"><a class=\"delete\" href=\"/mail/delete/all\">Delete all</a></small></h1>\n";
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
            for (MockMail mail : mailQueue) {
                output += buildMailRow(mail);
            }
            output += "    </tbody>\n";
            output += "  </table>\n";
        }

        long lastTimeStamp = getLastTimestamp();

        output += "<script type=\"text/javascript\">var previousTimestamp = '" + lastTimeStamp + "';</script>";
        output += "</div>\n";

        return output;
    }

    private long getLastTimestamp() {
        if (mailQueue.size() == 0)
            return 0;

        return mailQueue.get(0).getReceivedTime();
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
