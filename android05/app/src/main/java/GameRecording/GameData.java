package GameRecording;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class GameData implements Serializable {
    public String gameName;
    public Calendar gameDate;
    public ArrayList<String> gameMoves;
    public GameData(String name, ArrayList<String> moves){
        this.gameName=name;
        this.gameDate=Calendar.getInstance();
        this.gameMoves=moves;
    }

    public String getGameName(){
        return this.gameName;
    }

    public Calendar getGameDate() {
        return this.gameDate;
    }

    public  ArrayList<String> getMoves(){
        return this.gameMoves;
    }

    public String toString(){
        return this.getGameName()+ " | " + this.gameDate.getTime().toString();
    }
}
