package com.example.bankservice.dto;

public class BankResponse {

    private Long id;
    private String name;
    private Integer rating;
    private String country;

    public BankResponse(Long id, String name, Integer rating, String country) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getRating() {
        return rating;
    }

    public String getCountry() {
        return country;
    }
}
