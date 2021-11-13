package net.appcolors.babulandtask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<WeatherModel> models;
    Runnable runnable;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        recyclerView = findViewById(R.id.weatherRecyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        models = new ArrayList<>();
        runnable = null;
        handler = new Handler();
            String apiLink = "https://api.openweathermap.org/data/2.5/find?lat=23.68&lon=90.35&cnt=50&appid=e384f9ac095b2109c751d95296f8ea76";
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiLink, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                for (int i = 0; i < response.getJSONArray("list").length(); i++) {
                                    String city = response.getJSONArray("list").getJSONObject(i).getString("name");
                                    String temp = response.getJSONArray("list").getJSONObject(i).getJSONObject("main").getString("temp");
                                    String status = response.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main");
                                    int tempInDegree = (int) (Double.parseDouble(temp) - 273.15);
                                    String temperature = tempInDegree + "°" +"c";
                                    models.add(new WeatherModel(city, status, temperature));
                                    Log.d("City " + i, models.size()+"");
                                }

                            } catch (JSONException e) {
                                Toast.makeText(WeatherActivity.this, "1 " + e.getMessage(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(WeatherActivity.this, "2 " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    });

            requestQueue.add(request);

            runnable = new Runnable() {
                @Override
                public void run() {
                    if (models.size() < 50){
                        handler.postDelayed(this, 500);

                }
                    else {
                        RecyclerAdapter adapter = new RecyclerAdapter(WeatherActivity.this, models);
                        Log.d("ModelSize", models.size() + "");
                        recyclerView.setAdapter(adapter);
                    }
                }
            };
        handler.postDelayed(runnable, 5);
    }
}