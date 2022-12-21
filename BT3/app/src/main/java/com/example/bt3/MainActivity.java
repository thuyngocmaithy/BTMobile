package com.example.bt3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    ImageView imgLogo;
    ListView listView;
    ArrayList<String> arrFood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //View logo
        imgLogo = (ImageView) findViewById(R.id.imageView);
        imgLogo.setImageResource(R.drawable.foodpet_logo);
        //List view
        listView = (ListView) findViewById(R.id.listView);
        //View Catefory

        arrFood = new ArrayList<>();
        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();
            //parse xml từ file food.xml
            InputStream inputStream = getApplicationContext().getAssets().open("food.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            ArrayList<Food> foods = parseXML(parser);
            for (Food food : foods) {
                //duyệt các đối tượng food => lấy name để hiển thị item danh mục
                arrFood.add(food.getName());
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //set adapter tạo các item danh mục food
        ArrayAdapter adapter = new ArrayAdapter(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                arrFood);
        listView.setAdapter(adapter);

        //set sự kiện onclick danh mục => new intent ListItemActivity (ds các thức ăn trong danh mục)
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ListItemActivity.class);
                //putExtra tên thức ăn qua intent ListItemActivity
                intent.putExtra("FoodName", listView.getItemAtPosition(position).toString());
                startActivity(intent);
            }
        });
       /* new ReadRSS().execute("https://www.petfoodindustry.com/rss");*/
    }


    //parse xml sang list food
    public ArrayList<Food> parseXML(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<Food> foods = null;
        int eventType = parser.getEventType();
        Food food = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            //lặp đến khi hết file
            String name;
            switch (eventType) {
                //bắt đầu file => tạo arraylist foods chứa các danh mục thức ăn
                case XmlPullParser.START_DOCUMENT:
                    foods = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals("food")) {
                        food = new Food();
                        food.id = parser.getAttributeValue(null, "id");
                    } else if (food != null) {
                        if (name.equals("name")) {
                            food.name = parser.nextText();
                        }
                        else if(name.equals("link"))
                        {
                            food.link = parser.nextText();
                        }
                    }
                    break;
                    //kết thúc 1 tag trong xml
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("food") && food != null) {
                        //thêm vào list foods
                        foods.add(food);
                    }
            }
            eventType = parser.next();
        }
        return foods;
    }



}