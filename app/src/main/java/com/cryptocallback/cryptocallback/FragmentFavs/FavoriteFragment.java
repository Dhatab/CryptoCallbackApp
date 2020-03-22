package com.cryptocallback.cryptocallback.FragmentFavs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cryptocallback.cryptocallback.CoinDetailActivity.CoinDetailActivity;
import com.cryptocallback.cryptocallback.FragmentHome.ListItem;
import com.cryptocallback.cryptocallback.FragmentHome.MyAdapter;
import com.cryptocallback.cryptocallback.Graphs.Graph_AppController;
import com.cryptocallback.cryptocallback.R;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by User on 4/12/2018.
 */

public class FavoriteFragment extends Fragment {

    private RecyclerView favRV;
    private List<ListItem> listItems;
    private RecyclerView.Adapter adapter;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_favs, container, false);

        favRV = (RecyclerView) view.findViewById(R.id.recyclerViewFavs);
        favRV.setHasFixedSize(true);
        favRV.setLayoutManager(new LinearLayoutManager(getContext()));
        listItems = new ArrayList<>();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(firebaseUser.getUid()).child("Favorites");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    FavsModel favsModel = dataSnapshot1.getValue(FavsModel.class);
                    loadFavorites(favsModel.getCoin_name().toLowerCase());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;

    }

    private void loadFavorites(String coin_name) {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Loading Data...");
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false&ids="+coin_name,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                JSONArray array = new JSONArray(response);


                                for(int i=0; i<array.length(); i++){
                                    JSONObject o = array.getJSONObject(i);

                                    ListItem item = new ListItem(
                                            o.getString("symbol"),
                                            o.getString("name"),
                                            o.getString("image"),
                                            o.getString("current_price"),
                                            o.getString("market_cap_rank"),
                                            o.getString("price_change_percentage_24h")

                                    );

                                    listItems.add(item);
                                }
                                adapter = new MyAdapter(listItems, getContext());
                                favRV.setAdapter(adapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(),"Error",Toast.LENGTH_LONG).show();

                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        }
}

