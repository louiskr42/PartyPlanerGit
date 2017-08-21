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

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Kalender extends AppCompatActivity {

    CompactCalendarView calendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());

    ArrayList<Event>    eventList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_kalender);

        eventList = new ArrayList<>();

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);

        calendar = (CompactCalendarView)findViewById(R.id.compactcalendar_view);
        calendar.setUseThreeLetterAbbreviation(true);

        //set an event
        final Event ev1 = new Event(Color.RED, 1503352800000L, "Neues Event");
        calendar.addEvent(ev1);
        eventList.add(ev1);

        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();

                final Event newEvent = new Event(Color.RED, dateClicked.getTime(), "Neues Event");

                /*if (dateClicked.getDate() == 1503403200000L){
                    Toast.makeText(context, "Heurekah", Toast.LENGTH_SHORT).show();
                }else {
                    Event newEvent = new Event(Color.RED, dateClicked.getTime(), "Blocked");
                    calendar.addEvent(newEvent);
                }*/

                    if (newEvent.getTimeInMillis() == ev1.getTimeInMillis()) {
                        Toast.makeText(context, "Test Event", Toast.LENGTH_SHORT).show();
                    } else {
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
