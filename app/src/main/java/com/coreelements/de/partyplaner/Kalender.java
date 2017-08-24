package com.coreelements.de.partyplaner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Kalender extends AppCompatActivity implements View.OnClickListener {

    CompactCalendarView             calendar;
    private SimpleDateFormat        dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());

    ArrayList<Long>                 blockedEventList;
    ArrayList<Long>                 freeEventList;

    Event                           blockedEvent;

    Event                           selectedEvent;

    Button                          blockBTN, freeBTN, clearBTN;

    ObjectInputStream               blockedInputStream, freeInputStream;

    File                            eventFile,bfFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_kalender);

        blockBTN = (Button)findViewById(R.id.blockButton);
        blockBTN.setOnClickListener(this);

        freeBTN = (Button)findViewById(R.id.freeButton);
        freeBTN.setOnClickListener(this);

        clearBTN = (Button)findViewById(R.id.clearButton);
        clearBTN.setOnClickListener(this);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);

        calendar = (CompactCalendarView)findViewById(R.id.compactcalendar_view);
        calendar.setUseThreeLetterAbbreviation(true);

        eventFile = new File(getDir("dataEvents", MODE_PRIVATE), "blockedEventList");
        try {
            blockedInputStream = new ObjectInputStream(new FileInputStream(eventFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ObjectInputstream, zum Abrufen der in file gespeicherten Aufgaben initialisieren

        if (blockedInputStream != null) {
            try {
                blockedEventList = (ArrayList<Long>) blockedInputStream.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            blockedEventList = new ArrayList<>();
        }
        //Wenn bereits Aufgaben gespeichert sind, lädt er diese. Wenn nicht erstellt er eine neue Liste


        bfFile = new File(getDir("dataBf", MODE_PRIVATE), "freeEventList");
        try {
            freeInputStream = new ObjectInputStream(new FileInputStream(bfFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ObjectInputstream, zum Abrufen der in file gespeicherten Aufgaben initialisieren

        if (freeInputStream != null) {
            try {
                freeEventList = (ArrayList<Long>) freeInputStream.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            freeEventList = new ArrayList<>();
        }
        //Wenn bereits Aufgaben gespeichert sind, lädt er diese. Wenn nicht erstellt er eine neue Liste

        //set an event
        /*final Event testEvent = new Event(Color.RED, 1503352800000L, "Neues Event");
        calendar.addEvent(testEvent);
        blockedEventList.add(testEvent.getTimeInMillis());*/

        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

                Context context = getApplicationContext();

                selectedEvent = new Event(Color.BLUE, dateClicked.getTime(), "Ausgewähltes Event");

                    /*if (blockedEventList.contains(selectedEvent.getTimeInMillis())) {

                        if (calendar.getEvents(selectedEvent.getTimeInMillis()).contains(blockedEvent)) {

                            Toast.makeText(context, "Test Event", Toast.LENGTH_SHORT).show();
                            calendar.getEvents(selectedEvent.getTimeInMillis()).clear();
                            final Event freeEvent = new Event(Color.GREEN, dateClicked.getTime(), "Freies Event");
                            calendar.addEvent(freeEvent);

                        }else{

                            calendar.getEvents(selectedEvent.getTimeInMillis()).clear();
                        }

                    } else {

                        Toast.makeText(context, "Nicht Test Event", Toast.LENGTH_SHORT).show();
                        blockedEvent = new Event(Color.RED, dateClicked.getTime(), "Blocked");
                        calendar.addEvent(blockedEvent);
                        blockedEventList.add(blockedEvent.getTimeInMillis());

                    }*/

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    actionBar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));
                }
            }
        });

        fillCalendar();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainMenu.class));
        finish();
        //beendet die Activity und geht zurück zum Hauptmenü bei Klick auf die Zurück-Taste
    }

    @Override
    public void onClick(View v) {

        Long time;

        if (selectedEvent != null) {
            time = selectedEvent.getTimeInMillis();
        }else{

            Toast.makeText(getApplicationContext(), "Bitte wähle ein Datum aus!", Toast.LENGTH_LONG).show();
            time = 0000000000000L;
        }

        if (v.getId() == R.id.blockButton){

            calendar.getEvents(time).clear();
            blockedEventList.remove(time);
            freeEventList.remove(time);

            final Event newBlockedEvent = new Event(Color.RED, time, "Blocked Event");
            calendar.addEvent(newBlockedEvent);
            blockedEventList.add(time);

            Toast.makeText(getApplicationContext(), getString(R.string.set_blockedEvent), Toast.LENGTH_LONG).show();



        }else if (v.getId() == R.id.freeButton){

            calendar.getEvents(time).clear();
            blockedEventList.remove(time);
            freeEventList.remove(time);

            final Event newFreeEvent = new Event(Color.GREEN, time, "Free Event");
            calendar.addEvent(newFreeEvent);
            freeEventList.add(time);

            Toast.makeText(getApplicationContext(), getString(R.string.set_freeEvent), Toast.LENGTH_LONG).show();

        }else if (v.getId() == R.id.clearButton){

            calendar.getEvents(time).clear();
            blockedEventList.remove(time);
            freeEventList.remove(time);

            Toast.makeText(getApplicationContext(), getString(R.string.cleared_event), Toast.LENGTH_LONG).show();

        }

        stream();
    }

    @Override
    public void onPause() {
        super.onPause();
        stream();
        //lässt stream laufen, wenn die Activity pausiert oder beendet wird
    }

    public void stream(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(eventFile));
            outputStream.writeObject(blockedEventList);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), getString(R.string.speicher_fehler), Toast.LENGTH_LONG).show();
        }

        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(bfFile));
            outputStream.writeObject(freeEventList);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), getString(R.string.speicher_fehler), Toast.LENGTH_LONG).show();
        }
    }

    public void fillCalendar(){

        int i = 0;

        while (i < blockedEventList.size()){

            Long cacheTime = blockedEventList.get(i);

            Event cacheEvent = new Event(Color.RED, cacheTime, "BlockedEvent");

            calendar.addEvent(cacheEvent);


            i++;
        }

        int c = 0;

        while (c < freeEventList.size()){

            Long cacheTime = freeEventList.get(c);

            Event cacheEvent = new Event(Color.GREEN, cacheTime, "FreeEvent");

            calendar.addEvent(cacheEvent);


            c++;
        }

    }
}
