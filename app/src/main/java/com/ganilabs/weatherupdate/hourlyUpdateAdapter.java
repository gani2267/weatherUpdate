package com.ganilabs.weatherupdate;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class hourlyUpdateAdapter extends RecyclerView.Adapter<hourlyUpdateAdapter.hourlyViewHolder> {

    ArrayList<hourly_update> arr;

    public hourlyUpdateAdapter(ArrayList<hourly_update> arr){
        this.arr = arr;
    }

    @NonNull
    @Override
    public hourlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = li.inflate(R.layout.hourly_update_item, parent, false);

        return new hourlyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull hourlyViewHolder holder, int position) {

        hourly_update hu = arr.get(position);
        holder.time_hr_item.setText(String.valueOf(hu.getTime_hr()));
        holder.temp_hr_item.setText(String.valueOf(hu.getTemp_hr()+"°C"));

        if(hu.getText_color_hr() == 1){
            holder.temp_hr_item.setTextColor(Color.parseColor("#000000"));
            holder.time_hr_item.setTextColor(Color.parseColor("#000000"));
        }else{
            holder.temp_hr_item.setTextColor(Color.parseColor("#D7D5D5"));
            holder.time_hr_item.setTextColor(Color.parseColor("#D7D5D5"));
        }

        String icon_hr_url = hu.getIcon_hr();
        Picasso.get().load("https:"+icon_hr_url).into(holder.icon_hr_item);
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class hourlyViewHolder extends RecyclerView.ViewHolder {
        TextView time_hr_item,temp_hr_item;
        ImageView icon_hr_item;

        public hourlyViewHolder(@NonNull View itemView) {
            super(itemView);
            time_hr_item = itemView.findViewById(R.id.time_hr_item);
            icon_hr_item = itemView.findViewById(R.id.icon_hr_item);
            temp_hr_item = itemView.findViewById(R.id.temp_hr_item);
        }
    }
}
