package kg.geektech.taskapp35;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private SharedPreferences preferences;
    private String image;
    private String name;


    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Prefs(Context context) {
        preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    public void saveBoardState(){
        preferences.edit().putBoolean("isBoardShow", true).apply();
    }

    public boolean isBoardShow() {
        return preferences.getBoolean("isBoardShow",false);
    }

    public void saveImage(){
        preferences.edit().putString("imageAva", image).apply();

    }

    public String getStringImage(){
        return preferences.getString("imageAva","");
    }

    public void saveName(){
        preferences.edit().putString("nameAcc", name).apply();
    }
    public String isName(){
        return  preferences.getString("nameAcc","Fomin Ilya");
    }

}
