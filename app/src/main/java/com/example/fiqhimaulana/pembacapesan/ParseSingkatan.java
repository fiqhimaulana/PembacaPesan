package com.example.fiqhimaulana.pembacapesan;

import android.content.Context;
import android.text.TextUtils;
import java.util.List;

/**
 * Created by FIQHI MAULANA.
 */

public class ParseSingkatan {
    DataBase db;
    List<KataSingkatan> kataSingkatan;
    public ParseSingkatan(Context context){
        db = new DataBase(context);
        kataSingkatan = db.getKata();
    }
    public String parseSingkatan(String words){
        String[] s = words.split("\\s+");
        for ( int i = 0; i < s.length; i++) {
            s[i].replaceAll("[^a-zA-Z0-9]", "");
        }
        String parsed = TextUtils.join(" ",s);
        return parsed;
    }

    public String parseDatabase(String words){
        kataSingkatan = db.getKata();
        String[] s = words.split("\\s+");
        for (int i =0; i< s.length;i++){
            for (KataSingkatan kata: kataSingkatan) {
                if (s[i].equals(kata.getSingkatan())){
                    s[i] = kata.getKepanjangan();
                }
            }
        }

        String parsed = TextUtils.join(" ",s);
        return parsed;
    }
}