package com.example.bt_quatrinh4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText zipCode;
    Button btnSearch;
    ListView listView;
    ArrayList<CivicDTO> lists = new ArrayList<>();
    TextView normalizedInput;
    public static JSONArray officials = new JSONArray();
    JSONArray offices = new JSONArray();
    public static String topContent = "";
    CivicListAdapter adapter;
    String defaultSearch = "60601";
    String API_KEY = "AIzaSyCs2hlLrc4uYR5_bcAqEiJDWcARred4Qjw";
    String MAIN_URL = "https://www.googleapis.com/civicinfo/v2/representatives?key=" + API_KEY + "&address=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isNetworkAvailable() == false){
            //Nếu không kết nối được => setcontent no internet
            setContentView(R.layout.main_no_internet);
        }else{
            //Kết nối được
            // set content activity main
            setContentView(R.layout.activity_main);

            //Khởi tạo các components
            listView = findViewById(R.id.listView);
            normalizedInput = findViewById(R.id.normalizedInput);

            adapter = new CivicListAdapter(this, lists);
            //set adapter cho list view
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            //Mặc định url
            String url = MAIN_URL + defaultSearch;
            //Tìm url
            handleSearch(url);
        }


    }

    //tạo menu about và search
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater im = getMenuInflater();
        im.inflate(R.menu.option_menu, menu);
        return true;
    }

    //Search URL
    public void handleSearch(String url){
        //Clear lists CivicDTO cũ
        lists.clear();
        //Tạo request và respone để lấy dữ liệu từ file json về
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject result = new JSONObject(response);
                            //get data json ngoài cùng
                            officials = result.getJSONArray("officials");
                            offices = result.getJSONArray("offices");
                            //Duyệt qua mảng json để lấy các đối tượng bên trong mảng
                            for(int i = 0; i < officials.length(); i++){
                                JSONObject official = officials.getJSONObject(i);
                                String officialName = official.getString("name");
                                String officeName = "Not found";
                                for(int j = 0; j < offices.length(); j++){
                                    JSONObject office = offices.getJSONObject(j);
                                    JSONArray officialIndices = office.getJSONArray("officialIndices");
                                    for(int k = 0; k < officialIndices.length(); k++){
                                        if(officialIndices.getInt(k) == i){
                                            officeName = office.getString("name");
                                        }
                                    }
                                }
                                if(official.has("party")){
                                    String partyDB = official.getString("party");
                                    String party = partyDB.split("\\s")[0];
                                    officialName = officialName + " (" + party + ")";
                                }

                                CivicDTO civic = new CivicDTO(officeName, officialName, i);
                                official.put("office",officeName);
                                lists.add(civic);
                            }

                            JSONObject normal = result.getJSONObject("normalizedInput");
                            String city = normal.getString("city");
                            String state = normal.getString("state");
                            String zip = normal.getString("zip");
                            topContent = city + ", " + state + ", " + zip;
                            normalizedInput.setText(topContent);

                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            Log.e("MYAPP", "unexpected JSON exception", e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"City, State or Zip Code Not Found", Toast.LENGTH_SHORT).show();
                topContent = "No Data For Location";
                normalizedInput.setText(topContent);
            }
        });
        queue.add(stringRequest);
    }

    //Chọn Item About or Search
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.about:
                //Gọi intent aboutativity
                Intent i = new Intent(this, AboutActivity.class);
                startActivity(i);
                break;
            case R.id.search:
                //mở dialog search ở giữa màn hình
                openSearchDialog(Gravity.CENTER);
                break;

        }
        return true;
    }

    //Hàm mở dialog search
    private void openSearchDialog(int gravity){
        //tạo mới dialog
        final Dialog dialog = new Dialog(this);
        //không tiêu đề
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //set content layout
        dialog.setContentView(R.layout.search_popup);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if(Gravity.CENTER == gravity){
            // mất khi click vào bất kì đâu ngoài dialog
            dialog.setCancelable(true);
        }else{
            //click ra bên ngoài dialog thì nó vẫn không bị mất
            dialog.setCancelable(false);
        }

        //khởi tạo components
        EditText txtSearch = dialog.findViewById(R.id.txtSearch);
        Button cancel = dialog.findViewById(R.id.cancel);
        Button submit = dialog.findViewById(R.id.search);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Chuỗi search rỗng => return
                String valueSearch = txtSearch.getText().toString();
                if(valueSearch == ""){
                    return;
                }
                //Tìm url với value search, ẩn dialog
                String url = MAIN_URL + valueSearch;
                handleSearch(url);
                dialog.hide();
            }
        });

        dialog.show();
    }
    //Kiểm tra kết nối mạng
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
