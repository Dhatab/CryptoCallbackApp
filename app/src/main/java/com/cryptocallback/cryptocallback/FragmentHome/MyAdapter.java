package com.cryptocallback.cryptocallback.FragmentHome;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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

/**
 * Created by User on 4/7/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<ListItem> listItems;
    public Context context;
    private String coinPriceFormat;

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

        holder.textViewHead.setText(listItem.getHead());
        holder.textViewDesc.setText(listItem.getDesc());

        double x = Double.parseDouble(listItem.getPrice());
        if (x<1){
            coinPriceFormat = String.format("%.3f", x);
        }else {
            coinPriceFormat = String.format("%.2f", x);
        }
        holder.textViewPrice.setText("$" + coinPriceFormat);

        if (listItem.getOneHour().contains("-")) {
            holder.textOneHour.setText(Html.fromHtml(" 1h: " +"<font color= #ff4444>" +listItem.getOneHour() + "%"+ "</font>" ));
        } else {
            holder.textOneHour.setText(Html.fromHtml(" 1h: " +"<font color= #009900>" +listItem.getOneHour() + "%"+ "</font>"));
        }

        if (listItem.getTwentyfourHour().contains("-")) {
            holder.textViewTFHour.setText(Html.fromHtml(" 24h: " +"<font color= #ff4444>" +listItem.getTwentyfourHour() + "%"+ "</font>" ));
        } else {
            holder.textViewTFHour.setText(Html.fromHtml(" 24h: " +"<font color= #009900>" +listItem.getTwentyfourHour() + "%"+ "</font>"));
        }

        if (listItem.getSevendays().contains("-")) {
            holder.textSevenDay.setText(Html.fromHtml(" 7d: " +"<font color= #ff4444>" +listItem.getSevendays() + "%"+ "</font>" ));
        } else {
            holder.textSevenDay.setText(Html.fromHtml(" 7d: " +"<font color= #009900>" +listItem.getSevendays() + "%"+ "</font>"));
        }


       AssetManager assetManager = context.getAssets();
        try {
            InputStream ims = assetManager.open(listItem.getDesc().toLowerCase()+".jpg");
            Drawable d = Drawable.createFromStream(ims, null);
            holder.imageView1.setImageDrawable(d);
        } catch (IOException ex) {
            return;
        }

        /*Glide.with(context).load("https://api.cryptocallback.com/images/" + listItem.getDesc() + ".png")

                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageView1);*/

        //listen for item click then open new activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CoinDetailActivity.class);
                intent.putExtra("Coin Name", listItem.getHead());
                intent.putExtra("Coin Sym", listItem.getDesc());
                intent.putExtra("Price", listItem.getPrice());
                intent.putExtra("1 Hour", listItem.getOneHour());
                intent.putExtra("24 Hour", listItem.getTwentyfourHour());
                intent.putExtra("7 Days", listItem.getSevendays());
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
        public TextView textOneHour;
        public TextView textViewTFHour;
        public TextView textSevenDay;
        public ImageView imageView1;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewHead = (TextView) itemView.findViewById(R.id.textViewHead);
            textViewDesc = (TextView) itemView.findViewById(R.id.textViewDesc);
            textViewPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
            textOneHour = (TextView) itemView.findViewById(R.id.textOneHour);
            textViewTFHour = (TextView) itemView.findViewById(R.id.textViewTFHour);
            textSevenDay = (TextView) itemView.findViewById(R.id.textSevenDay);
            imageView1 = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
