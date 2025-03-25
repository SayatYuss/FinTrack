package com.sayat.Web.Site.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyService {
    private static final String API_KEY = "481dcb6dc30782e42c0533b0"; // Замени на свой ключ
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/";

    private final RestTemplate restTemplate;

    public CurrencyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Double> getExchangeRates() {
        String[] currencies = {"USD", "EUR", "RUB", "CNY"};
        Map<String, Double> rates = new HashMap<>();

        for (String currency : currencies) {
            String url = UriComponentsBuilder.fromHttpUrl(API_URL + currency + "/KZT").toUriString();
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && "success".equals(response.get("result"))) {
                Object rateObj = response.get("conversion_rate");
                if (rateObj instanceof Number) {
                    rates.put(currency, ((Number) rateObj).doubleValue());
                }
            }
        }
        return rates;
    }
}
