package com.example.myapplication.homeMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.feedback.FeedbackActivity;
import com.example.myapplication.homeMenu.adapter.HomeMenuAdapter;
import com.example.myapplication.homeMenu.model.HomeMenuModel;
import com.example.myapplication.notification.NotificationActivity;
import com.example.myapplication.utilities.Utilities;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Activity activity;

    ImageView img_openDrawer;
    private DrawerLayout mDrlOpener;

    private int[] homeMenuImage = new int[]{R.drawable.ic_car, R.drawable.ic_touch,R.drawable.ic_car,
            R.drawable.ic_clock,R.drawable.ic_payment, R.drawable.ic_person};

    private String[] homeMenuText = new String[]{"सदस्यता","मागणी वर उचलला","विशेष संकलन","विनंती","देय","खाते"};

    HomeMenuAdapter adapter;
 ArrayList<HomeMenuModel> modelArrayListMenu ;
RecyclerView rv_menu;
RelativeLayout rl_notification,rl_Feedback,rl_payments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        activity=MainActivity.this;

        bindViews( );
        btnListerner();
        setHomeMenu();

    }

    void bindViews()
    {
        img_openDrawer = findViewById(R.id.img_openDrawer);
        mDrlOpener = findViewById(R.id.drl_Opener);
        rv_menu = findViewById(R.id.rv_menu);
        rl_notification=findViewById(R.id.rl_notification);
        rl_Feedback=findViewById(R.id.rl_Feedback);
        rl_payments=findViewById(R.id.rl_Payment);


    }
    void btnListerner()
    {
        img_openDrawer.setOnClickListener(this) ;
        rl_Feedback.setOnClickListener(this);
        rl_notification.setOnClickListener(this);
        rl_payments.setOnClickListener(this);

    }


        void setHomeMenu()
    {
        modelArrayListMenu = arrayMenu();

        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        //  LinearLayout glm = new LinearLayout(getContext());
        //  rv_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
       rv_menu.setItemAnimator(new DefaultItemAnimator());
       rv_menu.setHasFixedSize(true);
       rv_menu.setLayoutManager(layoutManager);
        // recyclerView.setAdapter(adapter);


       /* adapter = new HomeMenuAdapter(this, modelArrayListMenu,
                new HomeMenuAdapter.ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        Toast.makeText(getApplicationContext(), "Position " + position, Toast.LENGTH_SHORT).show();

                    }
                });*/
       rv_menu.setAdapter(adapter);
       // progressDialog.dismiss();
    }

    private ArrayList<HomeMenuModel> arrayMenu(){
        ArrayList<HomeMenuModel> list = new ArrayList<>();
        for(int i = 0; i < 6; i++){
            HomeMenuModel homeMenuModel = new HomeMenuModel();
            homeMenuModel.setName(homeMenuText[i]);
            homeMenuModel.setImage_drawable(homeMenuImage[i]);
            list.add(homeMenuModel);
        }
        return list;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {



            case R.id.img_openDrawer:
                mDrlOpener.openDrawer(Gravity.LEFT); //Edit Gravity.START need API 14
                break;


            case R.id.rl_Feedback:
                Utilities.launchActivity(activity, FeedbackActivity.class,false);
                break;

            case R.id.rl_notification:
                Utilities.launchActivity(activity, NotificationActivity.class,false);
                break;
            case R.id.rl_Payment:
               // Utilities.launchActivity(activity, PaymentActivity.class,false);
                break;
        }

    }
}