package com.example.myapplication.profile;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityProfileBinding;
import com.example.myapplication.home.model.StatusResponce;
import com.example.myapplication.profile.api.ProfileApi;
import com.example.myapplication.profile.model.ProfileImageUpdateResponse;
import com.example.myapplication.retrofit.RetrofitClientInstance;
import com.example.myapplication.utilities.PrefManager;
import com.example.myapplication.utilities.UriUtils;
import com.example.myapplication.utilities.Utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {


    public static String TAG = "ProfileActivity";
    ActivityProfileBinding binding;
    ProgressDialog progressDialog;
    Activity activity;


    public static final int RequestPermissionCode = 2;

    private int GALLERY = 1, CAMERA = 2;
    String imageType, profileGetImage ;
    public Bitmap bitmap;
    String selectedFilePath1;

    private int PICK_IMAGE_REQUEST = 1;
    private int REQUEST_CAMERA = 2;

    private int PICK_IMAGE_REQUEST2 = 1;
    private int REQUEST_CAMERA2 = 2;

    File path,path2;
    String fullpath;
    String fullpath2;
    Uri uri, uri2;
    ArrayList<String> a;


    int userid =0 ;

    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        activity = ProfileActivity.this;
        a = new ArrayList<>();
        prefManager = new PrefManager(activity);

        //accessToken = Utilities.getSavedUserData(activity,"accesstoken");

        EnableRuntimePermission();
        progressDialog = new ProgressDialog(activity);

        binding.ivEdit.setOnClickListener(this);
        binding.ivEdit2.setOnClickListener(this);



        try {
            Log.e(TAG,"userID "+ prefManager.getUserid());

            userid = Integer.parseInt("1");

        }catch (Exception e){}


        //getProfile(12);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.iv_edit:
                    ShowImageChooser();
                break;
  case R.id.iv_edit2:
                    ShowImageChooser2();
                break;
                case R.id.btn_search:
                    ShowImageChooser2();
                break;

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void ShowImageChooser() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[item].equals("Choose from Library")) {

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);

                } else if (items[item].equals("Cancel")) {

                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void ShowImageChooser2() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA2);

                } else if (items[item].equals("Choose from Library")) {

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(intent, PICK_IMAGE_REQUEST2);

                } else if (items[item].equals("Cancel")) {

                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //1 img //3 audio //4 video //5 document
        Bitmap thumbnail = null;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == PICK_IMAGE_REQUEST) {

                if (data.getData() != null) {
                    uri = data.getData();
                    Log.e(TAG, "uri " + uri);

                    //fullpath= getPath(uri);
                    fullpath = UriUtils.getPathFromUri(this, uri);

                    Log.e(TAG, "fullpath " + fullpath);
                    if (fullpath.endsWith("jpg") || fullpath.endsWith("jpeg") || fullpath.endsWith("png")) {
                        a.add(fullpath);
                    } else {
                       // Utilities.dialogMessage(activity,"Select only images");
                    }

                }

                binding.civProfile.setImageURI(uri);
              //  updateImage(userid,fullpath);

                //et_attach.setText("You selected total " + 1 + " Images");
            }


            if (requestCode == PICK_IMAGE_REQUEST2) {

                if (data.getData() != null) {
                    uri2 = data.getData();
                    Log.e(TAG, "uri " + uri2);

                    //fullpath= getPath(uri);
                    fullpath2 = UriUtils.getPathFromUri(this, uri2);

                    Log.e(TAG, "fullpath " + fullpath);
                    if (fullpath.endsWith("jpg") || fullpath.endsWith("jpeg") || fullpath.endsWith("png")) {
                        a.add(fullpath);
                    } else {
                        // Utilities.dialogMessage(activity,"Select only images");
                    }

                }

                binding.civProfile.setImageURI(uri2);
                //  updateImage(userid,fullpath);

                //et_attach.setText("You selected total " + 1 + " Images");
            }

            else if (requestCode == REQUEST_CAMERA) {

                Bitmap photo = null;

                if(!((Bitmap) data.getExtras().get("data") == null)) {
                    photo = (Bitmap) data.getExtras().get("data");
                }else
                {

                }
                 // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
               // Uri tempUri = getImageUri(getApplicationContext(), photo);
                uri = getImageUri(getApplicationContext(), photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(uri));

                //System.out.println(tempUri);
                Log.d(TAG, "pthth " + uri + " final " + finalFile);

                fullpath = String.valueOf(finalFile);
                binding.civProfile.setImageURI(uri);
              //  updateImage(userid,fullpath);

                //et_attach.setText(fullpath);;
                Log.d(TAG, "fullpath sss  " + fullpath);
            }

            else if (requestCode == REQUEST_CAMERA2) {

                Bitmap photo = null;

                if(!((Bitmap) data.getExtras().get("data") == null)) {
                    photo = (Bitmap) data.getExtras().get("data");
                }else
                {

                }
                 // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
               // Uri tempUri = getImageUri(getApplicationContext(), photo);
                uri2 = getImageUri2(getApplicationContext(), photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(uri2));

                //System.out.println(tempUri);
                Log.d(TAG, "pthth " + uri + " final " + finalFile);

                fullpath2 = String.valueOf(finalFile);
                binding.civProfile.setImageURI(uri2);
              //  updateImage(userid,fullpath);

                //et_attach.setText(fullpath);;
                Log.d(TAG, "fullpath sss  " + fullpath2);
            }

        }

        Log.e("onActivityResult:2 ", a.toString() + "full " + fullpath);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
  public Uri getImageUri2(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path2 = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path2);
    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Utilities.messageDialog(activity,"Storage permission allows us to Access Storage");
        } else {

            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            }, RequestPermissionCode);

        }
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;

    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }



    private void updateImage(String imgpath, String imgpath2, int userid) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Update in Process");
        progressDialog.show();
        progressDialog.setCancelable(false);

        if (Utilities.isNetworkAvailable(activity)){
            path = new File(imgpath);
            Log.e(TAG,"imgggg "+path+" "+userid+" uri "+uri);
            RequestBody requestFile =
                    RequestBody.create(
                            okhttp3.MediaType.parse(getContentResolver().getType(uri)),
                            path
                    );

            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("profile_image", path.getName(), requestFile);


         path2 = new File(imgpath2);
                    Log.e(TAG,"imgggg "+path2+" "+userid+" uri "+uri);
                    RequestBody requestFile2 =
                            RequestBody.create(
                                    okhttp3.MediaType.parse(getContentResolver().getType(uri)),
                                    path2
                            );

                    MultipartBody.Part body2 =
                            MultipartBody.Part.createFormData("profile_image", path2.getName(), requestFile2);




            RequestBody id = RequestBody.create(okhttp3.MediaType.parse("text/plain"),
                    String.valueOf(userid));

            ProfileApi profileApi = RetrofitClientInstance.getRetrofitInstanceServer().
                    create(ProfileApi.class);

            profileApi.addWasteCollectionimg(body,body2,id).
                    enqueue(new Callback<StatusResponce>() {
                        @Override
                        public void onResponse(Call<StatusResponce> call,
                                               Response<StatusResponce> response) {
                            StatusResponce profileImageUpdateResponse =  response.body();

                            Log.e( "onResponse: ", profileImageUpdateResponse.toString() );

                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<StatusResponce> call, Throwable t) {
                            Log.e(TAG,"ProfileImageUpdateResponse "+t.getMessage());
                            progressDialog.dismiss();
                        }
                    });
        } else {
            //Toast.makeText(activity, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
            Utilities.messageDialog(activity, getResources().getString(R.string.check_internet));

        }
    }


}