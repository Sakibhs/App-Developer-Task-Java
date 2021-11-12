package net.appcolors.babulandtask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MapActivity extends AppCompatActivity {
    TextView cityName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        cityName = findViewById(R.id.nameId);
        String name = getIntent().getExtras().getString("CityName");
        cityName.setText(name);
    }
}