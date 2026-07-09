package com.algaworks.algashop.presentation;

import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect" + properties.getDefaultRedirectUri();
    }

}
