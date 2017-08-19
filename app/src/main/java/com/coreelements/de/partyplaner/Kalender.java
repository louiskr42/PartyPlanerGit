package com.coreelements.de.partyplaner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.Date;
import java.util.Locale;

public class Kalender extends AppCompatActivity {

    CompactCalendarView calendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_kalender);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);

        calendar = (CompactCalendarView)findViewById(R.id.compactcalendar_view);
        calendar.setUseThreeLetterAbbreviation(true);

        //set an event
        Event ev1 = new Event(Color.RED, 1503403200000L, "Test-Event");
        calendar.addEvent(ev1);

        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();

                if (dateClicked.toString() == "Tue Aug 22 12:00:00  GMT 2017") {
                    Toast.makeText(context, "Test Event", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Nicht Test Event", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    actionBar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainMenu.class));
        finish();
        //beendet die Activity und geht zurück zum Hauptmenü bei Klick auf die Zurück-Taste
    }
}
