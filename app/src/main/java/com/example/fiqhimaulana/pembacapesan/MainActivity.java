package com.example.fiqhimaulana.pembacapesan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PreferenceManager prefManager;

    List<ChatModel> listChat = new ArrayList<>();
    ChatAdapter mAdapter;

    DataBase db;
    TableLayout tab;

    private int[] tabIcons = {
            R.mipmap.icon_telegram,
            R.mipmap.icon_whatsapp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmain);
        prefManager = new PreferenceManager(this);

        db = new DataBase(getApplicationContext());
        mAdapter = new ChatAdapter(getApplicationContext(),listChat);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setTabIcons();
        tab = (TableLayout) findViewById(R.id.tab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.context_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.exitg:
                Toast.makeText(this, "Keluar dari Pembaca Pesan", Toast.LENGTH_SHORT).show();
                try {
                    // clearing app data
                    String packageName = getApplicationContext().getPackageName();
                    Runtime runtime = Runtime.getRuntime();
                    runtime.exec("pm clear "+packageName);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.notifsetting:
                startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                break;
            case R.id.ttson:
                prefManager.setIsTtsOn(true);
                Toast.makeText(this, "Text-to-Speech Aktif", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ttsoff:
                prefManager.setIsTtsOn(false);
                Toast.makeText(this, "Text-to-Speech Tidak Aktif", Toast.LENGTH_SHORT).show();
                break;
            case R.id.hapus_pesanW:
                AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
                alert1.setTitle("Konfirmasi");
                alert1.setMessage("Anda akan menghapus semua pesan WhatsApp ?");
                alert1.setIcon(R.drawable.ic_warning);
                alert1.setPositiveButton("HAPUS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteChatByPack(db.WHATSAPP_PACK);
                        listChat = new ArrayList<ChatModel>();
                        mAdapter.notifyDataSetChanged();
                        setupViewPager(viewPager);
                        setTabIcons();
                    }
                });
                alert1.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert1.create();
                alert1.show();
                break;
            case R.id.hapus_pesanT:
                AlertDialog.Builder alertt = new AlertDialog.Builder(this);
                alertt.setTitle("Konfirmasi");
                alertt.setMessage("Anda akan menghapus semua pesan Telegram ?");
                alertt.setIcon(R.drawable.ic_warning);
                alertt.setPositiveButton("HAPUS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteChatByPack(db.TELGRAM_PACK);
                        listChat = new ArrayList<ChatModel>();
                        mAdapter.notifyDataSetChanged();
                        setupViewPager(viewPager);
                        setTabIcons();
                    }
                });
                alertt.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertt.create();
                alertt.show();
                break;
            case R.id.add_database_singkatan:
                AlertDialog.Builder alertdbs = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater(); // mengkonvert view pada xml menjadi java objek
                final View dialogView = inflater.inflate(R.layout.tambah_data_singkatan,null);
                alertdbs.setView(dialogView);
                alertdbs.setCancelable(true);
                alertdbs.setTitle("Masukkan Kata Singkatan");
                alertdbs.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final EditText singkatan = (EditText) dialogView.findViewById(R.id.in_singkatan);
                        final String textsingkatan = singkatan.getText().toString();
                        final EditText kata = (EditText) dialogView.findViewById(R.id.in_kata);
                        final String textkata = kata.getText().toString();
                        db.addKata(textsingkatan,textkata);
                    }
                });

                alertdbs.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertdbs.create();
                alertdbs.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setTabIcons(){
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Telegram(), "Telegram");
        adapter.addFragment(new Whatsapp(), "Whatsapp");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}