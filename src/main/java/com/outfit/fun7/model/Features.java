package com.outfit.fun7.model;

public class Features {
    private String multiplayer;
    private String customer_support;
    private String ads;

    public Features(String multiplayer, String customer_support, String ads) {
        this.multiplayer = multiplayer;
        this.customer_support = customer_support;
        this.ads = ads;
    }

    @Override
    public String toString() {
        return "{" +
                "\"multiplayer\": " + "\"" + this.multiplayer + "\"" +
                ", \"user-support\": " + "\"" + this.customer_support + "\"" +
                ", \"ads\": " + "\"" + this.ads + "\"" +
                "}";
    }
}
