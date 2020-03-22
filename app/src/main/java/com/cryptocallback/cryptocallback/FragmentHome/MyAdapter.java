package com.cryptocallback.cryptocallback.FragmentHome;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cryptocallback.cryptocallback.CoinDetailActivity.CoinDetailActivity;
import com.cryptocallback.cryptocallback.R;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by User on 4/7/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<ListItem> listItems;
    public Context context;
    private String coinPriceFormat, marketPercent;

    public MyAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ViewHolder(v);

    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ListItem listItem = listItems.get(position);

        holder.textViewHead.setText(listItem.getName());
        holder.textViewDesc.setText(listItem.getSymbol().toUpperCase());
        holder.textRank.setText("Rank: " +listItem.getMarket_cap_rank());

        double x = Double.parseDouble(listItem.getCurrent_price());
        if (x<1){
            coinPriceFormat = String.format("%.3f", x);
        }else {
            coinPriceFormat = String.format("%.2f", x);
        }
        holder.textViewPrice.setText("$" + coinPriceFormat);

        double y = Double.parseDouble(listItem.getMarket_cap_change_24h());
        if (y<1){
            marketPercent = String.format("%.3f", y);
            holder.textViewTFHour.setText(Html.fromHtml(" 24h Change: " +"<font color= #ff4444>" + marketPercent + "%"+ "</font>" ));
        }else {
            marketPercent = String.format("%.2f", y);
            holder.textViewTFHour.setText(Html.fromHtml(" 24h Change: " +"<font color= #009900>" + marketPercent + "%"+ "</font>"));
        }




        Glide.with(context).load(listItem.getImage()).into(holder.imageView1);

        //listen for item click then open new activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CoinDetailActivity.class);
                intent.putExtra("Coin Name", listItem.getName());
                intent.putExtra("Coin Sym", listItem.getSymbol());
                intent.putExtra("Price", listItem.getCurrent_price());
                intent.putExtra("Rank", listItem.getMarket_cap_rank());
                intent.putExtra("24Hour", marketPercent);
                intent.putExtra("image", listItem.getImage());
                context.startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void filterList(ArrayList<ListItem> filteredList){
        this.listItems = filteredList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewHead;
        public TextView textViewDesc;
        public TextView textViewPrice;
        public TextView textRank;
        public TextView textViewTFHour;
        public ImageView imageView1;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewHead = (TextView) itemView.findViewById(R.id.textViewHead);
            textViewDesc = (TextView) itemView.findViewById(R.id.textViewDesc);
            textViewPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
            textRank = (TextView) itemView.findViewById(R.id.textOneHour);
            textViewTFHour = (TextView) itemView.findViewById(R.id.textViewTFHour);
            imageView1 = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
