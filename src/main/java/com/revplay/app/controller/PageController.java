package com.revplay.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class PageController {
    private static final Logger log = LoggerFactory.getLogger(PageController.class);

    @GetMapping("/")
    public String landing() {
        log.debug("GET /landing");
        log.debug("GET /");
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        log.debug("GET /login");
        log.debug("GET /login");
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        log.debug("GET /register");
        log.debug("GET /register");
        return "register";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }

    @GetMapping("/home")
    public String home() {
        log.debug("GET /home");
        log.debug("GET /home");
        return "home";
    }

    @GetMapping("/favorites")
    public String favorites() {
        log.debug("GET /favorites");
        return "favorites";
    }

    @GetMapping("/browse")
    public String browse() {
        log.debug("GET /browse");
        log.debug("GET /browse");
        return "browse";
    }

    @GetMapping("/playlists")
    public String playlists() {
        return "playlists";
    }

    @GetMapping("/playlist/{id}")
    public String playlist(@PathVariable Long id) {
        log.debug("GET /playlist/{}", id);
        return "playlist";
    }

    @GetMapping("/album/{id}")
    public String album(@PathVariable Long id) {
        return "album";
    }

    @GetMapping("/artist/{id}")
    public String artist(@PathVariable Long id) {
        return "artist";
    }

    @GetMapping("/podcasts")
    public String podcasts() {
        return "podcasts";
    }

    @GetMapping("/podcast/{id}")
    public String podcast(@PathVariable Long id) {
        return "podcast";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    @GetMapping("/artist-dashboard")
    public String artistDashboard() {
        log.debug("GET /artistDashboard");
        log.debug("GET /artist-dashboard");
        return "artist-dashboard";
    }
}
