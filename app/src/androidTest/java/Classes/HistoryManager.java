package Classes;

import android.content.Context;
import android.util.Log;

import com.example.sovellus.Mega;
import com.example.sovellus.SuperMetodit;
import com.example.sovellus.dataOlio;

import java.util.ArrayList;

public class HistoryManager {
    private int sYear, sMonth, sDay;
    private int eYear, eMonth, eDay;

    private Mega mega;
    private SuperMetodit SM;

    private int daysCombined;

    private final static String LOGTAG = "HistoryManager.java";

    Context context;

    public HistoryManager(Context c){
        this.context = c;
        mega = new Mega(c);
    }

    public ArrayList<dataOlio> getDayData(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay){
        this.sYear = startYear;
        this.sMonth = startMonth;
        this.sDay = startDay;

        this.eYear = endYear;
        this.eMonth = endMonth;
        this.eDay = endDay;

        if((startYear > endYear) ||(startYear == endYear && startMonth > endMonth)||(startYear == endYear && startMonth == endMonth && startDay > endDay)) {
            reverse();
        }

        return calculateDays();
    }

    private void reverse(){
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

    private ArrayList<dataOlio> calculateDays(){
        daysCombined = 0;

        int maxDays = 1000;
        int daySearched = 0;

        ArrayList<String> dataPackages = new ArrayList<>();

        int tmpY=this.sYear, tmpM=this.sMonth, tmpD=sDay;

        String stringYear = SM.customDigit(Integer.toString(this.sYear),4);
        String stringMonth =  SM.customDigit(Integer.toString(this.sMonth),4);

        String packageName = SM.createPackageName(stringYear,stringMonth);

        ArrayList<dataOlio> lista = mega.loadData(stringYear,stringMonth);

        while((tmpY <= this.eYear && tmpM <= this.eMonth)&&daySearched <= maxDays){

            if(tmpD > 31){
                tmpM++;
                if(tmpM > 12){
                    tmpY++;
                    tmpM = 1;
                }
                stringYear = SM.customDigit(Integer.toString(this.sYear),4);
                stringMonth =  SM.customDigit(Integer.toString(this.sMonth),4);
                lista = mega.loadData(stringYear,stringMonth);
                packageName = SM.createPackageName(stringYear,stringMonth);
                tmpD = 1;
            }

            if(tmpY == this.eYear && tmpM == this.eMonth && tmpD > this.eDay){
                Log.d(LOGTAG,"days searched: "+daySearched);
                break;
            }

            Log.d(LOGTAG,"searching for date: "+tmpD+"."+tmpM+"."+tmpY);

            if(lista == null || lista.size() < 1){
                tmpM++;
            } else {
                if(mega.dayFromList(tmpD,lista) != null){
                    if(!dataPackages.contains(packageName)){
                        dataPackages.add(packageName);
                    }
                    lista.add(mega.dayFromList(tmpD,lista));
                    Log.d(LOGTAG,"found: "+tmpD+"."+tmpM+"."+tmpY);
                    daysCombined++;
                }
            }

            daySearched++;
            tmpD++;
        }

        Log.d(LOGTAG,"days from time frame: "+daysCombined);

        return lista;

    }
}
