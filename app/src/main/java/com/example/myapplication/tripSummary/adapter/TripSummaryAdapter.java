package com.example.myapplication.tripSummary.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.tripSummary.model.TripSummaryModel;
import com.example.myapplication.wasteCollection.model.WasteCollectionModel;

import java.util.ArrayList;

public class TripSummaryAdapter extends RecyclerView.Adapter<TripSummaryAdapter.MyViewHolder> {

    private static final String TAG = "TripSummaryAdapter";

    private Activity mContext;
    ArrayList<TripSummaryModel> list;

    private  ItemClickListener itemClickListener;

    public TripSummaryAdapter(Activity context, ArrayList<TripSummaryModel> list){
        mContext = context;
        this.list = list;
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }
    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tripsummary, null);
        return new  MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, final int i) {
        final TripSummaryModel item = list.get(i);

        Log.e("vlist", list.toString());

       // viewHolder.tv_dateText.setGravity(Gravity.CENTER_VERTICAL);
        viewHolder.tv_YardNo.setText("Dump yard : "+item.title);
        viewHolder.tv_TripSummrydate.setText(item.cdate);
        viewHolder.tv_tripSummuryTime.setText(item.ctime);

     //   viewHolder.img_historyRs.setBackground(mContext.getResources().getDrawable(item.getImage_drawable()));
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView tv_YardNo,tv_TripSummrydate,tv_tripSummuryTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_YardNo = itemView.findViewById(R.id.tv_YardNo);
            tv_TripSummrydate = itemView.findViewById(R.id.tv_TripSummrydate);
            tv_tripSummuryTime = itemView.findViewById(R.id.tv_tripSummuryTime);

            itemView.setOnClickListener(this); // bind the listener

        }
        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onClick(view, getAdapterPosition());

                   //  tv_randomColour.setText("2");
        }
    }

    //region Search Filter (setFilter Code)
    public void setFilter(ArrayList<TripSummaryModel> newList) {
        list = new ArrayList<>();
        list.addAll(newList);
        notifyDataSetChanged();
    }
}

