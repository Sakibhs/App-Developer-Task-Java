package net.appcolors.babulandtask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button gotoWeather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Hello Babuland
        textView = findViewById(R.id.testTextViewId);
        gotoWeather = findViewById(R.id.gotoWeatherButtonId);
        gotoWeather.setOnClickListener(view -> {
            startActivity(new Intent(this, WeatherActivity.class));
        });
        String apiLink = "https://api.openweathermap.org/data/2.5/find?lat=23.68&lon=90.35&cnt=50&appid=e384f9ac095b2109c751d95296f8ea76";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiLink, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String temperature = response.getString("message");
                            textView.setText(temperature);
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, "1 "+e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "2 "+error.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(request);

    }
}