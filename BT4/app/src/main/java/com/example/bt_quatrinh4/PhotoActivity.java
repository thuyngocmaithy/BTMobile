package com.example.bt_quatrinh4;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {

    TextView txtName, txtDepartment, txtNormalize;
    ImageView imgPhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        txtName = findViewById(R.id.name_Photo);
        txtDepartment = findViewById(R.id.department_Photo);
        txtNormalize = findViewById(R.id.normalize_Photo);
        imgPhoto = findViewById(R.id.img_Photo);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String name = bundle.getString("Name_Photo");
            String department = bundle.getString("Department_Photo");
            String nomalize = bundle.getString("Normalized_Photo");
            String image = bundle.getString("image");

            txtName.setText(name);
            txtNormalize.setText(nomalize);
            txtDepartment.setText(department);
            if(isNetworkAvailable() == false){
                imgPhoto.setImageResource(R.drawable.img_loading);
            }else{
                if(image != null){
                    Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            final String changeUrl = image.replace("http:","https:");
                            picasso.load(changeUrl).error(R.drawable.img_error).placeholder(R.drawable.img_loading).into(imgPhoto);
                        }
                    }).build();

                    picasso.load(image).error(R.drawable.img_error).placeholder(R.drawable.img_loading).into(imgPhoto);

                }
            }

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
