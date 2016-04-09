package com.example.zeeshan.citysearchapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class CityDetail extends AppCompatActivity {

    EditText enterCity2;
    Button search2;
    TextView cityDetail;

    public static String JSON_url;

    public String CityCode, CityName, StateCode, StateName, CountryCode, CountryName, IsdCode, GPlaceId, Latitude, Longitude, ActiveStatus;
    public String finalDetail = "";

    public static String city2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Context context = getApplicationContext();

        enterCity2 = (EditText)findViewById(R.id.enter_city2);
        cityDetail = (TextView)findViewById(R.id.city_detail);
        search2 = (Button)findViewById(R.id.search2);
        search2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                city2 = enterCity2.getText().toString();
                if (city2.length() <= 2) {
                    Toast msg = Toast.makeText(context, "City cannot have less than 3 letters!", Toast.LENGTH_LONG);
                    msg.show();
                } else {
                    Intent i2 = new Intent(context, MainActivity.class);
                    i2.putExtra("city2", city2);
                    i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i2);
                }

            }
        });

        Intent i1 = getIntent();
        Bundle b1 =  i1.getExtras();
        String cityId = (String) b1.get("cityId");
        Log.v("NextPage cityId", cityId);

        JSON_url = "http://test.maheshwari.org/services/testwebservice.asmx/GetCity?cityId=";
        JSON_url = JSON_url + cityId;

        JsonObjectRequest request = new JsonObjectRequest(JSON_url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String detail = response.toString();
                Log.v("detail", detail);
                //cityDetail.setText(detail);

                    try {
                        JSONObject obj1 = response;
                        CityCode = obj1.getString("CityCode");
                        CityName = obj1.getString("CityName");

                        JSONObject State = obj1.getJSONObject("State");
                        StateCode = State.getString("StateCode");
                        StateName = State.getString("StateName");
                        CountryCode = State.getString("CountryCode");
                        Log.v("StateCode", StateCode);

                        JSONObject Country = obj1.getJSONObject("Country");
                        CountryName = Country.getString("CountryName");
                        IsdCode = Country.getString("IsdCode");

                        GPlaceId = obj1.getString("GPlaceId");
                        Latitude = obj1.getString("Latitude");
                        Longitude = obj1.getString("Longitude");
                        ActiveStatus = obj1.getString("ActiveStatus");

                        finalDetail = "City Code = "+CityCode+"\n"+
                                "City Name = "+CityName+"\n" +
                                "State Code = "+StateCode+"\n" +
                                "State Name = "+StateName+"\n" +
                                "Country Code = "+CountryCode+"\n" +
                                "Country Name = "+CountryName+"\n" +
                                "ISD Code = "+IsdCode+"\n" +
                                "GPlace Id = "+GPlaceId+"\n" +
                                "Latitude = "+Latitude+"°\n" +
                                "Longitude = "+Longitude+"°\n" +
                                "Active Status = "+ActiveStatus;

                        cityDetail.setText(finalDetail);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        AppController.getInstance().addToRequestQueue(request);

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
