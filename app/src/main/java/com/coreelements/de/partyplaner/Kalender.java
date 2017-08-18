package com.coreelements.de.partyplaner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Kalender extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_kalender);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainMenu.class));
        finish();
        //beendet die Activity und geht zurück zum Hauptmenü bei Klick auf die Zurück-Taste
    }
}
