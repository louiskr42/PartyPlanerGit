package com.coreelements.de.partyplaner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import butterknife.BindView;

public class Abstimmungen extends AppCompatActivity {

    @BindView(R.id.voteEditText) EditText et;
    @BindView(R.id.voteListView) ListView lv;
    @BindView(R.id.voteDeleteAllButton) Button deleteAllBtn;
    @BindView(R.id.voteDeleteSingleButton) Button deleteSingleBtn;
    @BindView(R.id.voteTextView) TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_abstimmungen);

        MobileAds.initialize(this, "ca-app-pub-1814335808278709~4572376197");

        AdView adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
    }

    public void initializeLayout() {

    }
}
