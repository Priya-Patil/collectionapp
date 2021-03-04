package com.example.myapplication.notification.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.R;
import com.example.myapplication.notification.model.NotificationModel;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private static final String TAG = "CartAdapter";


    private Activity mContext;
    ArrayList<NotificationModel> list;

    private  ItemClickListener itemClickListener;

    public NotificationAdapter(Activity context, ArrayList<NotificationModel> list, ItemClickListener itemClickListener){
        mContext = context;
        this.list = list;
        this.itemClickListener=itemClickListener;
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }
    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, null);
        return new  MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, final int i) {
        final NotificationModel item = list.get(i);

        Log.e("vlist", list.toString());

       // viewHolder.tv_dateText.setGravity(Gravity.CENTER_VERTICAL);
        viewHolder.tv_Notificationaddress.setText(item.getAddtess());
        viewHolder.tv_Notificationdesc.setText(item.getDesc());
        viewHolder.tv_Notificationdate.setText(item.getDate());
        viewHolder.tv_NotificationTime.setText(item.getTime());
        viewHolder.tv_notificationTitle.setText(item.getTitile());
     //   viewHolder.img_historyRs.setBackground(mContext.getResources().getDrawable(item.getImage_drawable()));
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView tv_Notificationdesc,tv_Notificationdate,tv_NotificationTime,tv_notificationTitle,tv_Notificationaddress;
        ImageView img_historyRs;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_Notificationaddress = itemView.findViewById(R.id.tv_Notificationaddress);
            tv_Notificationdesc = itemView.findViewById(R.id.tv_Notificationdesc);
            tv_Notificationdate = itemView.findViewById(R.id.tv_Notificationdate);
            tv_NotificationTime = itemView.findViewById(R.id.tv_NotificationTime);
            tv_notificationTitle = itemView.findViewById(R.id.tv_notificationTitle);
            itemView.setOnClickListener(this); // bind the listener

        }
        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onClick(view, getAdapterPosition());

                   //  tv_randomColour.setText("2");
        }
    }

    //region Search Filter (setFilter Code)
    public void setFilter(ArrayList<NotificationModel> newList) {
        list = new ArrayList<>();
        list.addAll(newList);
        notifyDataSetChanged();
    }
}

