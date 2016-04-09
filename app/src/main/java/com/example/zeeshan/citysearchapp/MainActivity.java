package com.example.zeeshan.citysearchapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static EditText enterCity;
    Button search;
    ListView cityList;

    public static String city;
    public static String JSON_url;
    public String city2 = "";

    public ArrayList<String> cities = new ArrayList<String>();
    public ArrayList<String> citiesId = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Context context = getApplicationContext();
        final InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        final ProgressDialog pDialog = new ProgressDialog(this);

        enterCity = (EditText) findViewById(R.id.enter_city);

        cityList = (ListView)findViewById(R.id.city_list);
        cityList.setDivider(new ColorDrawable(0x99B2B2B2));   //0xAARRGGBB
        cityList.setDividerHeight(1);

        Intent i2 = getIntent();
        Bundle b2 =  i2.getExtras();
        if(b2!=null) {
            city2 = (String) b2.get("city2");
            Log.v("city2", city2);
            enterCity.setText(city2);
            //inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            URLreponse(context, inputManager, pDialog);
            enterCity.setText(city2);
            enterCity.setSelection(enterCity.getText().length());
        }

        search = (Button) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pDialog.setMessage("Loading...");
                pDialog.show();
                URLreponse(context, inputManager, pDialog);
                enterCity.setText(city);
                enterCity.setSelection(enterCity.getText().length());

            }
        });



    }

    public void URLreponse(final Context context, InputMethodManager inputManager, final ProgressDialog pDialog) {

        city = enterCity.getText().toString();
        cities.clear();
        citiesId.clear();



        if (city.length() <= 2) {
            Toast msg = Toast.makeText(context, "City cannot have less than 3 letters!", Toast.LENGTH_LONG);
            msg.show();
        } else {

            if(city2.equals("")) {
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            if (!isInternetOn(getApplicationContext())) {

            }
            JSON_url = "http://test.maheshwari.org/services/testwebservice.asmx/SuggestCity?tryValue=";
            JSON_url = JSON_url + city;
            JsonArrayRequest request = new JsonArrayRequest(JSON_url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    pDialog.hide();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject temp = response.getJSONObject(i);
                            String cityName = temp.getString("Title");
                            String cityId = temp.getString("Id");
                            cities.add(cityName);
                            citiesId.add(cityId);

                            Log.v("cityName", cityName);
                            Log.v("cityId", cityId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    cityList.setAdapter(new CustomAdapter(context, cities, citiesId));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    //enterCity.setText("");
                }
            });
            AppController.getInstance().addToRequestQueue(request);
            //enterCity.setText("");

        }
    }


    public final boolean isInternetOn(Context context) {

        // get Connectivity Manager object to check connection
        ConnectivityManager connection = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        // Check for network connections
        if ( connection.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connection.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connection.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connection.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet
            //Toast msg1 = Toast.makeText(context, " Connected ", Toast.LENGTH_LONG);
            //msg1.show();
            return true;


        } else if ( connection.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED || connection.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
            Toast msg2 = Toast.makeText(context, "No available connection!", Toast.LENGTH_LONG);
            msg2.show();
            return false;
        }

        return false;
    }

}

class CustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> list;
    ArrayList<String> listId;
    private static LayoutInflater inflater = null;

    public CustomAdapter(Context context_here, ArrayList<String> cities, ArrayList<String>citiesId) {
        context = context_here;
        list = cities;
        listId = citiesId;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView mycity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.row_list, null);

        holder.mycity = (TextView) rowView.findViewById(R.id.mycity);
        holder.mycity.setText(list.get(position));

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast msg = Toast.makeText(context, "You Clicked: "+listId.get(position), Toast.LENGTH_LONG);
                //msg.show();
                Intent i1 = new Intent(context, CityDetail.class);
                i1.putExtra("cityId", listId.get(position));
                //context.startActivity(i1);
                i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i1);

            }
        });
        return rowView;
    }

}
