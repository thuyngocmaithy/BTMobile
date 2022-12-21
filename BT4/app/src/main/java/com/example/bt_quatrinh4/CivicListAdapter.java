package com.example.bt_quatrinh4;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CivicListAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<CivicDTO> lists = new ArrayList<>();

    public CivicListAdapter(Activity activity, ArrayList<CivicDTO> lists){
        this.activity = activity;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = activity.getLayoutInflater();

        //set view 1 item trong list
        view = inflater.inflate(R.layout.civic_info,null);

        TextView officeName = view.findViewById(R.id.officeName);
        TextView officialName = view.findViewById(R.id.officialName);

        officeName.setText(lists.get(i).getOfficeName());
        officialName.setText(lists.get(i).getOfficialName());

        //Sự kiện onclick => OfficialActivity
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putInt("index",i);
                Intent i = new Intent(activity, OfficialActivity.class);
                i.putExtras(bundle);
               activity.startActivity(i);
            }
        });

        return view;


    }
}
