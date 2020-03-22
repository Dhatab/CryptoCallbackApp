package com.cryptocallback.cryptocallback.CoinDetailActivity;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.tapadoo.alerter.Alerter;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import at.markushi.ui.CircleButton;

import static com.cryptocallback.cryptocallback.OneSignalAlerts.MyApplication.getContext;


public class CoinDetailActivity extends AppCompatActivity {
    private CircleButton button_alert;
    private CircleButton button_fav;
    private ImageView coinIMG;
    private FirebaseAuth firebaseAuth;
    public static String coinSymbol;
    private String coinName, coinPrice, coinRank, coinTFHR, coinImage, direction,alertValue;
    private String onesignal_id;
    private String url = "https://api.cryptocallback.com/createalert/";
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;


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
        TextView rank = findViewById(R.id.rank);
        TextView tf_hour = findViewById(R.id.tf_hour);


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
        coinRank = intent.getStringExtra("Rank");
        coinTFHR = intent.getStringExtra("24Hour");
        coinImage = intent.getStringExtra("image");

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
        symbol.setText(coinSymbol.toUpperCase());
        price.setText("$" + coinPrice);
        rank.setText("Market Rank: " + coinRank);
        tf_hour.setText("24 HR Change: " + coinTFHR);

        Glide.with(getApplicationContext()).load(coinImage).into(coinIMG);


        //Button alert method
        checkifFavExist();
        OnAlertButtonClickListener();
    }

    //Tabs for fragment
    private void setupViewPager(ViewPager viewPager){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment_Graph1());
        adapter.addFragment(new Fragment_Graph2());
        adapter.addFragment(new Fragment_Graph3());
        viewPager.setAdapter(adapter);
    }


    //Check if favorites exist
    private void checkifFavExist() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(firebaseUser.getUid()).child("Favorites").child(coinName.toLowerCase());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    button_fav.setImageResource(R.drawable.ic_fav_filled);
                    button_fav.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid());
                            myRef.child("Favorites").child(coinName.toLowerCase()).removeValue();
                        }
                    });

                }else{
                    button_fav.setImageResource(R.drawable.ic_fav_notfilled);
                    button_fav.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid());
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("coin_name", coinName);
                                    myRef.child("Favorites").child(coinName.toLowerCase()).setValue(hashMap);

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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