package com.example.android05;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import GameRecording.GameData;
import GameRecording.LoadGameData;

public class SaveGame extends AppCompatActivity {

    private Button save;
    private EditText gameName;
    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_game);

        list = new ArrayList<>();
        save = (Button) findViewById(R.id.save);
        gameName = (EditText) findViewById(R.id.gameName);

        Intent intent = getIntent();
        list=intent.getStringArrayListExtra("moves");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(gameName.getText().toString() == null || gameName.getText().toString().length()==0){
                    Toast.makeText(SaveGame.this,"Please give the game a name",Toast.LENGTH_LONG).show();
                    return;
                }

                GameData newGameData = new GameData(gameName.getText().toString(),list);
                if(LoadGameData.list == null)
                    LoadGameData.list = new ArrayList<GameData>();
                LoadGameData.list.add(newGameData);
                LoadGameData.saveGame();

                Intent intent = new Intent(SaveGame.this, Home.class);
                startActivity(intent);
            }
        });
    }
}
