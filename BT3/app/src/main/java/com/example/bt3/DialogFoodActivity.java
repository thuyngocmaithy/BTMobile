package com.example.bt3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class DialogFoodActivity extends AppCompatDialogFragment {

    ImageView imgLogo, imgFood;
    TextView textViewTieuDe, textViewMoTa;
    ArrayAdapter adapter;
    String link, linkDetail, hinhAnh, moTa;
    Button btnMore, btnClose;
    View view;
    private myInterface myInterface;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.layout_dialog_food, null);
        builder.setView(view);
        //ImgLogo
        imgLogo = view.findViewById(R.id.imageView1);
        imgLogo.setImageResource(R.drawable.foodpet_logo);
        //Lấy tên tiêu đề
        textViewTieuDe = view.findViewById(R.id.textViewTieuDe);
        Bundle bundle = getArguments();
        /*    Bundle bundle = getIntent().getExtras();*/
        if (bundle != null) {
            System.out.println("LINK: " + bundle.getString("link"));
            link = bundle.getString("link");
            new ReadRSS().execute(link);
        }
        textViewTieuDe.setText(getTag());
        //HÌNH ẢNH FOOD
        imgFood = view.findViewById(R.id.imgFood);
        imgFood.setImageResource(R.drawable.foodpet_logo);

        //button
        btnMore = view.findViewById(R.id.btnMore);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.text(linkDetail);

            }
        });
        btnClose = view.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            myInterface = (myInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+ " implement myinterface");
        }

    }

    public interface myInterface {
        void text(String link);
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
            String mota = "";
            String urlHinhanh = "";
            String link = "";
            String htmlDesc = "", htmlImg = "";
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                tieude = parser.getValue(element, "title");
                link = parser.getValue(element, "link");
                htmlDesc = parser.getValueDesc(element, "description");
                mota = parser.getDescContent(htmlDesc);

                NodeList media = element.getElementsByTagName("media:content");
                Element elementMedia = (Element) media.item(0);
                if (elementMedia != null) {
                    urlHinhanh = elementMedia.getAttribute("url");
                }
                if (textViewTieuDe.getText().equals(tieude)) {
                    hinhAnh = urlHinhanh;
                    linkDetail = link;
                    moTa = mota;
                    System.out.println("MÔ TẢ:" + moTa);
                    textViewMoTa = view.findViewById(R.id.textViewMoTa);
                    textViewMoTa.setText(moTa);

                    //HÌNH ẢNH FOOD
                    Picasso.get().load(hinhAnh).into(imgFood);

                }
            }

        }

    }
}