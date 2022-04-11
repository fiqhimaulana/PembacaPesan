package com.example.fiqhimaulana.pembacapesan;

/**
 * Created by FIQHI MAULANA.
 */

public class ChatModel {
    String id;
    String pengirim;
    String pesan;
    String datetime;

    public ChatModel(){};
    public ChatModel(String id, String pengirim, String pesan, String datetime) {
        this.id = id;
        this.pengirim = pengirim;
        this.pesan = pesan;
        this.datetime = datetime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPengirim() {
        return pengirim;
    }

    public void setPengirim(String pengirim) {
        this.pengirim = pengirim;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}