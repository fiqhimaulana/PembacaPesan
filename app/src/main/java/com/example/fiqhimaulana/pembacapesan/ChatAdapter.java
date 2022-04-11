package com.example.fiqhimaulana.pembacapesan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.List;
import static com.example.fiqhimaulana.pembacapesan.NotificationService.tts;

/**
 * Created by FIQHI MAULANA.
 */

public class ChatAdapter extends android.support.v7.widget.RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context context;
    private List <ChatModel> daftarchat;
    TextToSpeech textToSpeech;
    DataBase db;
    List<KataSingkatan> kataSingkaten;
    ParseSingkatan parser;

    public ChatAdapter(Context context,List<ChatModel>daftarchat){
        this.context=context;
        this.daftarchat=daftarchat;

        textToSpeech = NotificationService.getTts();

        db = new DataBase(context);
        kataSingkaten = db.getKata();
        parser = new ParseSingkatan(context);
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View list = inflater.inflate(R.layout.barischat,parent,false);

        ViewHolder viewHolder = new ViewHolder(list);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final ChatModel model = daftarchat.get(position);
        holder.pengirim.setText(model.getPengirim());
        holder.pesan.setText(model.getPesan());
        holder.datetime.setText(model.getDatetime());

        String parsed = parser.parseSingkatan(model.getPesan());
        parsed = parser.parseDatabase(parsed);
        final String finalParsed = parsed;
        holder.btnSuara.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              tts.speak(finalParsed,textToSpeech.QUEUE_ADD,null) ;
          }
      });

       holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {


            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle("Konfirmasi");
                alert.setMessage("Anda akan menghapus pesan ini ?");
                alert.setIcon(R.drawable.ic_warning);

                final AlertDialog.Builder yes = alert.setPositiveButton("HAPUS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        db.deleteChatById(model.getId());
                        daftarchat.remove(position);
                        notifyDataSetChanged();
                    }
                });
                alert.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return daftarchat.size(); //jumlah data yang ditempilkan di RecyclerView
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView pengirim;
        TextView pesan;
        ImageButton btnSuara;
        TextView datetime;

        public ViewHolder(View itemView) {
            super(itemView);
            pengirim = (TextView) itemView.findViewById(R.id.text_pengirim);
            pesan   = (TextView) itemView.findViewById(R.id.text_pesan);
            datetime = (TextView) itemView.findViewById(R.id.txt_datetime);
            btnSuara = (ImageButton)itemView.findViewById(R.id.btnBunyi);
    }
    }
}