package com.coreelements.de.partyplaner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity implements View.OnTouchListener {

    TextView aufgabenTV, guesteListeTV, kalenderTV, einkaufsListeTV;
    ImageView todoIV, guestListIV, calendarIV, shoppingIV;
    ScrollView sView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_new);

        sView = (ScrollView)findViewById(R.id.scrollView);
        sView.setOnTouchListener(this);

        aufgabenTV = (TextView)findViewById(R.id.todoTextView);
        aufgabenTV.setOnTouchListener(this);

        guesteListeTV = (TextView)findViewById(R.id.guestlistTextView);
        guesteListeTV.setOnTouchListener(this);

        kalenderTV = (TextView)findViewById(R.id.calendarTextView);
        kalenderTV.setOnTouchListener(this);

        einkaufsListeTV = (TextView)findViewById(R.id.shoppingTextView);
        einkaufsListeTV.setOnTouchListener(this);

        todoIV = (ImageView)findViewById(R.id.todoImageView);
        todoIV.setOnTouchListener(this);

        guestListIV = (ImageView)findViewById(R.id.guestlistImageView);
        guestListIV.setOnTouchListener(this);

        calendarIV = (ImageView)findViewById(R.id.calendarImageView);
        calendarIV.setOnTouchListener(this);

        shoppingIV = (ImageView)findViewById(R.id.shoppingImageView);
        shoppingIV.setOnTouchListener(this);

        setColorFilter();
    }

    public void setColorFilter(){
        shoppingIV.setColorFilter(Color.GRAY);
        calendarIV.setColorFilter(Color.GRAY);
        guestListIV.setColorFilter(Color.GRAY);
        todoIV.setColorFilter(Color.GRAY);

        aufgabenTV.setTextColor(Color.GRAY);
        guesteListeTV.setTextColor(Color.GRAY);
        kalenderTV.setTextColor(Color.GRAY);
        einkaufsListeTV.setTextColor(Color.GRAY);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                switch (v.getId()) {
                    case R.id.todoTextView:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            todoIV.setBackgroundColor(getColor(R.color.selectedGreyLight));
                            aufgabenTV.setBackgroundColor(getColor(R.color.selectedGreyLight));
                        }
                        return true;
                }

                switch (v.getId()) {
                    case R.id.todoImageView:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            todoIV.setBackgroundColor(getColor(R.color.selectedGreyLight));
                            aufgabenTV.setBackgroundColor(getColor(R.color.selectedGreyLight));
                        }
                        return true;
                }

                switch (v.getId()){
                    case R.id.guestlistTextView:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            guestListIV.setBackgroundColor(getColor(R.color.selectedGreyLight));
                            guesteListeTV.setBackgroundColor(getColor(R.color.selectedGreyLight));
                        }
                        return true;
                }

                switch (v.getId()) {
                    case R.id.guestlistImageView:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            guestListIV.setBackgroundColor(getColor(R.color.selectedGreyLight));
                            guesteListeTV.setBackgroundColor(getColor(R.color.selectedGreyLight));
                        }
                        return true;
                }

                switch (v.getId()){
                    case R.id.calendarTextView:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            calendarIV.setBackgroundColor(getColor(R.color.selectedGreyLight));
                            kalenderTV.setBackgroundColor(getColor(R.color.selectedGreyLight));
                        }
                        return true;
                }

                switch (v.getId()) {
                    case R.id.calendarImageView:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            calendarIV.setBackgroundColor(getColor(R.color.selectedGreyLight));
                            kalenderTV.setBackgroundColor(getColor(R.color.selectedGreyLight));
                        }
                        return true;
                }

                switch (v.getId()){
                    case R.id.shoppingTextView:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            shoppingIV.setBackgroundColor(getColor(R.color.selectedGreyLight));
                            einkaufsListeTV.setBackgroundColor(getColor(R.color.selectedGreyLight));
                        }
                        return true;
                }

                switch (v.getId()) {
                    case R.id.shoppingImageView:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            shoppingIV.setBackgroundColor(getColor(R.color.selectedGreyLight));
                            einkaufsListeTV.setBackgroundColor(getColor(R.color.selectedGreyLight));
                        }
                        return true;
                }
                return true;

            case MotionEvent.ACTION_UP:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setItemsToNormal();
                }
                switch (v.getId()) {
                    case R.id.todoTextView:
                        startActivity(new Intent(getApplicationContext(), ToDosList.class));
                        finish();
                        return true;
                }

                switch (v.getId()) {
                    case R.id.todoImageView:
                        startActivity(new Intent(getApplicationContext(), ToDosList.class));
                        finish();
                        return true;
                }

                switch (v.getId()) {
                    case R.id.guestlistTextView:
                        startActivity(new Intent(getApplicationContext(), GaesteListe.class));
                        finish();
                        return true;
                }

                switch (v.getId()) {
                    case R.id.guestlistImageView:
                        startActivity(new Intent(getApplicationContext(), GaesteListe.class));
                        finish();
                        return true;
                }

                switch (v.getId()) {
                    case R.id.calendarTextView:
                        startActivity(new Intent(getApplicationContext(), Kalender.class));
                        finish();
                        return true;
                }

                switch (v.getId()) {
                    case R.id.calendarImageView:
                        startActivity(new Intent(getApplicationContext(), Kalender.class));
                        finish();
                        return true;
                }

                switch (v.getId()) {
                    case R.id.shoppingTextView:
                        startActivity(new Intent(getApplicationContext(), Einkaufsliste.class));
                        finish();
                        return true;
                }

                switch (v.getId()) {
                    case R.id.shoppingImageView:
                        startActivity(new Intent(getApplicationContext(), Einkaufsliste.class));
                        finish();
                        return true;
                }

                switch (v.getId()) {
                    case R.id.scrollView:
                        setItemsToNormal();
                        return true;
                }

                return true;


        }
        return false;
    }

    private void setItemsToNormal() {
        todoIV.setBackgroundColor(Color.WHITE);
        guestListIV.setBackgroundColor(Color.WHITE);
        calendarIV.setBackgroundColor(Color.WHITE);
        shoppingIV.setBackgroundColor(Color.WHITE);
        aufgabenTV.setBackgroundColor(Color.WHITE);
        guesteListeTV.setBackgroundColor(Color.WHITE);
        kalenderTV.setBackgroundColor(Color.WHITE);
        einkaufsListeTV.setBackgroundColor(Color.WHITE);
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder mainbuilder = new AlertDialog.Builder(this);
        mainbuilder.setMessage(R.string.willst_du_beenden);
        mainbuilder.setCancelable(true);

        mainbuilder.setPositiveButton(R.string.ja, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        mainbuilder.setNegativeButton(R.string.nein, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog maindialog = mainbuilder.create();
        maindialog.show();
    }
}
