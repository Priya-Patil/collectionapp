package com.example.myapplication.complaint.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.complaint.model.ComplaintResponceModel;

import java.util.ArrayList;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.MyViewHolder> {

    private static final String TAG = "CartAdapter";


    private Activity mContext;
    ArrayList<ComplaintResponceModel> list;

    private  ItemClickListener itemClickListener;

    public ComplaintAdapter(Activity context, ArrayList<ComplaintResponceModel> list){
        mContext = context;
        this.list = list;
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }
    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wastecollection, null);
        return new  MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, final int i) {
        final ComplaintResponceModel item = list.get(i);

        Log.e("vlist", list.toString());

       // viewHolder.tv_dateText.setGravity(Gravity.CENTER_VERTICAL);
        viewHolder.tv_houseNo.setText("घर क्रमांक: "+item.home_id);
        viewHolder.tv_Collectdate.setText(item.cdate);
        viewHolder.tv_CollectTime.setText(item.ctime);

     //   viewHolder.img_historyRs.setBackground(mContext.getResources().getDrawable(item.getImage_drawable()));
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView tv_houseNo,tv_Collectdate,tv_CollectTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_houseNo = itemView.findViewById(R.id.tv_houseNo);
            tv_Collectdate = itemView.findViewById(R.id.tv_Collectdate);
            tv_CollectTime = itemView.findViewById(R.id.tv_CollectTime);

            itemView.setOnClickListener(this); // bind the listener

        }
        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onClick(view, getAdapterPosition());

                   //  tv_randomColour.setText("2");
        }
    }

    //region Search Filter (setFilter Code)
    public void setFilter(ArrayList<ComplaintResponceModel> newList) {
        list = new ArrayList<>();
        list.addAll(newList);
        notifyDataSetChanged();
    }
}

