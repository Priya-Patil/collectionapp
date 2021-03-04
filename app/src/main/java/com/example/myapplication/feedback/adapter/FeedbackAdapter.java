package com.example.myapplication.feedback.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.R;
import com.example.myapplication.feedback.model.FeedbackModel;

import java.util.ArrayList;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.MyViewHolder> {

    private static final String TAG = "CartAdapter";


    private Activity mContext;
    ArrayList<FeedbackModel> list;

    private  ItemClickListener itemClickListener;

    public FeedbackAdapter(Activity context, ArrayList<FeedbackModel> list, ItemClickListener itemClickListener){
        mContext = context;
        this.list = list;
        this.itemClickListener=itemClickListener;
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }
    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feedback, null);
        //  prefManager=new PrefManager(mContext);
        return new  MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, final int i) {
        final FeedbackModel item = list.get(i);

        Log.e("vlist", list.toString());

/*        viewHolder.tv_rsText.setVisibility(View.GONE);
        viewHolder.tv_dateText.setGravity(Gravity.CENTER_VERTICAL);
        //viewHolder.tv_rsText.setText(item.getAmount());*/
        viewHolder.radioButtonFeedback.setText(item.getAmount());
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        RadioButton radioButtonFeedback;


        public MyViewHolder(View itemView) {
            super(itemView);
            radioButtonFeedback = itemView.findViewById(R.id.radioButtonFeedback);
           /* tv_dateText = itemView.findViewById(R.id.tv_dateText);
            img_historyRs = itemView.findViewById(R.id.img_historyRs);*/
            itemView.setOnClickListener(this); // bind the listener

        }
        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onClick(view, getAdapterPosition());

                   //  tv_randomColour.setText("2");
        }
    }

    //region Search Filter (setFilter Code)
    public void setFilter(ArrayList<FeedbackModel> newList) {
        list = new ArrayList<>();
        list.addAll(newList);
        notifyDataSetChanged();
    }
}

