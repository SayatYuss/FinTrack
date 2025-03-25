package com.sayat.Web.Site.controllers;

import com.sayat.Web.Site.services.CurrencyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class CurrencyController {
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/")
    public String home(Model model) {
        Map<String, Double> rates = currencyService.getExchangeRates();

        // Курсы для отображения (USD, EUR, RUB, CNY -> KZT)
        model.addAttribute("usdToKzt", rates.getOrDefault("USD", 0.0));
        model.addAttribute("eurToKzt", rates.getOrDefault("EUR", 0.0));
        model.addAttribute("rubToKzt", rates.getOrDefault("RUB", 0.0));
        model.addAttribute("cnyToKzt", rates.getOrDefault("CNY", 0.0));

        return "home";
    }
}
