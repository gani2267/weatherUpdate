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

public class dailyUpdateAdapter extends RecyclerView.Adapter<dailyUpdateAdapter.dailyViewHolder> {

    ArrayList<daily_update> arr;

    public dailyUpdateAdapter(ArrayList<daily_update> arr){
        this.arr = arr;
    }

    @NonNull
    @Override
    public dailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = li.inflate(R.layout.daily_update_item, parent, false);

        return new dailyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull dailyViewHolder holder, int position) {

        daily_update du = arr.get(position);
        holder.text_dl.setText(String.valueOf(du.getText_dl()));
        holder.status_dl.setText(String.valueOf(du.getStatus_dl()));
        holder.min_max_dl.setText(String.valueOf(du.getMin_max_dl()));

        if(du.getTxt_color_dl() == 1){
            holder.text_dl.setTextColor(Color.parseColor("#000000"));
            holder.status_dl.setTextColor(Color.parseColor("#000000"));
            holder.min_max_dl.setTextColor(Color.parseColor("#000000"));
        }else {
            holder.text_dl.setTextColor(Color.parseColor("#D7D5D5"));
            holder.status_dl.setTextColor(Color.parseColor("#D7D5D5"));
            holder.min_max_dl.setTextColor(Color.parseColor("#D7D5D5"));
        }

        String icon_dl_url = du.getIcon_dl();
        Picasso.get().load("https:"+icon_dl_url).into(holder.icon_dl);
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class dailyViewHolder extends RecyclerView.ViewHolder {
        TextView text_dl,status_dl,min_max_dl;
        ImageView icon_dl;

        public dailyViewHolder(@NonNull View itemView) {
            super(itemView);
            text_dl = itemView.findViewById(R.id.daily_text);
            status_dl = itemView.findViewById(R.id.daily_status);
            min_max_dl = itemView.findViewById(R.id.daily_min_max);
            icon_dl = itemView.findViewById(R.id.daily_icon);
        }
    }
}
