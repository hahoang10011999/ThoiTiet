package com.example.thoitiet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thoitiet.Contexts.ContectThoiTiet;
import com.example.thoitiet.R;

import java.util.List;

public class AdapterThoiTiet extends RecyclerView.Adapter<AdapterThoiTiet.ViewHolder> {
    Context context;
    List<ContectThoiTiet> contectThoiTiets;
    public AdapterThoiTiet(List<ContectThoiTiet> contects) {
        this.contectThoiTiets = contects;
    }
    @NonNull
    @Override
    public AdapterThoiTiet.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =LayoutInflater.from(parent.getContext());
        View view =layoutInflater.inflate(R.layout.item,parent,false);
        AdapterThoiTiet.ViewHolder viewHolder = new ViewHolder((view));
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterThoiTiet.ViewHolder holder, int position) {
        final ContectThoiTiet thoiTiet = contectThoiTiets.get(position);
        holder.main.setText(thoiTiet.getMain());
        holder.max.setText(thoiTiet.getMax());
        holder.min.setText(thoiTiet.getMin());
        holder.day.setText(thoiTiet.getDay());
    }

    @Override
    public int getItemCount() {
        return contectThoiTiets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView main,min,max,day;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            main = itemView.findViewById(R.id.main);
            min = itemView.findViewById(R.id.min);
            max = itemView.findViewById(R.id.max);
            day = itemView.findViewById(R.id.day);
        }
    }
}
