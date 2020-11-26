/**
*
* VAIHDA ".sovellus" ohjelman nimeksi
*
* Data muoto: avain, urheilu, ruutuaika, paino, onko syötetty painoa?
*
*/

package com.example.sovellus;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Math.round;

public class dataOlio{

    private final static String LOGTAG = "dataOlio.java";

    private String currentDate;
    private String key;
    private int day;
    private double sportTime;
    private double screenTime;
    private double weight;
    private boolean dayWeight;

    private static Date creationDate;

    /** LUOKAN KONSTRUKTORI */
    public dataOlio(){
        this.key = getDate();
        this.day = 0;
        this.sportTime = 0;
        this.screenTime = 0;
        this.weight = 0;
        this.dayWeight = false;
        this.creationDate = new Date();
    }

    /** LUOKAN PARAMETRINEN KONSTRUKTORI */
    public dataOlio(String k, int d, double sport, double screen, double weight, boolean day){
        this.key = k;
        this.day = d;
        this.sportTime = sport;
        this.screenTime = screen;
        this.weight = weight;
        this.dayWeight = day;
        this.creationDate = new Date();
    }

    /** PÄIVÄÄ MUUTTAVA METODI */
    public void changeDay(int d){
        this.day = d;
    }

    /** PÄIVÄN LIIKUNTA AIKA */
    public void insertSport(double time){
        this.sportTime = time;
    }

    /** PÄIVÄN RUUTU AIKA */
    public void insertScreen(double time){
        this.screenTime = time;
    }

    /** PÄIVÄN PAINO */
    public void insertWeight(double inWeight){
        this.weight = inWeight;
    }

    /** MUUTA BOOLEANIA */
    public void insertWeightBoolean(boolean t){
        this.dayWeight = t;
    }

    /** BOOLEAN ONKO PÄIVÄLLE SYÖTETTY ERIKSEEN PAINOA */
    public boolean hasWeight(){
        return this.dayWeight;
    }

    /** PALAUTTAA PAINON DOUBLE ARVONA */
    public double returnWeight(){
        return this.weight;
    }

    /** PALAUTTAA OLION LUOMIS PÄIVÄMÄÄRÄN JOKA TOIMII MYÖS OLION AVAIMENA */
    public String getSaveDate(){
        return key;
    }

    /** PALAUTTAA PÄIVÄMÄÄRÄN KOKONAISLUKUNA */
    public int getDateInt(){
        currentDate = key.replace("/","");
        //Log.d(LOGTAG,currentDate);
        return Integer.parseInt(currentDate);
    }

    /** PALAUTTAA PÄIVÄN NUMERO */
    public int getDay(){
        return this.day;
    }

    /** OLION SISÄISEEN KÄYTTÖÖN TARKOITETTU PÄIVÄMÄÄRÄN ASETTAMIS METODI */
    private String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        currentDate = formatter.format(date);
        return currentDate;
    }
}