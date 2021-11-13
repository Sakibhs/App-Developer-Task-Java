package net.appcolors.babulandtask;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private Context context;
    private List<WeatherModel> models;
    public RecyclerAdapter(Context context, List<WeatherModel> models){
        this.context = context;
        this.models = models;
    }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycleview_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Intent intent = new Intent(context, MapsActivity.class);
        Log.d("Loggg", "ok2");
            WeatherModel model = models.get(position);
             holder.cityName.setText(model.getCityName());
             holder.weatherStatus.setText(model.getStatus());
             holder.temperature.setText(model.getTemperature());
             holder.layout.setOnClickListener(view -> {
                 intent.putExtra("CityName", model.getCityName());
                 context.startActivity(intent);
             });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
