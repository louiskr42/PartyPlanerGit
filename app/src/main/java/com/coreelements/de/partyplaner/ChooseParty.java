package com.coreelements.de.partyplaner;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseParty extends AppCompatActivity {

    @BindView(R.id.choosePartyListView) ListView chooseLV;

    SharedPreferences           currentPartyShared;
    SharedPreferences.Editor    currentPartyEditor;

    String                      currentParty;

    ArrayList<String>           parties;
    ArrayAdapter<String>        partiesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_choose_party);

        ButterKnife.bind(this);

        initializeObjects();

        stream();

    }

    private void stream() {

        currentPartyEditor.putString("party", "PAAARTEEYYY!!!");
        currentPartyEditor.commit();

    }

    private void initializeObjects() {

        defineListAndAdapter();
        defineSharedPreferences();

    }

    private void defineSharedPreferences() {

        currentPartyShared = this.getSharedPreferences("Party", MODE_PRIVATE);
        currentPartyEditor = currentPartyShared.edit();

        currentParty = currentPartyShared.getString("party", "party1");

    }

    private void defineListAndAdapter() {

        parties.add("Party1");

        partiesAdapter = new ArrayAdapter<>(this, R.layout.simple_list, parties);
        chooseLV.setAdapter(partiesAdapter);

    }
}
