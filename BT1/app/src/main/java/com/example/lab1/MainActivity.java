package com.example.lab1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int seek;
    SeekBar sb;
    LinearLayout lay1,lay2,lay3,lay4,lay5;

    private AlertDialog alertDialog;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mainLayout:
             //   Toast.makeText(this, "Linear Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.linearLayout:
              //  Toast.makeText(this, "Linear Clicked", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(this, MainLinearlayout.class);
                startActivity(intent3);
                return true;

            case R.id.relativeLayout:
             //   Toast.makeText(this, "Relative Clicked", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(this, MainRelativelayout.class);
                startActivity(intent1);
                return true;

            case R.id.tableLayout:
            //    Toast.makeText(this, "Table Clicked", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(this, MainTablelayout.class);
                startActivity(intent2);
                return true;

            case R.id.DialogOption:
                Toast.makeText(this, "Dialog Clicked", Toast.LENGTH_SHORT).show();
                showDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Linear Clicked", Toast.LENGTH_SHORT).show();
        lay1 = (LinearLayout)findViewById(R.id.l1);
        lay2 = (LinearLayout)findViewById(R.id.l2);
        lay3 = (LinearLayout)findViewById(R.id.l3);
        lay4 = (LinearLayout)findViewById(R.id.l4);
        lay5 = (LinearLayout)findViewById(R.id.l5);
        sb = (SeekBar) findViewById(R.id.seekBar);
        sb.setMax(100);
        sb.setOnSeekBarChangeListener(seekBarChangeListener);

    }

    private void showDialog(){
        if (alertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.dialog,
                    (ViewGroup) findViewById(R.id.dialogLayout)
            );
            builder.setView(view);
            alertDialog = builder.create();
            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        }

        alertDialog.show();
    }
    int progChange = 0;
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            //Initial color state values

            int[] redArray = {255, 0, 0};
            int[] blueArray = {0, 0, 255};
            int[] yellowArray = {255, 255, 0};
            int[] whiteArray = {255, 255, 255};
            int[] pinkArray = {255, 0, 255};
            progChange = progress;

            //Make incremental color value changes

            redArray[0] = redArray[0] - (255/100)*progChange;
            redArray[1] = redArray[1] + (229/100)*progChange;
            redArray[2] = redArray[2] + (238/100)*progChange;
            blueArray[0] = blueArray[0] + (255/100)*progChange;
            blueArray[1] = blueArray[1] + (102/100)*progChange;
            blueArray[2] = blueArray[2] - (255/100)*progChange;
            yellowArray[0] = yellowArray[0] - (125/100)*progChange;
            yellowArray[1] = yellowArray[1] - (255/100)*progChange;
            yellowArray[2] = yellowArray[2] + (130/100)*progChange;
            whiteArray[0] = whiteArray[0] - (200/100)*progChange;
            whiteArray[1] = whiteArray[1] - (255/100)*progChange;
            whiteArray[2] = whiteArray[2] - (255/100)*progChange;
            pinkArray[0] =pinkArray[0] - (255/100)*progChange;
            pinkArray[1] = pinkArray[1] + (140/100)*progChange;
            pinkArray[2] = pinkArray[2] - (50/100)*progChange;

            //Set the boxes to new colors

            lay1.setBackgroundColor(Color.rgb(redArray[0],redArray[1],redArray[2]));
            lay2.setBackgroundColor(Color.rgb(blueArray[0],blueArray[1],blueArray[2]));
            lay3.setBackgroundColor(Color.rgb( yellowArray[0],yellowArray[1],yellowArray[2]));
            lay4.setBackgroundColor(Color.rgb(whiteArray[0],whiteArray[1],whiteArray[2]));
            lay5.setBackgroundColor(Color.rgb(pinkArray[0],pinkArray[1],pinkArray[2]));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            Log.d("AAA", "onStartTrackingTouch: START");

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Log.d("AAA", "onStopTrackingTouch: STOP");

        }
    };
}