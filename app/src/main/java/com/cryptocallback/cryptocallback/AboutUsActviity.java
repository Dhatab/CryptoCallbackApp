package com.cryptocallback.cryptocallback;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AboutUsActviity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_actviity);
        TextView aboutUS = (TextView) findViewById(R.id.about_us);
        TextView website = (TextView) findViewById(R.id.website_link);
        TextView donate = (TextView) findViewById(R.id.donate);
        String linkText = "Website: <a href='http://cryptocallback.com/'> http://cryptocallback.com/ </a>";
        String linkText2 = "Donate: <a href='http://cryptocallback.com/donate'> http://cryptocallback.com/donate </a>";

        aboutUS.setText("Our team consists of two engineers with a taste for innovation and passion for community. The cryptocurrency market first caught our attention at the end of 2013 when Bitcoin had its first huge bull run. However, it was not until Ethereum had its first big run-up in March when we started to realize the massive potential of blockchain technology and decided it was time to get involved.\n" +
                "\n" +
                "As we dove into the industry, it became apparent that there was a huge unmet need when it came to tools and resources. For the tools and resources that did exist, they either lacked features and functionality or offered a horrible user experience. As our frustration continued to grow, we decided it was time to step in and create solutions, for the community and ourselves, instead of sitting back and watching.\n" +
                "\n" +
                "We now dedicate our time to the development of tools and resources that are both functional and intuitive. Our vision for this project has dramatically evolved since conception and will continue to do so over time. We are excited for what the future has in store and look forward to your support along the way!\n" +
                "\n" +
                "\n" +
                "Sincerely,\n" +
                "\n" +
                "CryptoCallback Team\n" +
                "\n" +
                "\n" +
                "P.S. If you agree with our mission and like what we're doing here, consider donating to help support further development!");

        website.setText(Html.fromHtml(linkText));
        website.setMovementMethod(LinkMovementMethod.getInstance());
        donate.setText(Html.fromHtml(linkText2));
        donate.setMovementMethod(LinkMovementMethod.getInstance());


    }
}