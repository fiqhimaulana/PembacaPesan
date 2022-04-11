package com.example.fiqhimaulana.pembacapesan;

/**
 * Created by FIQHI MAULANA.
 */

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NotificationService extends NotificationListenerService implements TextToSpeech.OnInitListener {

    public static TextToSpeech getTts() {
        return tts;
    }
    public void setTts(TextToSpeech tts) {
        this.tts = tts;
    }

    DataBase db;
    public static TextToSpeech tts;
    private String mPreviousNotificationKey = "";
    private Notification mPreviousNotification = null;
    private PreferenceManager prefManager;

    Context context;

    boolean bicara = false;

    @Override
    public void onCreate() {
        super.onCreate();
        //inisialisasi context, penghubung service dengen context
        context = getApplicationContext();
        //inisialisasi text to speech
        tts = new TextToSpeech(context,this);

        Log.d("NOTIFICASERV","On Created " + bicara);

        db = new DataBase(context);
        prefManager = new PreferenceManager(context);
    }

    public void onNotificationPosted(StatusBarNotification sbn) {
        if(bicara){
            bicara = false;
        } else {
            Log.i("NotifyService", "got notification");
            String pack = sbn.getPackageName();
            Bundle extras = sbn.getNotification().extras;
            String title = extras.getString("android.title");
            String text = extras.getString("android.text");
            mPreviousNotificationKey = sbn.getKey();
            mPreviousNotification = sbn.getNotification();

           if (pack.equals("org.telegram.messenger")|| pack.equals("com.whatsapp")){
                Intent msgrcv = new Intent("Msg");
                msgrcv.putExtra("package", pack);
                msgrcv.putExtra("title", title);
                msgrcv.putExtra("text", text);
                Calendar calendar = Calendar.getInstance();
               SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
               String created_at = dateFormat.format(calendar.getTime());
               db.addChat(title,text,pack, created_at);

                if(tts != null && prefManager.isTtsOn()){
                    ParseSingkatan parser = new ParseSingkatan(context);
                    text = parser.parseSingkatan(text);
                    text = parser.parseDatabase(text);
                    tts.speak("Pengirim,"+title+". "+text,TextToSpeech.QUEUE_ADD,null);
                }
                LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
                bicara = true;
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        tts.stop();
        Log.i("Msg","Notification Removed");
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS){
            tts.setLanguage(new Locale("id","ID"));
        } else {
            Toast.makeText(context,"Inisialisasi TTS tidak berhasil",Toast.LENGTH_LONG).show();
        }
    }
}