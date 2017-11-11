package com.coreelements.de.partyplaner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.ads.MobileAds;

public class MainMenu extends AppCompatActivity implements View.OnTouchListener {

    SharedPreferences infoTodos, infoCalendar, infoShopping, infoGuestlistFree, infoGuestlistTotal, launchCounter;
    SharedPreferences.Editor launchCounterEditor;

    @BindView(R.id.todo_menu_layout) RelativeLayout todoRl;
    @BindView(R.id.guestlist_menu_layout) RelativeLayout guestlistRl;
    @BindView(R.id.calendar_menu_layout) RelativeLayout calendarRl;
    @BindView(R.id.shopping_menu_layout) RelativeLayout shoppingRl;

    @BindView(R.id.todoIconImageView) ImageView todoIV;
    @BindView(R.id.guestlistIconImageView) ImageView guestListIV;
    @BindView(R.id.calendarIconImageView) ImageView calendarIV;
    @BindView(R.id.shoppingIconImageView) ImageView shoppingIV;

    @BindView(R.id.todoInfoTextView) TextView todoInfoTextView;
    @BindView(R.id.guestlistInfoTextView) TextView guestlistInfoTextView;
    @BindView(R.id.calendarInfoTextView) TextView calendarInfoTextView;
    @BindView(R.id.shoppingInfoTextView) TextView shoppingInfoTextView;

    @BindView(R.id.shoppingNameTextView) TextView shoppingNameTextView;
    @BindView(R.id.todoNameTextView) TextView todoNameTextView;
    @BindView(R.id.calendarNameTextView) TextView calendarNameTextView;
    @BindView(R.id.guestlistNameTextView) TextView guestlistNameTextView;

    @BindView(R.id.menuScrollView) ScrollView scrollView;

    int counter;

    int themeGreyHighkight = R.color.colorGreyHighlightMain;
    int themeAccent = R.color.colorLightBlue700;
    int themeWhite = R.color.colorWhite;
    int themeMainColor = R.color.colorLightBlue900;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_final);

        ButterKnife.bind(this);

        initializeObjects();

        countLaunch();

    }

    public void countLaunch() {

        counter = launchCounter.getInt("launchCount", 0);

        if (counter == 999) {

        }
        else if (counter != 0) {

            if (counter == 4) {

                launchCounterEditor.putInt("launchCount", 0);
                launchCounterEditor.commit();

            }
            else if (counter > 4) {

                launchCounterEditor.putInt("launchCount", 0);
                launchCounterEditor.commit();

            }
            else {

                counter++;

                launchCounterEditor.putInt("launchCount", counter);
                launchCounterEditor.commit();
            }

        }
        else {

            AlertDialog.Builder mainbuilder = new AlertDialog.Builder(this);
            mainbuilder.setMessage(R.string.wanna_rate);
            mainbuilder.setCancelable(true);

            mainbuilder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    String url = "https://play.google.com/store/apps/details?id=com.coreelements.de.partyplaner&hl=de";

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));

                    startActivity(intent);

                    launchCounterEditor.putInt("launchCount", 999);
                    launchCounterEditor.commit();
                }
            });

            mainbuilder.setNegativeButton(R.string.nein, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog maindialog = mainbuilder.create();
            maindialog.show();

            counter++;

            launchCounterEditor.putInt("launchCount", counter);
            launchCounterEditor.commit();

        }

    }

    public void initializeObjects() {

        initializeAdBanner();
        defineObjects();
        setColorFilterImageViews();
        setColorFilterInfoTextViews();
        getInfos();

    }

    public void defineObjects() {

        todoRl.setOnTouchListener(this);
        guestlistRl.setOnTouchListener(this);
        calendarRl.setOnTouchListener(this);
        shoppingRl.setOnTouchListener(this);
        scrollView.setOnTouchListener(this);

        infoTodos = this.getSharedPreferences("Info", MODE_PRIVATE);
        infoCalendar = this.getSharedPreferences("Info", MODE_PRIVATE);
        infoShopping = this.getSharedPreferences("Info", MODE_PRIVATE);
        infoGuestlistFree = this.getSharedPreferences("Info", MODE_PRIVATE);
        infoGuestlistTotal = this.getSharedPreferences("Info", MODE_PRIVATE);

        launchCounter = this.getSharedPreferences("LaunchCounter", MODE_PRIVATE);
        launchCounterEditor = launchCounter.edit();
    }

    public void initializeAdBanner() {

        MobileAds.initialize(this, "ca-app-pub-1814335808278709~4572376197");

        AdView adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        adView.loadAd(adRequest);

    }

    public void setColorFilterImageViews(){
        int highlightColor = themeGreyHighkight;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            todoIV.setColorFilter(getColor(highlightColor));
            shoppingIV.setColorFilter(getColor(highlightColor));
            calendarIV.setColorFilter(getColor(highlightColor));
            guestListIV.setColorFilter(getColor(highlightColor));
        }
        else {
            todoIV.setColorFilter(Color.GRAY);
            shoppingIV.setColorFilter(Color.GRAY);
            calendarIV.setColorFilter(Color.GRAY);
            guestListIV.setColorFilter(Color.GRAY);
        }
    }

    private void setColorFilterInfoTextViews() {
        int accentColor = themeAccent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            todoInfoTextView.setTextColor(getColor(accentColor));
            guestlistInfoTextView.setTextColor(getColor(accentColor));
            calendarInfoTextView.setTextColor(getColor(accentColor));
            shoppingInfoTextView.setTextColor(getColor(accentColor));
        }
        else {
            todoInfoTextView.setTextColor(Color.GRAY);
            guestlistInfoTextView.setTextColor(Color.GRAY);
            calendarInfoTextView.setTextColor(Color.GRAY);
            shoppingInfoTextView.setTextColor(Color.GRAY);
        }
    }

    private void setColorFilterNameTextViews(){
        int mainColor = themeMainColor;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            todoNameTextView.setTextColor(getColor(mainColor));
            shoppingNameTextView.setTextColor(getColor(mainColor));
            calendarNameTextView.setTextColor(getColor(mainColor));
            guestlistNameTextView.setTextColor(getColor(mainColor));
        }

    }

    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                switch (v.getId()) {
                    case R.id.todo_menu_layout:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            todoRl.setBackgroundColor(getColor(R.color.selectedGreyLight));
                        }
                        return true;

                    case R.id.guestlist_menu_layout:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            guestlistRl.setBackgroundColor(getColor(R.color.selectedGreyLight));
                        }
                        return true;

                    case R.id.calendar_menu_layout:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            calendarRl.setBackgroundColor(getColor(R.color.selectedGreyLight));
                        }
                        return true;

                    case R.id.shopping_menu_layout:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            shoppingRl.setBackgroundColor(getColor(R.color.selectedGreyLight));
                        }
                        return true;
                }

            case MotionEvent.ACTION_UP:

                launchActivity(v);
        }
        return false;
    }


    public void launchActivity(View v) {

        if (v.getId() == R.id.todo_menu_layout) {
            startActivity(new Intent(this, ToDosList.class));
            finish();
        }
        else if (v.getId() == R.id.guestlist_menu_layout) {
            startActivity(new Intent(this, GaesteListe.class));
            finish();
        }
        else if (v.getId() == R.id.calendar_menu_layout) {
            startActivity(new Intent(this, Kalender.class));
            finish();
        }
        else if (v.getId() == R.id.shopping_menu_layout) {
            startActivity(new Intent(this, Einkaufsliste.class));
            finish();
        }
        else if (v.getId() == R.id.menuScrollView) {
            setItemsToNormal();
        }

        getInfos();

}

    private void setItemsToNormal() {
        int mainColor = themeWhite;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            todoRl.setBackgroundColor(getColor(mainColor));
            guestlistRl.setBackgroundColor(getColor(mainColor));
            calendarRl.setBackgroundColor(getColor(mainColor));
            shoppingRl.setBackgroundColor(getColor(mainColor));
        }

        setColorFilterInfoTextViews();
        setColorFilterNameTextViews();
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

    public void getInfos() {

        setItemsToNormal();

        if (infoTodos.getInt("infoTodos", 0) != 0) {
            String info = getString(R.string.somany_tasks_to_do);
            info = String.format(info, infoTodos.getInt("infoTodos", 0) + "");
            todoInfoTextView.setText(info);
        }
        else {
            todoInfoTextView.setText(R.string.all_tasks_done);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                todoInfoTextView.setTextColor(getColor(themeGreyHighkight));
                todoNameTextView.setTextColor(getColor(themeGreyHighkight));
            }
        }

        if (infoCalendar.getInt("infoCalendar", 0) != 0) {
            String info = getString(R.string.somany_available_events);
            info = String.format(info, infoCalendar.getInt("infoCalendar", 0) + "");
            calendarInfoTextView.setText(info);
        }
        else {
            calendarInfoTextView.setText(R.string.no_available_events);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                calendarInfoTextView.setTextColor(getColor(themeGreyHighkight));
                calendarNameTextView.setTextColor(getColor(themeGreyHighkight));
            }
        }

        if (infoShopping.getInt("infoShopping", 0) != 0) {
            String info = getString(R.string.somany_items_on_list);
            info = String.format(info, infoShopping.getInt("infoShopping", 0) + "");
            shoppingInfoTextView.setText(info);
        }
        else {
            shoppingInfoTextView.setText(R.string.no_items_on_shoppinglist);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                shoppingInfoTextView.setTextColor(getColor(themeGreyHighkight));
                shoppingNameTextView.setTextColor(getColor(themeGreyHighkight));

            }
        }

        if (infoGuestlistTotal.getInt("infoGuestlistTotal", 0) != 0) {
            guestlistInfoTextView.setText(infoGuestlistFree.getInt("infoGuestlistFree", 0) + " von " +
                    infoGuestlistTotal.getInt("infoGuestlistTotal", 0) +
                    " Gästen haben zugesagt");
        }
        else {
            guestlistInfoTextView.setText(R.string.no_guests_on_guestlist);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                guestlistInfoTextView.setTextColor(getColor(themeGreyHighkight));
                guestlistNameTextView.setTextColor(getColor(themeGreyHighkight));
            }
        }
    }
}
