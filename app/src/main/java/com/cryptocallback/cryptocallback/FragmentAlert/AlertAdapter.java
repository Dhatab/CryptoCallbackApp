package com.cryptocallback.cryptocallback.FragmentAlert;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cryptocallback.cryptocallback.MainHomeActivity;
import com.cryptocallback.cryptocallback.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by User on 4/16/2018.
 */

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.ViewHolder>  {
    private List<AlertReference> AlertItem;
    public Context context;
    public String unique_id;
    private String url = "https://api.cryptocallback.com/deletealert/";
    MainHomeActivity alertFragment = new MainHomeActivity();

    public AlertAdapter(List<AlertReference> AlertItem, Context context) {
        this.AlertItem = AlertItem;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alert_list_item, parent, false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final AlertReference alertItemHolder = AlertItem.get(position);

        unique_id = alertItemHolder.getCoin_onesignal_id();
        holder.text_AlertCoin.setText("Alert for " + alertItemHolder.getCoin_asset() + " set for "+ alertItemHolder.getCoin_metric() + " "+ alertItemHolder.getCoin_direction() + " $"+alertItemHolder.getCoin_amount());
        // holder.text_AlertMetric.setText(alertItemHolder.getCoin_metric());
        // holder.text_AlertDirection.setText(alertItemHolder.getCoin_direction());
        //holder.text_AlertAmount.setText(alertItemHolder.getCoin_amount());



        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Would You Like To Delete This Alert?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteAlert();
                                removeItem(alertItemHolder);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog

                            }
                        });
                builder.create().show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return AlertItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text_AlertCoin;
        //public TextView text_AlertMetric;
        //public TextView text_AlertDirection;
        //public TextView text_AlertAmount;
        public ImageView image_AlertCoin;
        public RelativeLayout relativeLayout;

        public ViewHolder(View alertItemView) {
            super(alertItemView);

            text_AlertCoin = (TextView) alertItemView.findViewById(R.id.TV_CoinAsset);
            //text_AlertMetric = (TextView) alertItemView.findViewById(R.id.TV_CoinMetric);
            //text_AlertDirection = (TextView) alertItemView.findViewById(R.id.TV_CoinDirection);
            //text_AlertAmount = (TextView) alertItemView.findViewById(R.id.TV_CoinPrice);
            image_AlertCoin = (ImageView) alertItemView.findViewById(R.id.image_CoinAlert);
            relativeLayout = (RelativeLayout) alertItemView.findViewById(R.id.alert_layout);
        }
    }

    private void deleteAlert() {
        StringRequest deleterequest = new StringRequest(Request.Method.DELETE, url + unique_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Toast.makeText(context, "Alert Successfully Delete", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(deleterequest);
    }

    private void removeItem(AlertReference alertReference){
        int currPosition = AlertItem.indexOf(alertReference);
        AlertItem.remove(currPosition);
        notifyItemRemoved(currPosition);
    }

}
