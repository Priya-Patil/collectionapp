package com.example.myapplication.House.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.House.model.HomeResponce;

import java.util.ArrayList;

public class AddHouseAdapter extends RecyclerView.Adapter<AddHouseAdapter.MyViewHolder> {

    private static final String TAG = "AddHouseAdapter";


    private Activity mContext;
    ArrayList<HomeResponce> list;

    private  ItemClickListener itemClickListener;

    public AddHouseAdapter(Activity context, ArrayList<HomeResponce> list, ItemClickListener itemClickListener){
        mContext = context;
        this.list = list;
        this.itemClickListener=itemClickListener;
    }

 public AddHouseAdapter(Activity context, ArrayList<HomeResponce> list){
        mContext = context;
        this.list = list;
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }
    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addhouse, null);
        return new  MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, final int i) {
        final HomeResponce item = list.get(i);

        Log.e("vlist", list.toString());

       // viewHolder.tv_dateText.setGravity(Gravity.CENTER_VERTICAL);
        viewHolder.tv_addhouseNo.setText("घर क्रमांक: "+item.qrid);
        viewHolder.tv_addHouseDate.setText(item.cdate);
        viewHolder.tv_addHouseTime.setText(item.ctime);

     //   viewHolder.img_historyRs.setBackground(mContext.getResources().getDrawable(item.getImage_drawable()));
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView tv_addhouseNo,tv_addHouseDate,tv_addHouseTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_addhouseNo = itemView.findViewById(R.id.tv_addhouseNo);
            tv_addHouseDate = itemView.findViewById(R.id.tv_addHouseDate);
            tv_addHouseTime = itemView.findViewById(R.id.tv_addHouseTime);

            itemView.setOnClickListener(this); // bind the listener

        }
        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onClick(view, getAdapterPosition());

        }
    }
    //region Search Filter (setFilter Code)
    public void setFilter(ArrayList<HomeResponce> newList) {
        list = new ArrayList<>();
        list.addAll(newList);
        notifyDataSetChanged();
    }
}

