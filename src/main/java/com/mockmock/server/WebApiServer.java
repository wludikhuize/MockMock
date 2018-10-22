package com.mockmock.server;

import com.mockmock.AppStarter;
import com.mockmock.Settings;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;

@Service
public class WebApiServer implements com.mockmock.server.Server {
    private int port;

    private Settings settings;

    public void setPort(int port) {
        this.port = port;
    }

    public void start() {
        SpringApplication app = new SpringApplication(AppStarter.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", (Object) this.port));
        app.run();
    }

    @Autowired
    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
