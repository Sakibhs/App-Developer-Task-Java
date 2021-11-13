package net.appcolors.babulandtask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.appcolors.babulandtask.databinding.ActivityMapsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.Permission;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    String cityName;
    double cityLatitude, cityLongitude;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng latLng;
    Runnable runnable;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        latLng = new LatLng(23.7104,90.4074);
        handler = new Handler();
        cityName = getIntent().getExtras().getString("CityName");
        binding.cityId.setText(cityName);
        cityLatitude = 0;
        cityLongitude = 0;
        setViews();
    }

    private void setViews(){
        String apiLink = "https://api.openweathermap.org/data/2.5/find?lat=23.68&lon=90.35&cnt=50&appid=e384f9ac095b2109c751d95296f8ea76";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiLink, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            for (int i = 0; i < response.getJSONArray("list").length(); i++) {
                                String city = response.getJSONArray("list").getJSONObject(i).getString("name");
                                if(city.equals(cityName)){
                                    String temp = response.getJSONArray("list").getJSONObject(i).getJSONObject("main").getString("temp");
                                    cityLatitude = response.getJSONArray("list").getJSONObject(i).getJSONObject("coord").getDouble("lat");
                                    cityLongitude = response.getJSONArray("list").getJSONObject(i).getJSONObject("coord").getDouble("lon");

                                    String status = response.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main");
                                    int tempInDegree = (int) (Double.parseDouble(temp) - 273.15);
                                    String temperature = tempInDegree + "°" +"c";
                                    String humidity = "Humidity: " + response.getJSONArray("list").getJSONObject(i).getJSONObject("main").getString("humidity");
                                    String windSpeed = "Wind Speed: "+ response.getJSONArray("list").getJSONObject(i).getJSONObject("wind").getString("speed");
                                    String maxTemp = response.getJSONArray("list").getJSONObject(i).getJSONObject("main").getString("temp_max");
                                    int maxTempInDegree = (int) (Double.parseDouble(maxTemp) - 273.15);
                                    String minTemp = response.getJSONArray("list").getJSONObject(i).getJSONObject("main").getString("temp_min");
                                    int minTempInDegree = (int) (Double.parseDouble(minTemp) - 273.15);
                                    binding.tempId.setText(temperature);
                                    binding.statusId.setText(status);
                                    binding.humidityId.setText(humidity);
                                    binding.windSpeedId.setText(windSpeed);
                                    binding.maxTempId.setText("Max. Temp.: "+maxTempInDegree+"°c");
                                    binding.minTempId.setText("Min. Temp.: "+minTempInDegree+"°c");
                                    break;
                                }
                            }

                        } catch (JSONException e) {
                            Toast.makeText(MapsActivity.this, "1 " + e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapsActivity.this, "2 " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(request);

        runnable = new Runnable() {
            @Override
            public void run() {
                if ((cityLatitude == 0) && (cityLongitude == 0)){
                    handler.postDelayed(this, 200);

                }
                else {
                    getLocationDetails(cityLatitude, cityLongitude);
                }
            }
        };
        handler.postDelayed(runnable, 5);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Location Initialization
        LatLng dhaka = new LatLng(23.7104,90.4074);
        mMap.addMarker(new MarkerOptions().position(dhaka).title("Dhaka"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dhaka));

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title("My Position"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        };
    }

    public void getLocationDetails(double latitude, double longitude){

     latLng = new LatLng(latitude, longitude);
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        String address = null;
        String city = null;
        String state = null;
        String country = null;
        String postalCode = null;
        String featureName = null;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude,1);
            city = addresses.get(0).getLocality();
          //  Log.d("FeatureName", city);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        mMap.addMarker(new MarkerOptions().position(latLng).title(city));
        float zoomLevel = 12.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
    }
}