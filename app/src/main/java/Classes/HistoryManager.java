package Classes;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * @author Eljas Hirvelä ja Arttu Pösö
 */
public class HistoryManager {

    private int sYear, sMonth, sDay;
    private int eYear, eMonth, eDay;

    private Mega mega;
    private SuperMetodit SM;

    private int daysCombined;

    private final static String LOGTAG = "HistoryManager.java";

    Context context;

    /**
     * Luokan konstruktori ottaa aktiviteetilta kontekstin Context, jotta
     * SharedPreference ja sen toiminnot toimisivat.
     */
    public HistoryManager(Context c){

        this.context = c;
        mega = new Mega(c);
        SM = new SuperMetodit();
    }

    /**
     * Metodilla haetaan sille parametreinä syötetyltä aikaväliltä kaikki tallenteet, jotka palautetaan ArrayList<dataOlio> muodossa.
     * Jos arvot syötetään väärin päin, eli lopetus päivämäärä on ennen aloitus päivämäärä, osaa metodi kääntää arvot.
     */
    public ArrayList<dataOlio> getDayData(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay){

        this.sYear = startYear;
        this.sMonth = startMonth;
        this.sDay = startDay;

        this.eYear = endYear;
        this.eMonth = endMonth;
        this.eDay = endDay;

        //JOS ALOITUS PÄIVÄMÄÄRÄ ON LOPETUS PÄIVÄMÄÄRÄN JÄLKEEN, KÄÄNNETÄÄN ARVOT PÄITTÄÄN "reverse()" FUNKTIOLLA
        if((startYear > endYear) ||(startYear == endYear && startMonth > endMonth)||(startYear == endYear && startMonth == endMonth && startDay > endDay)) {
            reverse();
        }

        //FUNKTIOLLA ETSITÄÄN PÄIVÄT JOTKA PALAUTETAAN
        return calculateDays();
    }

    /**
     * Metodi kääntää aloitus ja lopetus vuoden päin vastoin, jos aloitus vuosi on lopetus vuoden jälkeen.
     */
    private void reverse(){
        //FUNKTIO KÄÄNTÄÄ ALOITUS-LOPETUS PÄIVÄMÄÄRÄT PÄITTÄÄN

        int tmp;

        tmp = this.sYear;
        this.sYear = this.eYear;
        this.eYear = tmp;

        tmp = this.sMonth;
        this.sMonth = this.eMonth;
        this.eMonth = tmp;

        tmp = this.sDay;
        this.sDay = this.eDay;
        this.eDay = tmp;
    }

    /**
     * Metodilla haetaan halutulta aika väliltä päivät ja asetetaan ne olio listaan joka palautetaan.
     */
    private ArrayList<dataOlio> calculateDays(){
        daysCombined = 0;

        int maxDays = 1000;
        int daySearched = 0;

        //dataPackages LISTAAN VARMUUDEN VUOKSI LÖYDETYT DATAPAKETIT MUISTIIN MAHDOLLISTA JATKO KÄYTTÖÄ VARTEN
        ArrayList<String> dataPackages = new ArrayList<>();
        ArrayList<dataOlio> lista;
        ArrayList<dataOlio> returnList = new ArrayList<>();

        int tmpY=this.sYear, tmpM=this.sMonth, tmpD=sDay;

        String stringYear = SM.customDigit(Integer.toString(this.sYear),4);
        String stringMonth =  SM.customDigit(Integer.toString(this.sMonth),2);

        String packageName;

        while((tmpY <= this.eYear && tmpM <= this.eMonth)&&daySearched <= maxDays){

            //JOS WHILE LOOP JOSTAIN SYYSTÄ PETTÄISI NIIN VARMUUDEN VUOKSI
            if(tmpY >= this.eYear && tmpM >= this.eMonth && tmpD >= eDay){
                break;
            }

            //LUODAAN DATAPAKETIN NIMI VUODEN JA KUUKAUDEN PERUSTEELLA HYÖDYNTÄEN SuperMetodit.java
            //LUOKAN FUNKTIOTA
            packageName = SM.createPackageName(stringYear,stringMonth);

            //HAETAAN Mega.java FUNKTION "loadData" AVULLA PÄIVÄMÄÄRÄÄ VASTAAVA DATAPAKETTI
            lista = mega.loadData(stringYear,stringMonth);

            //JOS DATAPAKETTI LÖYTYY, ETSITÄÄN SIITÄ HALUTUN AIKAVÄLIN PÄIVÄT
            if(lista!=null){
                int x;
                if(tmpM == eMonth){
                    x = eDay;
                } else {
                    x = 31;
                }

                //WHILE LOOP KÄY JOKO KOKO KUUKAUDEN LÄPI TAI JOS KYSEESSÄ ON LOPPUMIS KUUKAUSI
                //NIIN ETSITÄÄN VAIN HALUTUT PÄIVÄT
                while(tmpD<=x){
                    Log.d(LOGTAG,"searching for date: "+tmpD+"."+tmpM+"."+tmpY);
                    if(mega.dayFromList(tmpD,lista) != null){
                        Log.d(LOGTAG,"day found!");
                        if(!dataPackages.contains(packageName)){
                            dataPackages.add(packageName);
                        }
                        returnList.add(mega.dayFromList(tmpD,lista));
                        Log.d(LOGTAG,"found: "+tmpD+"."+tmpM+"."+tmpY);
                        daysCombined++;
                    }
                    daySearched++;
                    tmpD++;
                }
            }

            if(tmpD >= 31){ //JOS VAIHDETAAN SEURAAVAAN KUUKAUTEEN JA TARVITTAESSA SEURAAVAAN VUOTEEN
                tmpM++;
                if(tmpM > 12){
                    tmpY++;
                    tmpM = 1;
                }
                stringYear = SM.customDigit(Integer.toString(tmpY),4);
                stringMonth =  SM.customDigit(Integer.toString(tmpM),2);
                tmpD = 1;
            } else if (lista == null || lista.size() < 1) { //JOS KUUKAUDEN DATAPAKETTI ON TYHJÄ SIIRRYTÄÄN SUORAAN SEURAAVAAN KUUKAUTEEN/VUOTEEN
                tmpM++;
                if(tmpM > 12){
                    tmpY++;
                    tmpM = 1;
                }
                stringYear = SM.customDigit(Integer.toString(tmpY),4);
                stringMonth =  SM.customDigit(Integer.toString(tmpM),2);
                tmpD = 1;
            } else {
                daySearched++;
                tmpD++;
            }
        }

        Log.d(LOGTAG,"days searched: "+daySearched);
        Log.d(LOGTAG,"days from time frame: "+daysCombined);

        //PALAUTTAA "dataOlio" LISTAN JOKA SISÄLTÄÄ PÄIVÄMÄÄRÄLLISESSÄ JÄRJESTYKSESSÄ HALUTUN AIKAVÄLIN PÄIVIEN TIEDOT
        return returnList;

    }
}
