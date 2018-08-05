package com.cryptocallback.cryptocallback.FragmentFavs;

/**
 * Created by User on 4/21/2018.
 */

public class FavListRef {

    private String Coin_Name;
    private String Coin_Symbol;


    public FavListRef() {
    }

    public FavListRef(String Coin_Name, String Coin_Symbol) {
        this.Coin_Name = Coin_Name;
        this.Coin_Symbol = Coin_Symbol;


    }

    public String getCoin_Name() {
        return Coin_Name;
    }

    public void setCoin_Name(String coin_Name) {
        Coin_Name = coin_Name;
    }

    public String getCoin_Symbol() {
        return Coin_Symbol;
    }

    public void setCoin_Symbol(String coin_Symbol) {
        Coin_Symbol = coin_Symbol;
    }

}
