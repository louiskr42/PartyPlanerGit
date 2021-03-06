package com.coreelements.de.partyplaner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

public class Einkaufsliste extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener, AdapterView.OnItemClickListener {

    final static String         bannerID = "ca-app-pub-1814335808278709~4572376197";

    File                        file;

    Button                      deleteAllBTN;
    ListView                    aufgabenLV;
    EditText                    aufgabenET;
    TextView                    aufgabenTV;

    ArrayList<String>           listItems;
    ArrayAdapter<String>        aufgabenPersonenAdapter;

    ObjectInputStream           inputStream;

    SharedPreferences           infoShopping;
    SharedPreferences.Editor    infoShoppingEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_todos_constraint);

        initializeObjects();

    }

    public void initializeObjects() {

        defineButtons();
        defineFiles();
        defineEditTexts();
        defineListViews();
        defineTextViews();
        defineSharedPreferences();
        loadListAndSetAdapter();
        initializeAdBanner();
        stream();

    }

    public void defineFiles() {

        file = new File(getDir("dataShopping", MODE_PRIVATE), "shoppingList");

    }

    public void defineButtons() {
        deleteAllBTN = (Button) findViewById(R.id.deleteAllButton);
        deleteAllBTN.setOnClickListener(this);
        deleteAllBTN.setText(getString(R.string.alle_artikel_loeschen));
        //Button initialisieren
    }

    public void defineEditTexts() {
        aufgabenET = (EditText) findViewById(R.id.aufgabenEditText);
        aufgabenET.setOnKeyListener(this);
        //EditText initialisieren und Text einstellen
    }

    public void defineTextViews() {
        aufgabenTV = (TextView) findViewById(R.id.aufgabenTextView);
        aufgabenTV.setText(getString(R.string.gib_artikel));
        //TextView initialisieren
    }

    public void defineListViews() {
        aufgabenLV = (ListView) findViewById(R.id.aufgabenListView);
        aufgabenLV.setOnItemClickListener(this);
        //ListView initialisieren
    }

    public void defineSharedPreferences() {
        infoShopping = this.getSharedPreferences("Info", MODE_PRIVATE);
        infoShoppingEditor = infoShopping.edit();
    }

    public void loadListAndSetAdapter() {

        try {

            inputStream = new ObjectInputStream(new FileInputStream(file));

        }
        catch (IOException e) {

            e.printStackTrace();

        }
        //ObjectInputstream, zum Abrufen der in file gespeicherten Aufgaben initialisieren

        if (inputStream != null) {

            try {
                listItems = (ArrayList<String>) inputStream.readObject();
            }
            catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        else {

            listItems = new ArrayList<>();

        }
        //Wenn bereits Aufgaben gespeichert sind, lädt er diese. Wenn nicht erstellt er eine neue Liste

        aufgabenPersonenAdapter = new ArrayAdapter<>(this, R.layout.simple_list, listItems);

        aufgabenLV.setAdapter(aufgabenPersonenAdapter);
    }

    public void initializeAdBanner() {
        MobileAds.initialize(this, bannerID);

        AdView adView = (AdView)findViewById(R.id.adViewTodos);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        adView.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainMenu.class));
        finish();
        //beendet die Activity und geht zurück zum Hauptmenü bei Klick auf die Zurück-Taste
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.deleteAllButton) {
            AlertDialog.Builder mainbuilder = new AlertDialog.Builder(this);
            mainbuilder.setMessage(R.string.alles_loeschen);
            mainbuilder.setCancelable(true);

            mainbuilder.setPositiveButton(R.string.ja, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    listItems.clear();
                    aufgabenPersonenAdapter.notifyDataSetChanged();
                    //löscht den Inhalt der Liste und benachrichtigt den Adapter, damit die ListView aktualisiert wird
                    stream();
                }
            });

            mainbuilder.setNegativeButton(R.string.nein, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog maindialog = mainbuilder.create();
            maindialog.show();

            //erstellt einen Bist du dir sicher? Dialog vor dem Löschen aller Aufgaben
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        String item = listItems.get(position);

        AlertDialog.Builder mainbuilder = new AlertDialog.Builder(this);
        mainbuilder.setMessage(getString(R.string.moechtest_du) + " \"" + item + "\" " + getString(R.string.artikel_loeschen2));
        mainbuilder.setCancelable(true);

        mainbuilder.setPositiveButton(R.string.ja, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                listItems.remove(position);
                aufgabenPersonenAdapter.notifyDataSetChanged();
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
    }

    public void stream() {

        try {

            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(listItems);
            outputStream.flush();
            outputStream.close();

        }
        catch (IOException e) {

            e.printStackTrace();
            Toast.makeText(getApplicationContext(), getString(R.string.speicher_fehler), Toast.LENGTH_LONG).show();

        }
        //speichert die Liste über den ObjectOutputStream in file

        int info = listItems.size();
        infoShoppingEditor.putInt("infoShopping", info);
        infoShoppingEditor.commit();

    }

    public void sortList(String aufgabe) {
        listItems.add(aufgabe.replace("\n", "").replace("  ", ""));
        aufgabenPersonenAdapter.notifyDataSetChanged();
        aufgabenLV.setAdapter(aufgabenPersonenAdapter);
        aufgabenET.setText(null);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_ENTER) {

            if (event.getAction() == KeyEvent.ACTION_DOWN) {

                if (aufgabenET.getText().toString().replace("\n", "").replace(" ", "").length() > 0) {

                    String artikel = aufgabenET.getText().toString().replace("\n", "").replace("  ", "");
                    sortList(artikel);
                    stream();

                }
                else {

                    Toast t = Toast.makeText(getApplicationContext(), getString(R.string.gib_etwas_ein), Toast.LENGTH_LONG);
                    t.show();

                }

            }

            aufgabenET.setText("".replace("\n", ""));

        }

        return false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {

                ArrayList<String> templist = new ArrayList<>();

                for(String temp : listItems){

                    if (temp.toLowerCase().contains(newText.toLowerCase())){

                        templist.add(temp);

                    }

                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Einkaufsliste.this, R.layout.simple_list, templist);
                aufgabenLV.setAdapter(adapter);

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
    public void onPause() {

        super.onPause();
        stream();
        //lässt stream laufen, wenn die Activity pausiert oder beendet wird

    }
}
