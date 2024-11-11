package com.example.english_4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class ViborCategory extends AppCompatActivity {

    private RecyclerView ButtonList;
    private RecyclerView ButtonListUsers;
    private ArrayList<String[]> getCategoryNames;
    private static ArrayList<ListenButtom> listenButtoms = new ArrayList<>();
    private static ArrayList<ListenButtom> listenButtomsUsers = new ArrayList<>();
    private ArrayList<Object[]> listenButtomsUpdate = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vibor_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeData();
        ButtonList = findViewById(R.id.ButtonList);
        ButtonListUsers = findViewById(R.id.ButtonListUsers);
        ListenButtomAdapter listenButtonAdapter = new ListenButtomAdapter(this, listenButtoms, true);
        ListenButtomAdapter listenButtonAdapterUsers = new ListenButtomAdapter(this, listenButtomsUsers, true);
        ButtonList.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        ButtonListUsers.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        ButtonList.setAdapter(listenButtonAdapter);
        ButtonListUsers.setAdapter(listenButtonAdapterUsers);

    }

    public static void clickCheckBox(View view){

        boolean found = true;

        for(int x = 0; x<listenButtoms.size(); x++){
            if (view.getTag().toString() == listenButtoms.get(x).getName()){
                listenButtoms.get(x).setVidor(!listenButtoms.get(x).getVidor());
                found = false;
            }
        }
        if(found){
            for(int x = 0; x<listenButtomsUsers.size(); x++){
                if (view.getTag().toString() == listenButtomsUsers.get(x).getName()){
                    listenButtomsUsers.get(x).setVidor(!listenButtomsUsers.get(x).getVidor());
                }
            }
        }


        Log.println(Log.DEBUG, "clickCheckBox", view.getTag().toString());

    }

    public void buttonOk(View view){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);


        for (int x = 0; x<listenButtoms.size(); x++){
            listenButtomsUpdate.add((new Object[]{listenButtoms.get(x).getName(), listenButtoms.get(x).getVidor()}));
        }
        for (int x = 0; x<listenButtomsUsers.size(); x++){
            listenButtomsUpdate.add((new Object[]{listenButtomsUsers.get(x).getName(), listenButtomsUsers.get(x).getVidor()}));
        }

//        Log.println(Log.DEBUG, "SQL", String.valueOf(listenButtomsUpdate.get(2)[0] +" "+String.valueOf(listenButtomsUpdate.get(2)[1])));

        databaseHelper.onUpgradevibor(listenButtomsUpdate);
        listenButtoms.clear();
        listenButtomsUsers.clear();
        startActivity(new Intent(this, MainActivity.class));

    }

    public void backbottom(View view) {
        finish();
    }

    public void initializeData(){
        listenButtoms.clear();
        listenButtomsUsers.clear();

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        getCategoryNames = databaseHelper.getCategoryNames();
        for (int x = 0; x<getCategoryNames.size(); x++){
            if(Integer.parseInt(getCategoryNames.get(x)[0]) < 60){
                listenButtoms.add(new ListenButtom(getCategoryNames.get(x)[1], getCategoryNames.get(x)[2], getCategoryNames.get(x)[3], Boolean.valueOf(getCategoryNames.get(x)[4])));
            }
            else{
                listenButtomsUsers.add(new ListenButtom(getCategoryNames.get(x)[1], getCategoryNames.get(x)[2], getCategoryNames.get(x)[3], Boolean.valueOf(getCategoryNames.get(x)[4])));
            }
        }

    }

}
