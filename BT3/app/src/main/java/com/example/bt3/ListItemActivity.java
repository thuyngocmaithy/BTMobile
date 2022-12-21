package com.example.bt3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ListItemActivity extends AppCompatActivity implements DialogFoodActivity.myInterface {
    Toolbar toolbar;
    ImageView imgLogo;
    ListView listView;
    ArrayList<String> arrItem;
    ArrayAdapter adapter;
    String link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Ẩn header mặc định
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        //
        setContentView(R.layout.activity_list_item);
        //Tạo toolbar hiển thị tên danh mục
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imgLogo= (ImageView) findViewById(R.id.imageView1);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            toolbar.setTitle(bundle.getString("FoodName"));
        }
        //View Logo
        imgLogo = (ImageView) findViewById(R.id.imageView1);
        imgLogo.setImageResource(R.drawable.foodpet_logo);

        //Inner List Item
        listView = (ListView) findViewById(R.id.listViewItem1);
        arrItem = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrItem);
        listView.setAdapter(adapter);

        //View List view (so sánh tên danh mục => hiển thị danh sách thức ăn tương ứng)
        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();
            InputStream inputStream = getApplicationContext().getAssets().open("food.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            MainActivity mainActivity = new MainActivity();
            ArrayList<Food> foods = mainActivity.parseXML(parser);

            for (Food food : foods) {
                //nếu tên food trong file food.xml trùng với tên food người dùng đã chọn ở MainActivity
                if(food.getName().equals(bundle.getString("FoodName")))
                {
               /*     System.out.println("NAME: "+food.getName());
                    System.out.println("LINK: "+food.getLink());*/
                    //lấy liên kết từ đối tượng food
                    link = food.getLink();
                    //đọc rss
                    new ReadRSS().execute(link);
                }
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Onclick view dialog
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                openDialogFood(Gravity.CENTER);
                DialogFoodActivity dialogFoodActivity = new DialogFoodActivity();

                Bundle bundle = new Bundle();
                bundle.putString("link",link);
                dialogFoodActivity.setArguments(bundle);

                dialogFoodActivity.show(getSupportFragmentManager(), listView.getItemAtPosition(position).toString());
            }
        });
    }

    @Override
    public void text(String link) {
        Intent intent = new Intent(ListItemActivity.this, ViewDetailFoodActivity.class);
        intent.putExtra("LinkDetail", link);
        startActivity(intent);
    }

    private class ReadRSS extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line);
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content.toString();
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);
            NodeList nodeList = document.getElementsByTagName("item");
            String tieude = "";
            for(int i=0; i<nodeList.getLength(); i++)
            {
                Element element = (Element) nodeList.item(i);
                tieude=parser.getValue(element,"title");
                arrItem.add(tieude);
            }
            adapter.notifyDataSetChanged();
        }

    }
}