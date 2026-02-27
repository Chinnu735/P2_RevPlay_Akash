package com.revplay.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String landing() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/favorites")
    public String favorites() {
        return "favorites";
    }

    @GetMapping("/browse")
    public String browse() {
        return "browse";
    }

    @GetMapping("/playlist/{id}")
    public String playlist() {
        return "playlist";
    }

    @GetMapping("/album/{id}")
    public String album() {
        return "album";
    }

    @GetMapping("/artist/{id}")
    public String artist() {
        return "artist";
    }

    @GetMapping("/podcast/{id}")
    public String podcast() {
        return "podcast";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    @GetMapping("/artist-dashboard")
    public String artistDashboard() {
        return "artist-dashboard";
    }

    @GetMapping("/artists")
    public String artists() {
        return "artists";
    }
}
