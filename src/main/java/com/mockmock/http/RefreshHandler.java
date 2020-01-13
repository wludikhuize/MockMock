package com.mockmock.http;

import com.mockmock.mail.MailQueue;
import com.mockmock.mail.MockMail;

import org.eclipse.jetty.server.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class RefreshHandler extends BaseHandler {
    private MailQueue mailQueue;

    @Override
    public void handle(String target, Request request, HttpServletRequest httpServletRequest,
            HttpServletResponse response) throws IOException, ServletException {
        if (!target.equals("/mail/refresh")) {
            return;
        }

        int hashcode = this.mailQueue.hashCode();

        setDefaultResponseOptions(response);

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(Integer.toString(hashcode));
        response.getWriter().flush();
        response.getWriter().close();

        request.setHandled(true);
    }

    @Autowired
    public void setMailQueue(MailQueue mailQueue) {
        this.mailQueue = mailQueue;
    }
}
