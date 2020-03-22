package com.cryptocallback.cryptocallback.FragmentHome;

/**
 * Created by User on 4/7/2018.
 */

public class ListItem {
    private String symbol;
    private String name;
    private String image;
    private String current_price;
    private String market_cap_rank;
    private String market_cap_change_24h;

    public ListItem(String symbol, String name, String image, String current_price, String market_cap_rank, String price_change_percentage_24h) {
        this.symbol = symbol;
        this.name = name;
        this.image = image;
        this.current_price = current_price;
        this.market_cap_rank = market_cap_rank;
        this.market_cap_change_24h = price_change_percentage_24h;
    }

    public ListItem() {
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(String current_price) {
        this.current_price = current_price;
    }

    public String getMarket_cap_rank() {
        return market_cap_rank;
    }

    public void setMarket_cap_rank(String market_cap_rank) {
        this.market_cap_rank = market_cap_rank;
    }

    public String getMarket_cap_change_24h() {
        return market_cap_change_24h;
    }

    public void setMarket_cap_change_24h(String market_cap_change_24h) {
        this.market_cap_change_24h = market_cap_change_24h;
    }
}
