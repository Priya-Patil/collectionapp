package com.example.myapplication.engineer.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.engineer.model.WasteTypeModel;

import java.util.ArrayList;

public class WasteTypeAdapter extends RecyclerView.Adapter<WasteTypeAdapter.MyViewHolder> {

    private static final String TAG = "CartAdapter";

    private Activity mContext;
    ArrayList<WasteTypeModel> list;

    private  ItemClickListener itemClickListener;
    private RadioButton lastCheckedRB = null;


    public WasteTypeAdapter(Activity context, ArrayList<WasteTypeModel> list, ItemClickListener itemClickListener){
        mContext = context;
        this.list = list;
        this.itemClickListener=itemClickListener;
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wastetype, null);
        return new  MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, final int i) {
        final WasteTypeModel item = list.get(i);

        Log.e("vlist", list.toString());
        viewHolder.tv_title.setBackground(mContext.getResources().getDrawable(item.getImage()));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.rb_item.setChecked(true);
            }
        });




       /* viewHolder.radiogrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checked_rb = (RadioButton) group.findViewById(checkedId);
                if (lastCheckedRB != null) {
                    lastCheckedRB.setChecked(false);
                }
                //store the clicked radiobutton
                lastCheckedRB = checked_rb;
            }
        });*/
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        RadioButton rb_item;
        ImageView tv_title;
        RadioGroup radiogrp;

        public MyViewHolder(View itemView) {
            super(itemView);
            rb_item = itemView.findViewById(R.id.rb_item);
            tv_title = itemView.findViewById(R.id.tv_title);
            radiogrp = itemView.findViewById(R.id.radiogrp);
            itemView.setOnClickListener(this); // bind the listener

        }
        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onClick(view, getAdapterPosition());

                   //  tv_randomColour.setText("2");
        }
    }

    //region Search Filter (setFilter Code)
    public void setFilter(ArrayList<WasteTypeModel> newList) {
        list = new ArrayList<>();
        list.addAll(newList);
        notifyDataSetChanged();
    }
}

