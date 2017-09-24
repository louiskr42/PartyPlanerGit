package com.coreelements.de.partyplaner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class GaesteListe extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, View.OnKeyListener {

    boolean                     deleteMode = false;

    int                         infoFree, infoTotal;

    Button                      deleteAllBTN, deleteSingleBTN;
    TextView                    aufgabenTV, vielleichtTV, jaTV, neinTV, neutralTV;
    ListView                    listLV;
    EditText                    editET;

    ArrayList<String>           guestList;
    ArrayAdapter<String>        adapter;

    File                        file;

    ObjectInputStream           inputStream;

    SharedPreferences           infoGuestlistFree, infoGuestlistTotal;
    SharedPreferences.Editor    infoGuestlistFreeEditor, infoGuestlistTotalEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_guestlist_constriant);

        initializeObjects();

        loadListAndSetAdapter();

        loadItemStatus();

    }

    public void initializeObjects() {

        defineSharedPreferences();
        defineFiles();
        defineTextViews();
        defineListViews();
        defineEditTexts();
        defineButtons();
        initializeAdBanner();

    }

    public void defineFiles() {

        file = new File(getDir("dataGaeste", MODE_PRIVATE), "gaesteListe");

    }

    public void defineButtons() {

        deleteAllBTN = (Button)findViewById(R.id.deleteAllButton);
        deleteAllBTN.setOnClickListener(this);

        deleteSingleBTN = (Button)findViewById(R.id.deleteSingleButton);
        deleteSingleBTN.setOnClickListener(this);

    }

    public void defineEditTexts() {

        editET = (EditText)findViewById(R.id.aufgabenEditText);
        editET.setOnKeyListener(this);

    }

    public void defineTextViews() {

        aufgabenTV = (TextView)findViewById(R.id.aufgabenTextView);
        aufgabenTV.setText(getString(R.string.gib_namen_gast));

        vielleichtTV = (TextView)findViewById(R.id.vielleichtTextView);
        jaTV = (TextView)findViewById(R.id.jaTextView);
        neinTV = (TextView)findViewById(R.id.neinTextView);
        neutralTV = (TextView)findViewById(R.id.neutralTextView);

    }

    public void defineSharedPreferences() {

        infoGuestlistFree = this.getSharedPreferences("Info", MODE_PRIVATE);
        infoGuestlistFreeEditor = infoGuestlistFree.edit();

        infoGuestlistTotal = this.getSharedPreferences("Info", MODE_PRIVATE);
        infoGuestlistTotalEditor = infoGuestlistTotal.edit();

    }

    public void defineListViews() {

        listLV = (ListView)findViewById(R.id.aufgabenListView);
        listLV.setOnItemClickListener(this);

    }

    public void initializeAdBanner() {

        MobileAds.initialize(this, "ca-app-pub-1814335808278709~4572376197");

        AdView adView = (AdView)findViewById(R.id.adViewGuestlist);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);

    }

    public void loadListAndSetAdapter() {

        try {

            inputStream = new ObjectInputStream(new FileInputStream(file));

        }
        catch (IOException e) {

            e.printStackTrace();

        }

        if (inputStream != null) {

            try {

                guestList = (ArrayList<String>) inputStream.readObject();

            }
            catch (IOException | ClassNotFoundException e) {

                e.printStackTrace();

            }

        }
        else {

            guestList = new ArrayList<>();

        }

        adapter = new ArrayAdapter<>(this, R.layout.simple_list, guestList);
        listLV.setAdapter(adapter);

    }

    public void stream() {

        try {

            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(guestList);
            outputStream.flush();
            outputStream.close();

        }
        catch (IOException e) {

            e.printStackTrace();
            Toast.makeText(this, getString(R.string.speicher_fehler), Toast.LENGTH_LONG).show();

        }

        //speichert die Liste über den ObjectOutputStream in file

        infoGuestlistFreeEditor.putInt("infoGuestlistFree", infoFree);
        infoGuestlistFreeEditor.commit();

        infoGuestlistTotalEditor.putInt("infoGuestlistTotal", infoTotal);
        infoGuestlistTotalEditor.commit();
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(), MainMenu.class));
        finish();
        //beendet die Activity und geht zurück zum Hauptmenü bei Klick auf die Zurück-Taste

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextChange(String newText){

                ArrayList<String> templist = new ArrayList<>();

                for(String temp : guestList){

                    if (temp.toLowerCase().contains(newText.toLowerCase())){

                        templist.add(temp);

                    }

                }

                ArrayAdapter<String> queryAdapter = new ArrayAdapter<>(GaesteListe.this, R.layout.simple_list, templist);
                listLV.setAdapter(queryAdapter);

                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {

        vielleichtTV = (TextView)findViewById(R.id.vielleichtTextView);
        jaTV = (TextView)findViewById(R.id.jaTextView);
        neinTV = (TextView)findViewById(R.id.neinTextView);
        neutralTV = (TextView)findViewById(R.id.neutralTextView);

        if (v.getId() == R.id.deleteAllButton) {

            AlertDialog.Builder mainbuilder = new AlertDialog.Builder(this);
            mainbuilder.setMessage(R.string.alles_loeschen);
            mainbuilder.setCancelable(true);

            mainbuilder.setPositiveButton(R.string.ja, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    guestList.clear();
                    adapter.notifyDataSetChanged();
                    deleteMode = true;
                    checkDeleteMode();
                    loadItemStatus();
                    //löscht alle Gäste von der Liste und aktualisiert den Adapter und die ListView
                    stream();
                }
            });

            mainbuilder.setNegativeButton(R.string.nein, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog maindialog = mainbuilder.create();
            maindialog.show();

            //erstellt einen Bist du dir sicher? Dialog vor dem Löschen aller Gäste

            loadItemStatus();

        }
        else{

            checkDeleteMode();

        }

        loadItemStatus();

    }

    public void checkDeleteMode(){

        if (!deleteMode){

            deleteMode = true;
            aufgabenTV.setText(getString(R.string.welches_loeschen));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                aufgabenTV.setTextColor(getColor(R.color.gaestListRed));

            }

            View view = this.getCurrentFocus();

            if (view != null) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            }

            deleteSingleBTN.setText(getString(R.string.namen_hinzufuegen));
            editET.setClickable(false);
            editET.setVisibility(View.INVISIBLE);

        }
        else{

            deleteMode = false;
            aufgabenTV.setText(getString(R.string.gib_namen_gast));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                aufgabenTV.setTextColor(getColor(R.color.colorLightBlue700));

            }

            deleteSingleBTN.setText(getString(R.string.namen_loeschen));
            editET.setClickable(true);
            editET.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        String item = guestList.get(position);

        if (!deleteMode) {

            checkItemStatus(view, position);

            stream();
            loadItemStatus();

        }
        else{

            AlertDialog.Builder mainbuilder = new AlertDialog.Builder(this);
            mainbuilder.setMessage(getString(R.string.gast_loeschen1) + " " + '"' + item.replace("\n", "")
                    .replace("  ", "")
                    .replace("|Vielleicht|", "")
                    .replace("|Nein|", "")
                    .replace("|Ja|", "") + '"' + " " + getString(R.string.gast_loeschen2));
            mainbuilder.setCancelable(true);

            mainbuilder.setPositiveButton(R.string.ja, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    guestList.remove(position);
                    adapter.notifyDataSetChanged();
                    loadItemStatus();
                    //entfernt das angelickte Item und aktualiesiert den Adapter und die ListView
                }
            });

            mainbuilder.setNegativeButton(R.string.nein, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog maindialog = mainbuilder.create();
            maindialog.show();

            stream();
            loadItemStatus();

        }

        loadItemStatus();
    }

    public void checkItemStatus(View view, final int position){

        String item = guestList.get(position);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (item.contains("|Nein|")){

                guestList.set(position, item.replace("|Nein|", "").replace("\n", "").replace("  ", "") + "\n");
                adapter.notifyDataSetChanged();

            }
            else if (item.contains("|Vielleicht|")) {

                guestList.set(position, item.replace("|Vielleicht|", "|Ja|"));
                adapter.notifyDataSetChanged();

            }
            else if (item.contains("|Ja|")) {

                guestList.set(position, item.replace("|Ja|", "|Nein|"));
                adapter.notifyDataSetChanged();

            }
            else {

                guestList.set(position, item + "|Vielleicht|");
                adapter.notifyDataSetChanged();

            }

        }

    }

    public void setCounter(int jaSize, int neinSize, int vielleichtSize, int neutralSize){

        int size = guestList.size();

        infoFree = jaSize;
        infoTotal = size;

        vielleichtTV.setText(vielleichtSize+"/" + size + "\nVielleicht");
        jaTV.setText(jaSize+"/" + size+ "\nZusagen");
        neinTV.setText(neinSize+"/" + size+"\nAbsagen");
        neutralTV.setText(neutralSize+"/" + size+"\nAusstehend");


    }

    public void loadItemStatus(){

        int     c = 0;
        int     jaSize = 0;
        int     neinSize = 0;
        int     vielleichtSize = 0;
        int     neutralSize = 0;

        String  item;

        while (c < guestList.size()) {

            item = guestList.get(c);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (item.contains("|Nein|")) {

                    neinSize++;

                }
                else if (item.contains("|Vielleicht|")) {

                    vielleichtSize++;

                }
                else if (item.contains("|Ja|")) {

                    jaSize++;

                }
                else {

                    neutralSize++;

                }

            }

            c++;

        }

        setCounter(jaSize, neinSize, vielleichtSize, neutralSize);
    }

    public void addName(String name){

        guestList.add(name);
        adapter.notifyDataSetChanged();
        stream();

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_ENTER){

            if (event.getAction() == KeyEvent.ACTION_DOWN) {

                if (editET.getText().toString().replace("\n", "").replace(" ", "").length() > 0){

                    String text = editET.getText().toString().replace("\n", "").replace("  ", "").replace("|","") + "\n";
                    addName(text);

                }
                else {

                    Toast t = Toast.makeText(getApplicationContext(), getString(R.string.gib_etwas_ein), Toast.LENGTH_LONG);
                    t.show();

                }

            }

            editET.setText("".replace("\n", ""));
            loadItemStatus();

        }
        return false;
    }

    @Override
    public void onPause() {

        super.onPause();
        stream();

    }

}
