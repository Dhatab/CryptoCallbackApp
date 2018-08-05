package com.cryptocallback.cryptocallback.FragmentFavs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.cryptocallback.cryptocallback.CoinDetailActivity.CoinDetailActivity;
import com.cryptocallback.cryptocallback.FragmentHome.HomeFragment;
import com.cryptocallback.cryptocallback.Graphs.Graph_AppController;
import com.cryptocallback.cryptocallback.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 4/12/2018.
 */

public class FavoriteFragment extends Fragment {

    private RecyclerView favRV;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference myref;
    private String intentPrice, jsonResponse, jsonResponse0, jsonResponse1, jsonResponse2;



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_favs, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        favRV = (RecyclerView) view.findViewById(R.id.recyclerViewFavs);
        favRV.setHasFixedSize(true);
        favRV.setLayoutManager(new LinearLayoutManager(getContext()));
        myref = FirebaseDatabase.getInstance().getReference(firebaseAuth.getCurrentUser().getUid()).child("/Favorites");

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();

        FirebaseRecyclerAdapter<FavListRef, FavsViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<FavListRef, FavsViewHolder>
                (FavListRef.class, R.layout.fav_list_item, FavsViewHolder.class, myref)



        {
            @Override
            protected void populateViewHolder(final FavsViewHolder viewHolder, final FavListRef model, int position) {
                viewHolder.setCoin_Name(model.getCoin_Name());
                viewHolder.setCoin_Symbol(model.getCoin_Symbol());

                AssetManager assetManager = getContext().getAssets();

                try {
                    InputStream ims = assetManager.open(model.getCoin_Symbol().toLowerCase()+".jpg");
                    Drawable d = Drawable.createFromStream(ims, null);
                    viewHolder.imageViewFav.setImageDrawable(d);
                } catch (IOException ex) {
                    return;
                }


                StringRequest req = new StringRequest(Request.Method.GET, "https://api.coinmarketcap.com/v1/ticker/"+model.getCoin_Name().toLowerCase().replace(' ','-')+"/",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                progressDialog.dismiss();

                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject o = jsonArray.getJSONObject(i);
                                        String pricefav = o.getString("price_usd");
                                        String one_fav = o.getString("percent_change_1h");
                                        String tf_fav = o.getString("percent_change_24h");
                                        String sd_fav = o.getString("percent_change_7d");

                                        double x = Double.parseDouble(pricefav);
                                        if (x<1){
                                            jsonResponse = String.format("%.3f", x);
                                        }else {
                                            jsonResponse = String.format("%.2f", x);
                                        }
                                        jsonResponse0 = one_fav;
                                        jsonResponse1 = tf_fav;
                                        jsonResponse2 = sd_fav;
                                        intentPrice = pricefav;
                                    }

                                    //for intent
                                    viewHolder.intPrice = intentPrice;
                                    viewHolder.inttwo = jsonResponse0;
                                    viewHolder.intthree = jsonResponse1;
                                    viewHolder.intfour = jsonResponse2;

                                    //For recyclerview
                                    viewHolder.textView_Fav_Price.setText("$" + jsonResponse);

                                    if (jsonResponse0.contains("-")) {
                                        viewHolder.textView_Fav_OneHR.setText(Html.fromHtml(" 1h: " +"<font color= #ff4444>" +jsonResponse0 + "%"+ "</font>" ));
                                    } else {
                                        viewHolder.textView_Fav_OneHR.setText(Html.fromHtml(" 1h: " +"<font color= #009900>" +jsonResponse0 + "%"+ "</font>"));
                                    };

                                    if (jsonResponse1.contains("-")) {
                                        viewHolder.textView_Fav_TF_HR.setText(Html.fromHtml(" 24h: " +"<font color= #ff4444>" +jsonResponse1 + "%"+ "</font>" ));
                                    } else {
                                        viewHolder.textView_Fav_TF_HR.setText(Html.fromHtml(" 24h: " +"<font color= #009900>" +jsonResponse1 + "%"+ "</font>"));
                                    };

                                    if (jsonResponse2.contains("-")) {
                                        viewHolder.textView_Fav_Seven_Day.setText(Html.fromHtml(" 7d: " +"<font color= #ff4444>" +jsonResponse2 + "%"+ "</font>" ));
                                    } else {
                                        viewHolder.textView_Fav_Seven_Day.setText(Html.fromHtml(" 7d: " +"<font color= #009900>" +jsonResponse2 + "%"+ "</font>"));
                                    };


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }

                });


                // Adding request to request queue
                Graph_AppController.getInstance().addToRequestQueue(req);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), CoinDetailActivity.class);
                        intent.putExtra("Coin Name", model.getCoin_Name());
                        intent.putExtra("Coin Sym", model.getCoin_Symbol());
                        intent.putExtra("Price", viewHolder.intPrice);
                        intent.putExtra("1 Hour", viewHolder.inttwo);
                        intent.putExtra("24 Hour", viewHolder.intthree);
                        intent.putExtra("7 Days", viewHolder.intfour);
                        getContext().startActivity(intent);
                    }

                });
            }
        };



        favRV.setAdapter(recyclerAdapter);

        return view;
    }

    public static class FavsViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView textView_title;
        TextView textView_decription;
        TextView textView_Fav_Price;
        TextView textView_Fav_OneHR;
        TextView textView_Fav_TF_HR;
        TextView textView_Fav_Seven_Day;
        ImageView imageViewFav;
        String intPrice, inttwo,intthree,intfour;


        public FavsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            textView_title = (TextView) itemView.findViewById(R.id.TV_FAV_COIN);
            textView_decription = (TextView) itemView.findViewById(R.id.TV_FAV_SYM);
            imageViewFav = (ImageView) itemView.findViewById(R.id.image_CoinFAV);
            textView_Fav_Price = (TextView) itemView.findViewById(R.id.TV_FAV_Price);
            textView_Fav_OneHR = (TextView) itemView.findViewById(R.id.Fav_OneHR);
            textView_Fav_TF_HR = (TextView) itemView.findViewById(R.id.FAV_TFHour);
            textView_Fav_Seven_Day = (TextView) itemView.findViewById(R.id.FAV_SevenDay);


        }

        public void setCoin_Name(String coin_Name) {
            textView_title.setText(coin_Name);


        }

        public void setCoin_Symbol(String coin_Symbol) {
            textView_decription.setText(coin_Symbol);

        }


    }

}