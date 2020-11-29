/**
*
* VAIHDA ".sovellus" ohjelman nimeksi
*
* Data muoto: avain, urheilu, ruutuaika, paino, onko syötetty painoa?
*
*/

package com.example.sovellus;

import java.util.Date;

public class dataOlio{

    private final static String LOGTAG = "dataOlio.java";

    private String key;
    private int day;
    private String month,year;
    private int sportTime;
    private int screenTime;
    private double weight;
    private boolean dayWeight;

    private static Date creationDate;

    /** LUOKAN KONSTRUKTORI */
    public dataOlio(String keyName,String y, String m){
        this.key = keyName;
        this.year = y;
        this.month = m;
        this.day = 0;
        this.sportTime = 0;
        this.screenTime = 0;
        this.weight = 0;
        this.dayWeight = false;
        this.creationDate = new Date();
    }

    /** PÄIVÄÄ MUUTTAVA METODI */
    public void changeDay(int d){
        this.day = d;
    }

    /** PÄIVÄN LIIKUNTA AIKA */
    public void insertSport(int time){
        this.sportTime = time;
    }

    /** PÄIVÄN RUUTU AIKA */
    public void insertScreen(int time){
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

    /** PALAUTTAA DATAPAKETIN VUODEN */
    public String getYear(){return this.year;}

    /** PALAUTTAA DATAPAKETIN KUUKAUDEN */
    public String getMonth(){return this.month;}

    /** PALAUTTAA URHEILUAJAN SEKUNTEISSA */
    public int sportSec(){
        return this.sportTime;
    }

    /** PALAUTTAA RUUTUAJAN SEKUNTEISSA */
    public int screenSec(){
        return this.screenTime;
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
    public String getPackageName(){
        return key;
    }

    /** PALAUTTAA PÄIVÄN NUMERO */
    public int getDay(){
        return this.day;
    }
}