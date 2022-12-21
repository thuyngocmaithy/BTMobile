package com.example.bt_quatrinh4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import static android.Manifest.permission.CALL_PHONE;

public class OfficialActivity extends AppCompatActivity {

    TextView txtNormalized, txtName, txtParty, txtDepartment, txtAddress, txtPhone, txtEmail, txtWebsite;
    ImageView youtube, facebook, google, twitter, photoUrl;
    LinearLayout content;
    String currentPhoto = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        //khởi tạo các components
        txtNormalized = findViewById(R.id.normalizedInput);
        txtName = findViewById(R.id.name);
        txtParty = findViewById(R.id.party);
        txtDepartment = findViewById(R.id.department);
        txtAddress = findViewById(R.id.address);
        txtPhone = findViewById(R.id.phone);
        txtEmail = findViewById(R.id.email);
        txtWebsite = findViewById(R.id.website);

        youtube = findViewById(R.id.youtube);
        facebook = findViewById(R.id.facebook);
        google = findViewById(R.id.google);
        twitter = findViewById(R.id.twitter);
        photoUrl = findViewById(R.id.photoUrl);

        content = findViewById(R.id.content);

        txtNormalized.setText(MainActivity.topContent);

        //lấy giá trị index từ mainactivity
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            int index = bundle.getInt("index");
           try {
               //chuyển json => string
               JSONObject official = MainActivity.officials.getJSONObject(index);
               txtName.setText(official.getString("name"));


               if(isNetworkAvailable() == false) {
                   //không kết nối mạng được
                   photoUrl.setImageResource(R.drawable.img_loading);
               } else {
                   //kết nối thành công
                   if(official.has("photoUrl")){
                       String image = official.getString("photoUrl");
                       currentPhoto = image;
                       Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                           @Override
                           public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                               final String changeUrl = image.replace("http:","https:");
                               picasso.load(changeUrl).error(R.drawable.img_error).placeholder(R.drawable.img_loading).into(photoUrl);
                           }
                       }).build();

                       //dùng picasso load ảnh
                       picasso.load(image).error(R.drawable.img_error).placeholder(R.drawable.img_loading).into(photoUrl);

                   }else{
                       //nếu không có ảnh => mặc định
                       photoUrl.setImageResource(R.drawable.img_default);
                   }
               }

               if(official.has("address")){
                   JSONArray listAddress = official.getJSONArray("address");
                   String address = "";

                   String line1 = "",line2 = "",city = "",state = "",zip = "";

                   for(int i = 0; i < listAddress.length(); i++){
                       JSONObject currentAddress = listAddress.getJSONObject(i);
                       if(currentAddress.has("line1")){
                           line1 = currentAddress.getString("line1");
                       }
                       if(currentAddress.has("line2")){
                           line2 = currentAddress.getString("line2");
                       }
                       city = currentAddress.getString("city");
                       state = currentAddress.getString("state");
                       zip = currentAddress.getString("zip");

                       address = line1 + "," + line2 + "," + city + "," + state + "," + zip;



                   }
                   txtAddress.setText(address);


               }

               if(official.has("party")){
                       String party = official.getString("party");

                           if(party.toLowerCase(Locale.ROOT).indexOf("republican") != -1){
                               content.setBackgroundColor(Color.RED);
                           }else if(party.toLowerCase(Locale.ROOT).indexOf("democratic") != -1){
                               content.setBackgroundColor(Color.BLUE);
                           }else{
                               content.setBackgroundColor(Color.BLACK);
                           }
                       txtParty.setText("(" + party + ")");

                   }else{
                       content.setBackgroundColor(Color.BLACK);

                   }



                if(official.has("office")){
                    txtDepartment.setText(official.getString("office"));
                }

              if(official.has("phones")){
                  String phones = "";
                  JSONArray listPhone = official.getJSONArray("phones");
                  for(int i = 0; i < listPhone.length(); i++){
                      phones = listPhone.getString(i);



                  }
                  txtPhone.setText(phones);
              }

               if(official.has("emails")){
                   String emails = "";
                   JSONArray listEmail = official.getJSONArray("emails");
                       for(int i = 0; i < listEmail.length(); i++){
                           emails = listEmail.getString(i);

                       }

                   txtEmail.setText(emails);
               }

               if(official.has("urls")){
                   String urls = "";

                   JSONArray listUrl = official.getJSONArray("urls");
                   for(int i = 0; i < listUrl.length(); i++){
                       urls = listUrl.getString(i);

                   }

                   txtWebsite.setText(urls);
               }

               boolean isYoutube = false, isFacebook = false, isTwitter = false, isGoogle = false;
               if(official.has("channels")){
                   JSONArray listChannels = official.getJSONArray("channels");
                   for(int i = 0; i < listChannels.length(); i++){
                       JSONObject currentChanel = listChannels.getJSONObject(i);
                       String type = currentChanel.getString("type");
                       String id = currentChanel.getString("id");
                       if(type.toLowerCase(Locale.ROOT).indexOf("facebook") != -1){
                           facebook.setTag(id);
                           isFacebook = true;
                       }else if(type.toLowerCase(Locale.ROOT).indexOf("youtube") != -1){
                           youtube.setTag(id);
                           isYoutube = true;
                       }else if(type.toLowerCase(Locale.ROOT).indexOf("twitter") != -1){
                           twitter.setTag(id);
                           isTwitter = true;
                       }else if(type.toLowerCase(Locale.ROOT).indexOf("google") != -1){
                           google.setTag(id);
                           isGoogle = true;
                       }
                   }
               }

               if(!isFacebook){
                   facebook.setVisibility(View.GONE);
               }

               if(!isYoutube){
                   youtube.setVisibility(View.GONE);
               }

               if(!isTwitter){
                   twitter.setVisibility(View.GONE);
               }

               if(!isGoogle){
                   google.setVisibility(View.GONE);
               }



           }catch(JSONException e){
               Log.e("ErrorCatch",e.toString());
           }
        }

        //set sự kiện khi click vào photo
        photoUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new intent photoActivity
                Intent iGetPhoto = new Intent(getApplicationContext(),PhotoActivity.class);

                Bundle bGetPhotoInfo = new Bundle();
                bGetPhotoInfo.putString("Normalized_Photo", txtNormalized.getText().toString());
                bGetPhotoInfo.putString("Department_Photo", txtDepartment.getText().toString());
                bGetPhotoInfo.putString("Name_Photo", txtName.getText().toString());
                bGetPhotoInfo.putString("Party_Photo", txtParty.getText().toString());
                bGetPhotoInfo.putString("image",currentPhoto);

                iGetPhoto.putExtras(bGetPhotoInfo);
                startActivity(iGetPhoto);
            }
        });




        txtWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtWebsite.getText().toString().indexOf("No Data Provided") != -1){
                    return;
                }

                Intent goToWebsite = new Intent(Intent.ACTION_VIEW, Uri.parse(txtWebsite.getText().toString()));
                startActivity(goToWebsite);
            }
        });



        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FACEBOOK_URL = "https://www.facebook.com/" + facebook.getTag().toString();
                String urlToUse;
                PackageManager packageManager = getPackageManager();
                try {
                    int versionCode = packageManager.getPackageInfo("com.facebook.katana",
                            0).versionCode;
                    if (versionCode >= 3002850) {
                        urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                    } else {
                        urlToUse = "fb://page/" + facebook.getTag().toString();
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    urlToUse = FACEBOOK_URL;
                }
                Intent facebookIntent = new
                        Intent(Intent.ACTION_VIEW);
                facebookIntent.setData(Uri.parse(urlToUse));
                startActivity(facebookIntent);
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = twitter.getTag().toString();
                Intent twitterIntent;
                try{
                    getPackageManager().getPackageInfo("com.twitter.android",0);
                    twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
                    twitterIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }catch(Exception e){
                    Log.e("TwitterError",e.toString());
                    twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
                }
                startActivity(twitterIntent);
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = google.getTag().toString();
                try {
                    Intent googleIntent = new Intent(Intent.ACTION_VIEW);
                    googleIntent.setClassName("com.google.android.apps.plus", "com.google.android.apps.plus.phone.UrlGatewayActivity");
                    googleIntent.putExtra("customAppUri", name);
                    startActivity(googleIntent);
                }catch (ActivityNotFoundException e){
                    Log.e("GoogleError",e.toString());
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + name)));
                }
            }
        });

        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = google.getTag().toString();
                try {

                    Intent youtubeIntent = new Intent(Intent.ACTION_VIEW);
                    youtubeIntent.setPackage("com.google.android.youtube");
                    youtubeIntent.setData(Uri.parse("https://www.youtube.com/" + name)); startActivity(youtubeIntent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + name)));
                }
            }
        });

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}