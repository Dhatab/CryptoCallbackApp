package com.cryptocallback.cryptocallback.FragmentFavs;

public class FavsModel {
    private String coin_name;

    public FavsModel(String coin_name) {
        this.coin_name = coin_name;
    }

    public FavsModel() {
    }

    public String getCoin_name() {
        return coin_name;
    }

    public void setCoin_name(String coin_name) {
        this.coin_name = coin_name;
    }
}
