package Classes;

import java.util.Date;

/**
 *
 *
 * @author Eljas Hirvelä;
 */
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

    /** Luokan konstruktori. */
    /**
     * Oliota luodessa otetaan "keyName", joka on sama kuin datapaketin nimi missä oliota säilytetään, sekä
     *  datapaketin päivämääriä vastaavan vuoden ja kuukauden.
     */
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

    /** Päivää muuttava metodi */
    public void changeDay(int d){
        this.day = d;
    }

    /** Päivän liikunta aika */
    public void insertSport(int time){
        this.sportTime = time;
    }

    /** Päivän ruutu aika */
    public void insertScreen(int time){
        this.screenTime = time;
    }

    /** Päivän paino */
    public void insertWeight(double inWeight){
        this.weight = inWeight;
    }

    /** Painon booleanin vaihto */
    public void insertWeightBoolean(boolean t){
        this.dayWeight = t;
    }

    /** Palauttaa datapaketin vuoden */
    public String getYear(){return this.year;}

    /** Palauttaa olion kuukauden */
    public String getMonth(){return this.month;}

    /** Palauttaa urheiluajan sekunteissa */
    public int sportSec(){
        return this.sportTime;
    }

    /** Palauttaa ruutuajan sekunteissa */
    public int screenSec(){
        return this.screenTime;
    }

    /** Boolean onko päivälle syötetty painoa */
    public boolean hasWeight(){
        return this.dayWeight;
    }

    /** Palauttaa painon boolean arvon */
    public double returnWeight(){
        return this.weight;
    }

    /** Palauttaa olion luomis päivämäärän */
    public String getPackageName(){
        return this.key;
    }

    /** Palauttaa päivän numeron */
    public int getDay(){
        return this.day;
    }
}