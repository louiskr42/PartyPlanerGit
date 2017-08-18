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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ToDosList extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener, AdapterView.OnItemClickListener {

    ListView aufgabenLV;
    EditText aufgabenET;
    TextView aufgabenTV;
    Button btn;

    int i = 1;

    ArrayList<String> listItems;
    ArrayAdapter<String> aufgabenPersonenAdapter;

    SharedPreferences mainPref, subPref;
    SharedPreferences.Editor mainPrefEditor, subPrefEditor;

    File file;
    //Datei, in der die Liste gespeichert wird
    ObjectInputStream inputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_aufgabenverteilung);

        btn = (Button) findViewById(R.id.deleteAllButton);
        btn.setOnClickListener(this);
        //Button initialisieren

        aufgabenET = (EditText) findViewById(R.id.aufgabenEditText);
        aufgabenET.setOnKeyListener(this);
        //EditText initialisieren und Text einstellen

        aufgabenLV = (ListView) findViewById(R.id.aufgabenListView);
        aufgabenLV.setOnItemClickListener(this);
        //ListView initialisieren

        aufgabenTV = (TextView) findViewById(R.id.aufgabenTextView);
        //TextView initialisieren

        mainPref = this.getSharedPreferences("Aufgaben", MODE_PRIVATE);
        mainPrefEditor = mainPref.edit();

        subPref = this.getSharedPreferences("Personen", MODE_PRIVATE);
        subPrefEditor = subPref.edit();
        //SharedPreferences initialisieren



        file = new File(getDir("dataItems", MODE_PRIVATE), "list");
        try {
            inputStream = new ObjectInputStream(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ObjectInputstream, zum Abrufen der in file gespeicherten Aufgaben initialisieren

        if (inputStream != null) {
            try {
                listItems = (ArrayList<String>) inputStream.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            listItems = new ArrayList<>();
        }
        //Wenn bereits Aufgaben gespeichert sind, lädt er diese. Wenn nicht erstellt er eine neue Liste

        aufgabenPersonenAdapter = new ArrayAdapter<String>(this, R.layout.simple_list, listItems);

        aufgabenLV.setAdapter(aufgabenPersonenAdapter);
    }

    public void sortList(String aufgabe, String person){
        listItems.add(aufgabe.replace("\n", "") + "\n" + "("+ person.replace("\n", "") + ")");
        aufgabenPersonenAdapter.notifyDataSetChanged();
        aufgabenLV.setAdapter(aufgabenPersonenAdapter);
        aufgabenET.setText(null);
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (aufgabenET.getText().toString().replace("\n", "").replace(" ", "").length() > 0) {
                    if (i == 2) {
                        String text = aufgabenET.getText().toString().replace("\n", "").replace("  ", "");
                        subPrefEditor.putString("personen", text);
                        subPrefEditor.commit();
                        aufgabenET.setText(null);
                        //speichert die Details der Aufgabe in den SharedPreferences

                        String aufgabe = mainPref.getString("aufgaben", "");
                        String person = subPref.getString("personen", "");
                        sortList(aufgabe, person);
                        //leitet die eingegebenen Information an sortList weiter

                        mainPrefEditor.clear();
                        mainPrefEditor.commit();

                        subPrefEditor.clear();
                        subPrefEditor.commit();
                        //löscht die SharedPreferences

                        aufgabenTV.setText(getString(R.string.gib_namen_der_aufgabe));

                        i = 1;
                        //setzt i auf 1, damit die nächste Eingabe den Namen zugeordnet wird

                    } else if (i == 1) {
                        String text = aufgabenET.getText().toString().replace("\n", "").replace("  ", "");
                        mainPrefEditor.putString("aufgaben", text);
                        mainPrefEditor.commit();
                        aufgabenET.setText(null);
                        //speichert den Namen der Aufgabe in der SharedPreferences

                        aufgabenTV.setText(getString(R.string.gib_details_aufgabe));

                        i++;
                        //addiert 1 zu i, damit die nächste Eingabe den Details zugeordnet wird
                    }
                } else {
                    Toast t = Toast.makeText(getApplicationContext(), getString(R.string.gib_etwas_ein), Toast.LENGTH_LONG);
                    t.show();
                }
            }
            aufgabenET.setText("".replace("\n", ""));
        }
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        stream();
        //lässt stream laufen, wenn die Activity pausiert oder beendet wird
    }

    public void stream() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(listItems);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //speichert die Liste über den ObjectOutputStream in file
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

        } else {

        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainMenu.class));
        finish();
        //beendet die Activity und geht zurück zum Hauptmenü bei Klick auf die Zurück-Taste
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        AlertDialog.Builder mainbuilder = new AlertDialog.Builder(this);
        mainbuilder.setMessage(R.string.willst_du_aufgabe_loeschen);
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

                for(String temp : listItems){
                    if (temp.toLowerCase().contains(newText.toLowerCase())){
                        templist.add(temp);
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(ToDosList.this, R.layout.simple_list, templist);
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

}