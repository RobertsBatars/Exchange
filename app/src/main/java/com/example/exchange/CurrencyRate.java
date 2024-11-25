package com.example.exchange;

public class CurrencyRate {
    private String currencyCode;
    private String currencyName;
    private double exchangeRate;

    public CurrencyRate(String currencyCode, String currencyName, double exchangeRate) {
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
        this.exchangeRate = exchangeRate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    @Override
    public String toString() {
        return String.format("%s - %.3f", currencyCode, exchangeRate);
    }
}