package net.appcolors.babulandtask;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView cityName, weatherStatus, temperature;
    LinearLayout layout;
    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        cityName = itemView.findViewById(R.id.cityNameId);
        weatherStatus = itemView.findViewById(R.id.weatherStatusId);
        temperature = itemView.findViewById(R.id.temperatureId);
        layout = itemView.findViewById(R.id.linearLayoutId);
    }
}
