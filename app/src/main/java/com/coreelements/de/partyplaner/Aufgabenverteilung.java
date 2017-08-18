package com.coreelements.de.partyplaner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Aufgabenverteilung extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener, AdapterView.OnItemClickListener {

    ListView aufgabenLV;
    EditText aufgabenET;
    Button btn;

    int i = 1;

    HashMap<String, String> aufgabenPersonen;
    List<HashMap<String, String>> listItems;
    SimpleAdapter aufgabenPersonenAdapter;
    Iterator it;

    SharedPreferences mainPref, subPref;
    SharedPreferences.Editor mainPrefEditor, subPrefEditor;

    File file;
    //Datei, in der die HashMap gespeichert wird
    ObjectInputStream inputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_aufgabenverteilung);

        btn = (Button)findViewById(R.id.deleteAllButton);
        btn.setOnClickListener(this);
        //Button initialisieren

        aufgabenET = (EditText)findViewById(R.id.aufgabenEditText);
        aufgabenET.setOnKeyListener(this);
        aufgabenET.setHint(getString(R.string.gib_namen_der_aufgabe));
        //EditText initialisieren und Text einstellen

        aufgabenLV = (ListView)findViewById(R.id.aufgabenListView);
        aufgabenLV.setOnItemClickListener(this);
        //ListView initialisieren

        mainPref = this.getSharedPreferences("Aufgaben", MODE_PRIVATE);
        mainPrefEditor = mainPref.edit();

        subPref = this.getSharedPreferences("Personen", MODE_PRIVATE);
        subPrefEditor = subPref.edit();
        //SharedPreferences initialisieren

        file = new File(getDir("data", MODE_PRIVATE), "map");
        try {
            inputStream = new ObjectInputStream(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ObjectInputstream, zum Abrufen der in file gespeicherten Aufgaben initialisieren

        if (inputStream != null){
            try {
                aufgabenPersonen = (HashMap<String, String>)inputStream.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            aufgabenPersonen = new HashMap<>();
        }
        //Wenn bereits Aufgaben gespeichert sind, lädt er diese. Wenn nicht erstellt er eine neue HashMap

        sortList();
    }




    public void sortList(String main, String sub){

        aufgabenPersonen.put(main, sub);
        //fügt der HashMap die neu eingegebenen Daten hinzu

        listItems = new ArrayList<>();
        /*aufgabenPersonenAdapter = new SimpleAdapter(this, listItems, R.layout.list_item,
                new String[]{"First Line", "Second Line"}, new int[]{R.id.listViewMain, R.id.listViewSub});*/
        //initialisiert den Adapter und definiert, was hinein muss
        it = aufgabenPersonen.entrySet().iterator();
        //initialisiert den Iterator

        while (it.hasNext()){
            HashMap<String, String> resultsMap = new HashMap<>();
            Map.Entry pair = (Map.Entry)it.next();
            resultsMap.put("First Line", pair.getKey().toString());
            resultsMap.put("Second Line", pair.getValue().toString());
            listItems.add(resultsMap);
        }
        //erstellt eine neue HashMap, in der jeweils die Aufgabe und ihre Details zusammengoerdnet werden
        //und fügt das ergebnis dann der Liste hinzu

        aufgabenLV.setAdapter(aufgabenPersonenAdapter);
        //setzt den Adapter auf die ListView
    }

    public void sortList(){
        listItems = new ArrayList<>();
        /*aufgabenPersonenAdapter = new SimpleAdapter(this, listItems, R.layout.list_item,
                new String[]{"First Line", "Second Line"}, new int[]{R.id.listViewMain, R.id.listViewSub});*/
        it = aufgabenPersonen.entrySet().iterator();

        while (it.hasNext()){
            HashMap<String, String> resultsMap = new HashMap<>();
            Map.Entry pair = (Map.Entry)it.next();
            resultsMap.put("First Line", pair.getKey().toString());
            resultsMap.put("Second Line", pair.getValue().toString());
            listItems.add(resultsMap);
        }

        aufgabenLV.setAdapter(aufgabenPersonenAdapter);

        //macht das gleiche, wie das erste sortList() nur ohne einen neuen Eintrag hinzu zu fügen
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER){
            if (event.getAction() == KeyEvent.ACTION_UP) {
                if (aufgabenET.getText().toString().length() != 0) {
                    if (i == 2) {
                        String text = aufgabenET.getText().toString();
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

                        aufgabenET.setHint(getString(R.string.gib_namen_der_aufgabe));

                        i = 1;
                        //setzt i auf 1, damit die nächste Eingabe den Namen zugeordnet wird

                    } else if (i == 1) {
                        String text = aufgabenET.getText().toString();
                        mainPrefEditor.putString("aufgaben", text);
                        mainPrefEditor.commit();
                        aufgabenET.setText(null);
                        //speichert den Namen der Aufgabe in der SharedPreferences

                        aufgabenET.setHint(getString(R.string.gib_details_aufgabe));

                        i++;
                        //addiert 1 zu i, damit die nächste Eingabe den Details zugeordnet wird
                    }
                }else{
                    Toast t = Toast.makeText(getApplicationContext(), getString(R.string.gib_etwas_ein), Toast.LENGTH_LONG );
                    t.show();
                }

                aufgabenET.setText(null);
            }
        }
        return false;
    }

    @Override
    public void onPause(){
        super.onPause();
            stream();
        //lässt stream laufen, wenn die Activity pausiert oder beendet wird
    }

    public void stream() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(aufgabenPersonen);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //speichert die HashMap über den ObjectOutputStream in file
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.deleteAllButton) {
            AlertDialog.Builder mainbuilder = new AlertDialog.Builder(this);
            mainbuilder.setMessage(R.string.alles_loeschen);
            mainbuilder.setCancelable(true);

            mainbuilder.setPositiveButton(R.string.ja, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    aufgabenPersonen.clear();
                    //löscht den Inhalt der HashMap
                    stream();
                    sortList();
                }
            });

            mainbuilder.setNegativeButton(R.string.nein, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog maindialog = mainbuilder.create();
            maindialog.show();

            //erstellt einen Bist du dir sicher? Dialog vor dem Löschen aller Aufgaben

        }else{

        }
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), MainMenu.class));
        finish();
        //beendet die Activity und geht zurück zum Hauptmenü bei Klick auf die Zurück-Taste
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Object item = parent.getItemAtPosition(position);

        TextView tv = (TextView)findViewById(R.id.listViewMain);
        String text = tv.getText().toString();
        int c = 0;

        while (c == 0) {
            if (aufgabenLV.getItemAtPosition(position).toString().contains(text)) {
                aufgabenPersonen.remove(text);
                c++;
            }else{
                position++;
            }
        }

        stream();
        sortList();
    }
}
