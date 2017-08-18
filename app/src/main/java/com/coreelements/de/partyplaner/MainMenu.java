package com.coreelements.de.partyplaner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainMenu extends AppCompatActivity implements View.OnTouchListener {

    TextView aufgabenTV, guesteListeTV, kalenderTV, einkaufsListeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_menu);

        aufgabenTV = (TextView)findViewById(R.id.aufgabenTextView);
        aufgabenTV.setOnTouchListener(this);

        guesteListeTV = (TextView)findViewById(R.id.guesteListeTextView);
        guesteListeTV.setOnTouchListener(this);

        kalenderTV = (TextView)findViewById(R.id.kalenderTextView);
        kalenderTV.setOnTouchListener(this);

        einkaufsListeTV = (TextView)findViewById(R.id.einkaufsListeTextView);
        einkaufsListeTV.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                switch (v.getId()) {
                    case R.id.aufgabenTextView:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            aufgabenTV.setTextSize(33);
                        }
                        return true;
                }

                switch (v.getId()){
                    case R.id.guesteListeTextView:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            guesteListeTV.setTextSize(33);
                        }
                        return true;
                }

                switch (v.getId()){
                    case R.id.kalenderTextView:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            kalenderTV.setTextSize(33);
                        }
                        return true;
                }

                switch (v.getId()){
                    case R.id.einkaufsListeTextView:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            einkaufsListeTV.setTextSize(33);
                        }
                        return true;
                }
                return true;

            case MotionEvent.ACTION_UP:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    aufgabenTV.setTextSize(30);
                    guesteListeTV.setTextSize(30);
                    kalenderTV.setTextSize(30);
                    einkaufsListeTV.setTextSize(30);
                }
                switch (v.getId()) {
                    case R.id.aufgabenTextView:
                        startActivity(new Intent(getApplicationContext(), ToDosList.class));
                        finish();
                        return true;
                }

                switch (v.getId()) {
                    case R.id.guesteListeTextView:
                        startActivity(new Intent(getApplicationContext(), GaesteListe.class));
                        finish();
                        return true;
                }

                switch (v.getId()) {
                    case R.id.kalenderTextView:
                        startActivity(new Intent(getApplicationContext(), Kalender.class));
                        finish();
                        return true;
                }

                switch (v.getId()) {
                    case R.id.einkaufsListeTextView:
                        startActivity(new Intent(getApplicationContext(), Einkaufsliste.class));
                        finish();
                        return true;
                }

                return true;


        }
        return false;
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
