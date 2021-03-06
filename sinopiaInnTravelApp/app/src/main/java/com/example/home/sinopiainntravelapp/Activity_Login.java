package com.example.home.sinopiainntravelapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Activity_Login extends AppCompatActivity {

    EditText token;
    Button confirm;
    ImageView backgrounImage;
    ScaleBitMaps bitmapClass ;
    public static final String PREFS_NAME = "checkinToken";
    FrameLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        relativeLayout = (FrameLayout) findViewById(R.id.frameLayout);

        token = (EditText) findViewById(R.id.amenities);

        confirm = (Button) findViewById(R.id.loginbutton);

        backgrounImage = (ImageView) findViewById(R.id.backgrounImage);


        backgrounImage.setImageResource(R.drawable.logo);

        confirm.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {


                String WEB_SERVICE_URL = "http://www.sinopiainn.com/api/login";

                AsyncHttpClient client = new AsyncHttpClient();

                client.setConnectTimeout(20000);

                client.get(WEB_SERVICE_URL, new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        // called before request is started
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray json) {

                        String guestName = null;

                        Boolean guest = false;

                        for (int l = 0; l < json.length(); l++) {


                            JSONObject booking = null;



                            try {

                                booking = (JSONObject) json.get(l);

                                if (booking.getString("token").equals( String.valueOf(token.getText()))) {


                                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("token",token.getText().toString());
                                    editor.putString("fname",booking.getString("fname"));
                                    editor.putString("lname",booking.getString("lname"));
                                    editor.putString("email",booking.getString("email"));
                                    editor.putString("fromdate",booking.getString("fromdate"));
                                    editor.putString("todate",booking.getString("todate"));
                                    editor.putString("reservationID",booking.getString("_id"));
                                    editor.commit();

                                    /*Intent intent = new Intent(getBaseContext(), Activity_CheckIn.class);
                                    startActivity(intent);

                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);*/

                                    Intent mServiceIntent = new Intent(getBaseContext(),Timeline_Service.class);

                                    mServiceIntent.putExtra("email",booking.getString("email"));

                                    startService(mServiceIntent);



                                    break;


                                }


                            } catch (JSONException e) {

                                e.printStackTrace();


                            }


                        }




                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject e)  {


                        Snackbar snackbar = Snackbar
                                .make(relativeLayout, "Checking Reservation Details", Snackbar.LENGTH_LONG);

                        snackbar.show();

                    }
                    @Override
                    public void onRetry(int retryNo) {

                    }
                });



            }

        });


    }
    @Override
    protected void onStop() {
        super.onStop();

        finish();
    }


}










