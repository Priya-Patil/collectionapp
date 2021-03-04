package com.example.myapplication.homeMenu.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.R;
import com.example.myapplication.engineer.PaymentCollectionActivity;
import com.example.myapplication.engineer.RouteSummaryActivity;
import com.example.myapplication.engineer.UpdateRouteActivity;
import com.example.myapplication.homeMenu.model.HomeMenuModel;
import com.example.myapplication.qrCode.QrCodeAddHome;
import com.example.myapplication.utilities.Utilities;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeMenuAdapter extends RecyclerView.Adapter<HomeMenuAdapter.MyViewHolder> {

    private static final String TAG = "CartAdapter";

    private Activity mContext;
    ArrayList<HomeMenuModel> list;
    private  ItemClickListener itemClickListener;

    public HomeMenuAdapter(Activity context, ArrayList<HomeMenuModel> list) {
        this.list = list;
        mContext = context;
    }

    public HomeMenuAdapter(Activity context, ArrayList<HomeMenuModel> list, ItemClickListener itemClickListener){
        mContext = context;
        this.list = list;
        this.itemClickListener=itemClickListener;
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    @Override

    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_menu, null);
        //  prefManager=new PrefManager(mContext);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, final int i) {
        final HomeMenuModel item = list.get(i);
        Log.e("vlist", list.toString());
      // viewHolder.tv_menuText.setText(item.getDisease());
        viewHolder.tv_menuText.setText(item.getName());
       // viewHolder.img_menuItem.setBackground(mContext.getResources().getDrawable(item.getImage_drawable()));

        viewHolder.img_menuItem.setImageDrawable(mContext.getResources().getDrawable(item.getImage_drawable()));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (i){
                    case 0:
                        Toast.makeText(mContext,"Coming soon", Toast.LENGTH_SHORT).show();
                        break;

                    case 1:
                        Utilities.launchActivity(mContext, RouteSummaryActivity.class,true);
                        break;

                    case 2:
                        Toast.makeText(mContext, "Coming Soon", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(mContext, "Coming Soon", Toast.LENGTH_SHORT).show();
                        break;

                    case 4:
                        Utilities.launchActivity(mContext, PaymentCollectionActivity.class,false);
                        break;

                    case 5:
                        Utilities.launchActivity(mContext, QrCodeAddHome.class,false);
                        break;
                    case 6:
                        Utilities.launchActivity(mContext, UpdateRouteActivity.class,false);
                        break;
                }
            }

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView img_menuItem;
        TextView tv_menuText;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_menuText = itemView.findViewById(R.id.tv_menuText);
            img_menuItem = itemView.findViewById(R.id.img_menuItem);
            itemView.setOnClickListener(this); // bind the listener
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onClick(view, getAdapterPosition());
        }
    }

    //region Search Filter (setFilter Code)
    public void setFilter(ArrayList<HomeMenuModel> newList) {
        list = new ArrayList<>();
        list.addAll(newList);
        notifyDataSetChanged();
    }
}

