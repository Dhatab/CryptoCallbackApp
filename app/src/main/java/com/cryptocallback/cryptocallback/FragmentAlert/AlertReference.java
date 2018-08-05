package com.cryptocallback.cryptocallback.FragmentAlert;

/**
 * Created by User on 4/16/2018.
 */

public class AlertReference {

    private String coin_onesignal_id;
    private String coin_asset;
    private String coin_metric;
    private String coin_direction;
    private String coin_amount;


    public AlertReference(String coin_onesignal_id, String coin_asset, String coin_metric, String coin_direction, String coin_amount) {
        this.coin_onesignal_id = coin_onesignal_id;
        this.coin_asset = coin_asset;
        this.coin_metric = coin_metric;
        this.coin_direction = coin_direction;
        this.coin_amount = coin_amount;
    }

    public String getCoin_onesignal_id() {
        return coin_onesignal_id;
    }

    public String getCoin_asset() {
        return coin_asset;
    }

    public String getCoin_metric() {
        return coin_metric;
    }

    public String getCoin_direction() {
        return coin_direction;
    }

    public String getCoin_amount() {
        return coin_amount;
    }
}
