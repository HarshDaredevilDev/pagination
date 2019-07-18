package com.example.com.zylatest;

import android.support.v7.widget.RecyclerView;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PageAdapter extends RecyclerView.Adapter<PageAdapter.MyViewHolder> {

    private List<Model> pinCodeList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView pinCode, name;

        public MyViewHolder(View view) {
            super(view);
            pinCode = (TextView) view.findViewById(R.id.pin_code);
            name = (TextView) view.findViewById(R.id.name);
           }
    }


    public PageAdapter(List<Model> pinCodeList) {
        this.pinCodeList = pinCodeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pincode_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Model movie = pinCodeList.get(position);
        holder.pinCode.setText(movie.getPinCode());
        holder.name.setText(movie.getName());
     }

    @Override
    public int getItemCount() {
        return pinCodeList.size();
    }
}
