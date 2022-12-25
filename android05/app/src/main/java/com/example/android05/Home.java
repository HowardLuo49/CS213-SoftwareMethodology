package com.example.android05;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;

import GameRecording.GameData;
import GameRecording.LoadGameData;


public class Home extends AppCompatActivity {

    private Button startGameButton;
    private ListView gamesListView;
    private Button sortByDate;
    private Button sortByName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        startGameButton = (Button) findViewById(R.id.startGame);
        gamesListView = (ListView) findViewById(R.id.games);
        sortByDate = (Button) findViewById(R.id.sortByDate);
        sortByName = (Button) findViewById(R.id.sortByName);

        LoadGameData.c = getApplicationContext();
        LoadGameData.loadGame();

        if(LoadGameData.list != null) {
            String[] gameNameList = new String[LoadGameData.list.size()];

            for (int i = 0; i <  LoadGameData.list.size(); i++) {
                gameNameList[i] = LoadGameData.list.get(i).toString();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, gameNameList);
            gamesListView.setAdapter(adapter);
        }

        sortByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(LoadGameData.list != null) {
                    Collections.sort(LoadGameData.list, new dateSort());
                    String[] temp = new String[LoadGameData.list.size()];
                    for (int i = 0; i< LoadGameData.list.size(); i++) {
                        temp[i] = LoadGameData.list.get(i).toString();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Home.this, android.R.layout.simple_list_item_1, temp);
                    gamesListView.setAdapter(adapter);
                }
                else{
                    Toast.makeText(Home.this,"No saved games",Toast.LENGTH_LONG).show();
                }
            }
        });

        sortByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(LoadGameData.list != null) {
                    Collections.sort(LoadGameData.list, new nameSort());
                    String[] temp = new String[LoadGameData.list.size()];
                    for (int i = 0; i< LoadGameData.list.size(); i++) {
                        temp[i] = LoadGameData.list.get(i).toString();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Home.this, android.R.layout.simple_list_item_1, temp);
                    gamesListView.setAdapter(adapter);
                }
                else{
                    Toast.makeText(Home.this,"No saved games",Toast.LENGTH_LONG).show();
                }
            }
        });

        gamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GameData gameData= LoadGameData.list.get(i);
                Intent intent=new Intent(Home.this, Replay.class);
                intent.putExtra("moves",gameData.getMoves());
                startActivity(intent);
            }
        });

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Home.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    class nameSort implements Comparator<GameData> {
        public int compare(GameData a, GameData b) {
            if(a != null && b != null)
                return a.gameName.compareToIgnoreCase(b.gameName);
            return 0;
        }
    }

    class dateSort implements Comparator<GameData> {
        public int compare(GameData a, GameData b) {
            if(a != null && b != null)
                return b.gameDate.compareTo(a.gameDate);
            return 0;
        }
    }
}