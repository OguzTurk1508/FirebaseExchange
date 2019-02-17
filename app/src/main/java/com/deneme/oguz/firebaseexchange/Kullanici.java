package com.deneme.oguz.firebaseexchange;

public class Kullanici {

    String ad;
    String sifre;
    float TRY,USD,EUR,GBP;

    public Kullanici(){
        ad="";
        sifre="";
        TRY=1000;
        EUR=100;
        USD=100;
        GBP=100;
    }

    public Kullanici(String id , String pw){
        ad=id;
        sifre=pw;
        TRY=1000;
        EUR=100;
        USD=100;
        GBP=100;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public float getTRY() {
        return TRY;
    }

    public void setTRY(float TRY) {
        this.TRY = TRY;
    }

    public float getUSD() {
        return USD;
    }

    public void setUSD(float USD) {
        this.USD = USD;
    }

    public float getEUR() {
        return EUR;
    }

    public void setEUR(float EUR) {
        this.EUR = EUR;
    }

    public float getGBP() {
        return GBP;
    }

    public void setGBP(float GBP) {
        this.GBP = GBP;
    }
}
