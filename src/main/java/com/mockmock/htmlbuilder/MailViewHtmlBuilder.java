package com.mockmock.htmlbuilder;

import com.mockmock.mail.MockMail;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailViewHtmlBuilder implements HtmlBuilder {
    private MailViewHeadersHtmlBuilder headersBuilder;
    private AddressesHtmlBuilder addressesHtmlBuilder;

    private MockMail mockMail;

    public void setMockMail(MockMail mockMail) {
        this.mockMail = mockMail;
    }

    public String build() {
        headersBuilder.setMockMail(mockMail);

        addressesHtmlBuilder.setMockMail(mockMail);

        String subjectOutput;
        if (mockMail.getSubject() == null) {
            subjectOutput = "<em>No subject given</em>";
        } else {
            subjectOutput = StringEscapeUtils.escapeHtml(mockMail.getSubject());
        }

        subjectOutput += " <small class=\"deleteLink\"><a href=\"/delete/" + mockMail.getId() + "\">Delete</a></small>";

        String output = "<div class=\"container content\">\n";

        output += "<h2>" + subjectOutput + "</h2>\n" + "  <div class=\"row\">\n";

        output += "    <div class=\"span10\" name=\"addresses\">\n" + "       <h3>Addresses</h3>\n" + "       "
                + addressesHtmlBuilder.build() + "    </div>\n";

        output += "    <div class=\"span10\" name=\"headers\">\n" + "       <h3>Mail headers</h3>\n" + "       "
                + headersBuilder.build() + "    </div>\n";

        if (mockMail.getBody() != null) {
            output += "    <div class=\"span10\" name=\"bodyPlainText\">\n" + "       <h3>Plain text body</h3>\n"
                    + "       <div class=\"well\">" + StringEscapeUtils.escapeHtml(mockMail.getBody()) + "</div>\n"
                    + "    </div>\n";
        }

        if (mockMail.getBodyHtml() != null) {
            // also show a parsed version in div
            output += " <div class=\"span10\" name=\"bodyHTML_Formatted\">\n" + " <h3>HTML body formatted</h3>\n"
                    + " <div class=\"well\">" + mockMail.getBodyHtml() + "</div>\n" + " </div>\n";

            // also show a parsed version in an iframe
            output += " <div class=\"span10\" name=\"iFrame\">\n" + " <h3>HTML body formatted in IFrame</h3>\n"
                    + " <iframe class=\"well\" src=\"/view/html/" + mockMail.getId()
                    + "\" style=\"width: 780px; height: 700px; overflow: scroll;\" style=\"\" name=\"bodyHTML_iFrame\">\n"
                    + " </iframe>\n" + " </div>";

            // also show html in a div with a Copy2Clipboard button
            output += " <div class=\"span10\" name=\"bodyHTML_Unformatted\">\n" + " <h3>HTML unformatted</h3>\n"
                    + " <button class=\"btn\" data-clipboard-target=\"#html2copy\">Copy2Clipboard</button>\n "
                    + " <div class=\"well\" id=\"html2copy\">" + StringEscapeUtils.escapeHtml(mockMail.getBodyHtml())
                    + "</div>\n" + " </div>\n";
        }

        // just output the raw mail so we're sure everything is on the screen
        if (mockMail.getRawMail() != null) {
            // output complete raw mail
            output += "    <div class=\"span10\" name=\"rawOutput\">\n" + "       <h3>Complete raw mail output</h3>\n"
                    + "       <div class=\"well\">" + StringEscapeUtils.escapeHtml(mockMail.getRawMail()) + "</div>\n"
                    + "    </div>\n";
        }

        output += "  </div>\n";

        output += "</div>\n";

        return output;
    }

    @Autowired
    public void setMailViewHeadersHtmlBuilder(MailViewHeadersHtmlBuilder mailViewHeadersHtmlBuilder) {
        this.headersBuilder = mailViewHeadersHtmlBuilder;
    }

    @Autowired
    public void setAddressesHtmlBuilder(AddressesHtmlBuilder addressesHtmlBuilder) {
        this.addressesHtmlBuilder = addressesHtmlBuilder;
    }
}