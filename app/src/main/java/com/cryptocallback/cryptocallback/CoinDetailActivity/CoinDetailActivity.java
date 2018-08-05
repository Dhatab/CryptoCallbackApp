package com.cryptocallback.cryptocallback.CoinDetailActivity;


import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.cryptocallback.cryptocallback.Graphs.Fragment_Graph1;
import com.cryptocallback.cryptocallback.Graphs.Fragment_Graph2;
import com.cryptocallback.cryptocallback.Graphs.Fragment_Graph3;
import com.cryptocallback.cryptocallback.R;
import com.cryptocallback.cryptocallback.FragmentFavs.favoritesRef;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.shawnlin.numberpicker.NumberPicker;
import com.tapadoo.alerter.Alerter;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import at.markushi.ui.CircleButton;

import static com.cryptocallback.cryptocallback.OneSignalAlerts.MyApplication.getContext;


public class CoinDetailActivity extends AppCompatActivity {
    private CircleButton button_alert;
    private CircleButton button_fav;
    private ImageView coinIMG, graphIMG;
    private FirebaseAuth firebaseAuth;
    public static String coinSymbol;
    private String coinName, coinPrice, coinOneHR, coinTFHR, coinSD,
            direction,alertValue, coinPriceFormat;
    private String onesignal_id;
    private String url = "https://api.cryptocallback.com/createalert/";
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_detail);


        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = (NonSwipeableViewPager) findViewById(R.id.graph_container);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(10);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Day");
        tabLayout.getTabAt(1).setText("Week");
        tabLayout.getTabAt(2).setText("Month");

        TextView header = findViewById(R.id.newtextViewHead);
        TextView symbol = findViewById(R.id.Symbol2);
        TextView price = findViewById(R.id.Price2);
        TextView price2 = findViewById(R.id.Hour2);
        TextView price3 = findViewById(R.id.TFHour2);
        TextView price4 = findViewById(R.id.Seven2);

        coinIMG = (ImageView) findViewById(R.id.imageView2);
        button_alert = (CircleButton) findViewById(R.id.button);
        button_fav = (CircleButton) findViewById(R.id.button_favorite);
        firebaseAuth = FirebaseAuth.getInstance();

        OneSignal.startInit(getContext()).init();
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        onesignal_id = status.getSubscriptionStatus().getUserId();


        //Calls for intent
        Intent intent = getIntent();


        //Intent carry overs
        coinName = intent.getStringExtra("Coin Name");
        coinSymbol = intent.getStringExtra("Coin Sym");
        coinPrice = intent.getStringExtra("Price");
        coinOneHR = intent.getStringExtra("1 Hour");
        coinTFHR = intent.getStringExtra("24 Hour");
        coinSD = intent.getStringExtra("7 Days");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_toolbar);
        TextView textView = (TextView) findViewById(R.id.title);
        Button back_button = (Button) findViewById(R.id.back_button);
        textView.setText(coinName);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        //header.setText(coinName);
        symbol.setText(coinSymbol);
        price.setText("$" + coinPrice);
        price2.setText("1 HR: " + coinOneHR + "% ");
        price3.setText("24 HR: " + coinTFHR+ "% ");
        price4.setText("7 Day: " + coinSD+ "% ");

        /*if (coinOneHR.contains("-")) {
            price2.setTextColor(Color.parseColor("#e32636"));
        } else {
            price2.setTextColor(Color.parseColor("#32cd32"));
        }

        if (coinTFHR.contains("-")) {
            price3.setTextColor(Color.parseColor("#e32636"));
        } else {
            price3.setTextColor(Color.parseColor("#32cd32"));
        }

        if (coinSD.contains("-")) {
            price4.setTextColor(Color.parseColor("#e32636"));
        } else {
            price4.setTextColor(Color.parseColor("#32cd32"));
        }

        AssetManager assetManager = getAssets();

        try {
            InputStream ims = assetManager.open(coinSymbol.toLowerCase()+".jpg");
            Drawable d = Drawable.createFromStream(ims, null);
            coinIMG.setImageDrawable(d);
        } catch (IOException ex) {
            return;
        }*/

        //Button alert method
        OnAlertButtonClickListener();
        FavsExistCheckAutoRefresh();
    }

    //Tabs for fragment
    private void setupViewPager(ViewPager viewPager){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment_Graph1());
        adapter.addFragment(new Fragment_Graph2());
        adapter.addFragment(new Fragment_Graph3());
        viewPager.setAdapter(adapter);
    }

    //onclick button to add to database
    public void OnFavsAddButtonClickListener() {
        button_fav.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addtoFavoritesDB();

                    }
                });
    }

    //onclick to remove from database
    public void OnFavsRemoveButtonClickListener() {
        button_fav.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removefromFavoritesDB();
                    }
                });
    }

    //method to add to database
    private void addtoFavoritesDB(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid());
        favoritesRef favoritesRef = new favoritesRef(coinName,coinSymbol);
        myRef.child("Favorites").child(coinName.toLowerCase()).setValue(favoritesRef);

    }

    //method to remove from database
    private void removefromFavoritesDB(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid());
        myRef.child("Favorites").child(coinName.toLowerCase()).removeValue();
    }

    //Check if favorites exist
    private void checkifFavExist() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(firebaseAuth.getCurrentUser().getUid());
        Query query = reference.child("Favorites").orderByChild("Coin_Name").equalTo(coinName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    button_fav.setImageResource(R.drawable.ic_fav_filled);
                    OnFavsRemoveButtonClickListener();

                }else{
                    button_fav.setImageResource(R.drawable.ic_fav_notfilled);
                    OnFavsAddButtonClickListener();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Check if favs exist then refreshes on click
    private void FavsExistCheckAutoRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkifFavExist();
                FavsExistCheckAutoRefresh();
            }
        }, 100);
    }

    //on alert click button
    public void OnAlertButtonClickListener() {
        button_alert.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        show();
                    }
                });

    }

    //shows alert number picker
    public void show() {

        final Dialog npDialog = new Dialog(CoinDetailActivity.this);
        npDialog.setTitle("Alert");
        npDialog.setContentView(R.layout.dialog);
        Button setBtn = (Button) npDialog.findViewById(R.id.npYesButton);
        Button cnlBtn = (Button) npDialog.findViewById(R.id.npCancelButton);
        final TextView alertText= (TextView) npDialog.findViewById(R.id.npText);

        /*double x = Double.parseDouble(coinPrice);
        final int coinIntPrice = (int) x;*/
        alertText.setText("");
        alertText.setHint("Current Price: $"+coinPrice);

        final EditText numberPicker = (EditText) npDialog.findViewById(R.id.numberPicker);
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberPicker.getText().toString().length() == 0 || numberPicker.getText().toString().equals(".")){
                    Toast.makeText(CoinDetailActivity.this, "Please Enter A Value or Cancel", Toast.LENGTH_SHORT).show();
                }else {
                    double x = Double.parseDouble(numberPicker.getText().toString());
                    double y = Double.parseDouble(coinPrice);
                    alertValue = Double.toString(x);
                    if (x > y) {
                        direction = "Above";
                        postCCB();
                        npDialog.dismiss();
                    } else if (x < y) {
                        direction = "Below";
                        postCCB();
                        npDialog.dismiss();
                    } else if (x == y) {
                        Toast.makeText(CoinDetailActivity.this, "Alert must be set below or above current price!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        /*numberPicker.setMaxValue(10000);
        numberPicker.setMinValue(0);
        numberPicker.setValue(coinIntPrice);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // TODO Auto-generated method stub

            }
        });
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                alertValue = String.valueOf(numberPicker.getValue());
                if (numberPicker.getValue() > coinIntPrice){
                    direction = "Above";
                    postCCB();
                    //Toast.makeText(CoinDetailActivity.this, "Alert For " +coinName + " Above Price of $" +alertValue + " Has Been Created!", Toast.LENGTH_SHORT).show();
                    npDialog.dismiss();
                }else if (numberPicker.getValue() < coinIntPrice){
                    direction = "Below";
                    postCCB();
                    // Toast.makeText(CoinDetailActivity.this, "Alert For " +coinName + " Below Price of $" +alertValue + " Has Been Created!", Toast.LENGTH_SHORT).show();
                   npDialog.dismiss();
                }else if (numberPicker.getValue() == coinIntPrice){
                    Toast.makeText(CoinDetailActivity.this, "Set Alert Below or Above Current Value!", Toast.LENGTH_SHORT).show();

                }

            }
        });*/

        cnlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                npDialog.dismiss();
            }
        });

        npDialog.show();
    }

    //post to CCB when create alert is clicked in number picker
    private void postCCB() {
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                final double xdouble = Double.parseDouble(alertValue);
                final String alertShow = String.format("%.2f",xdouble);
                Alerter.create(CoinDetailActivity.this)
                        .setTitle("Alert!")
                        .setText(coinName + " Price " + direction + " The Amount Of $" + alertShow + " Has Been Created!")
                        .setIcon(R.drawable.ic_notifications_active_black_24dp)
                        .setBackgroundColorRes(R.color.colorPrimary)
                        .setProgressColorInt(Color.WHITE)
                        .setDuration(2000)
                        .enableSwipeToDismiss()
                        .enableProgress(true)
                        .show();

                //Toast.makeText(CoinDetailActivity.this, response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            protected Map<String,String> getParams() {
                Map<String,String> MyData = new HashMap<String, String>();
                MyData.put("asset",coinName);
                MyData.put("metric","price");
                MyData.put("direction",direction);
                MyData.put("amount",alertValue);
                MyData.put("webpush",onesignal_id);

                return MyData;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        MyRequestQueue.add(MyStringRequest);
    }
}