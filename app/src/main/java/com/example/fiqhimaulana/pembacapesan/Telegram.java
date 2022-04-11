package com.example.fiqhimaulana.pembacapesan;

/**
 * Created by FIQHI MAULANA.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Telegram  extends Fragment{
    RecyclerView pesan;
    List<ChatModel> listChat = new ArrayList<>();
    ChatAdapter mAdapter;
    DataBase db;

    public Telegram() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.telegram, container, false);
        pesan = (RecyclerView) view.findViewById(R.id.rcv_telegram);
        db = new DataBase(getContext());
        listChat = db.getChatDetails(db.TELGRAM_PACK);
        mAdapter = new ChatAdapter(getActivity().getApplicationContext(),listChat);
        pesan.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        pesan.setItemAnimator(new DefaultItemAnimator());
        pesan.setHasFixedSize(true);
        pesan.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));
        pesan.setAdapter(mAdapter);
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(onNotice,new IntentFilter("Msg"));
        return view;
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String pack = intent.getStringExtra("package");
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String created_at = dateFormat.format(calendar.getTime());

            if(pack.equals("org.telegram.messenger")){
                listChat.add(0,new ChatModel("",title,text,created_at));
                mAdapter.notifyDataSetChanged();
            }
        }
    };
}
