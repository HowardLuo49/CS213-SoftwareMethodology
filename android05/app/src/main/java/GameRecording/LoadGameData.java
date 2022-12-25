package GameRecording;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class LoadGameData implements Serializable {
    public static ArrayList<GameData> list;
    public static Context c;

    public static void loadGame() {
        File f = new File(c.getFilesDir(), "data.dat");
        if (f.exists()) {
            try {
                FileInputStream fileInputStream = c.openFileInput("data.dat");
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                list = (ArrayList<GameData>) objectInputStream.readObject();
                objectInputStream.close();
                fileInputStream.close();
            } catch(Exception e){
                e.printStackTrace();
            }
        } else {
            list = new ArrayList<GameData>();
        }
    }

    public static void saveGame() {
        try {
            FileOutputStream fileOutputStream = c.openFileOutput("data.dat",0);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(list);
            objectOutputStream.close();
            fileOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

