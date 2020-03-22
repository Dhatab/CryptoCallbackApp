package com.cryptocallback.cryptocallback.FragmentAlert;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cryptocallback.cryptocallback.R;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

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

public class AlertFragment extends Fragment {

    private String URL_ALERT_DATA;
    private RecyclerView alertRecyclerView;
    private RecyclerView.Adapter alertAdapater;
    private List<AlertReference> AlertItem;
    private String onesignal_id;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alerts, container, false);

        AlertItem = new ArrayList<>();
        alertRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewAlerts);

        alertRecyclerView.setHasFixedSize(true);
        alertRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        OneSignal.startInit(getContext()).init();
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        onesignal_id = status.getSubscriptionStatus().getUserId();

        loadAlertJSON();

        return view;
    }

    public void loadAlertJSON() {
        URL_ALERT_DATA = "https://api.cryptocallback.com/onesignal/" + onesignal_id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_ALERT_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray mArray = new JSONArray(response);


                            for (int i = 0; i < mArray.length(); i++) {
                                JSONObject object = mArray.getJSONObject(i);
                                AlertReference alert_item = new AlertReference(
                                        object.getString("id"),
                                        object.getString("asset"),
                                        object.getString("metric"),
                                        object.getString("direction"),
                                        object.getString("amount")
                                );
                                AlertItem.add(alert_item);
                            }
                            alertAdapater = new AlertAdapter(AlertItem, getContext());
                            alertRecyclerView.setAdapter(alertAdapater);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Server Error", Toast.LENGTH_LONG).show();

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


}

