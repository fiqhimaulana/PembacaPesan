package com.example.fiqhimaulana.pembacapesan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by FIQHI MAULANA.
 */

public class DataBase extends SQLiteAssetHelper {
    private static final String TAG = DataBase.class.getSimpleName();
    public static final String TELGRAM_PACK = "org.telegram.messenger";
    public static final String WHATSAPP_PACK="com.whatsapp";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "autospeech.db";

    // Table chat
    private static final String TABLE_CHAT = "chat";

    //table singkatan
    private static final String TABLE_SINGKATAN = "singkatan";

    // kolom chat
    private static final String KEY_ID = "_id";
    private static final String KEY_PENIRIM = "pengirim";
    private static final String KEY_PESAN = "pesan";
    private static final String KEY_PACKAGE = "pack";
    private static final String KEY_DATETIME = "created_at";

    //kolom singkatan
    private static final String KEY_IDS = "_id";
    private static final String KEY_SINGKATAN = "word";
    private static final String KEY_KEPANJANGAN = "meaning";

    public DataBase(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void addChat( String pengirm,String pesan,String pack,String datetime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues chat = new ContentValues();
        chat.put(KEY_PENIRIM,pengirm);
        chat.put(KEY_PESAN,pesan);
        chat.put(KEY_PACKAGE,pack);
        chat.put(KEY_DATETIME,datetime);

        long id = db.insert(TABLE_CHAT,null,chat);
        db.close();

        Log.d(TAG, "Chat baru masuk ke database " + id);
    }
    /*
    * tambahkan singkatan
    * */
    public void addKata (String singkatan, String kepanjangan){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues kata = new ContentValues();
        kata.put(KEY_SINGKATAN, singkatan);
        kata.put(KEY_KEPANJANGAN, kepanjangan);

        long kta = db.insert(TABLE_SINGKATAN,null,kata);
        db.close();
        Log.d(TAG,"kata yang masuk " + kta);
    }

    public List<ChatModel> getChatDetails(String pack) {
        HashMap<String, String> chat = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM " + TABLE_CHAT + " WHERE " +KEY_PACKAGE + " = '" + pack+ "' ORDER BY " + KEY_ID+ " DESC" ;

        List<ChatModel> chatList = new ArrayList<>();
        ChatModel model;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        // Move to first row
        cursor.moveToFirst();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            // do what you need with the cursor here
            model = new ChatModel(String.valueOf(cursor.getLong(0)),cursor.getString(1),cursor.getString(2),cursor.getString(4));

            chatList.add(model);
        }

        cursor.close();
        db.close();
        // return chat
        Log.d(TAG, "Mengambil chat dari sqlite: " + chatList.toString());

        return chatList;
    }

    public List<KataSingkatan> getKata(){
        HashMap<String, String> singkatan = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM " + TABLE_SINGKATAN ;
        List<KataSingkatan> kataSingkatanList = new ArrayList<>();
        KataSingkatan model;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        cursor.moveToFirst();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            model = new KataSingkatan(String.valueOf(cursor.getLong(0)),cursor.getString(1),cursor.getString(2));

            kataSingkatanList.add(model);
        }
        cursor.close();
        db.close();
        Log.d(TAG, "Mengambil kata dari sqlite: " + kataSingkatanList.toString());
        return kataSingkatanList;
    }

    public void deleteChat(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_CHAT, null, null);
        db.close();
        Log.d(TAG, "Deleted all chat info from sqlite");
    }

    public void deleteChatById(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete Id
        db.delete(TABLE_CHAT,KEY_ID + " = ?",new String[]{id});
        db.close();
        Log.d(TAG,"Delete Chat Id"+id);
    }

    public void deleteChatByPack(String pack) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete Pack
        db.delete(TABLE_CHAT,KEY_PACKAGE + " = ?",new String[]{pack});
        db.close();
        Log.d(TAG,"Delete Chat Pack"+pack);
    }
}
