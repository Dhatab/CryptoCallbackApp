package com.cryptocallback.cryptocallback.FragmentHome;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cryptocallback.cryptocallback.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by User on 4/12/2018.
 */

public class HomeFragment extends Fragment {

    public static final String URL_DATA = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false";
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    private RecyclerView recyclerView;
    private EditText SearchText;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        listItems = new ArrayList<>();
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        SearchText = (EditText) v.findViewById(R.id.search_text);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadRecyclerViewData();

        SearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        return v;
    }



    private void filter(String text) {
        List<ListItem> filteredList = new ArrayList<>();

        for (ListItem item : listItems) {
            if (item.getName().toLowerCase().contains(text.toLowerCase()) || item.getSymbol().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter = new MyAdapter(filteredList, getContext());
        recyclerView.setAdapter(adapter);
    }


    private void loadRecyclerViewData() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA,
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
                            recyclerView.setAdapter(adapter);

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