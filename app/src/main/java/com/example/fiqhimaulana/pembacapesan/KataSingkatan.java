package com.example.fiqhimaulana.pembacapesan;

/**
 * Created by FIQHI MAULANA.
 */

public class KataSingkatan {
    String ids ;
    String singkatan;
    String kepanjangan;

    public KataSingkatan(){
    }

    public KataSingkatan (String id, String singkatan, String kepanjangan){
        this.ids = ids;
        this.singkatan = singkatan ;
        this.kepanjangan = kepanjangan;
    }

    public String getIds(){
        return ids;
    }
    public void  setIds(String ids){
        this.ids = ids;
    }

    public String getSingkatan(){
        return singkatan;
    }
    public void setSingkatan(String singkatan){
        this.singkatan = singkatan;
    }

    public String getKepanjangan(){
        return kepanjangan;
    }
    public void setKepanjangan(String kepanjangan){
        this.kepanjangan =kepanjangan;
    }
}
