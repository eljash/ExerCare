/**
* Olio -> Json, Json -> Olio
*
* Key: päivämäärä pv.kk.vv
*
* Kaksi eri Json tiedostoa, data ja sitten käyttäjän tiedot
*
* Data muoto: avain, urheilu, ruutuaika, paino, onko syötetty painoa?
*
* Käyttäjän tiedot: käyttäjänimi, ikä, datailia?, tavoite ruutuaika, tavoite paino
*
* VAIHDA ".sovellus" ohjelman nimeksi
*
* Ohjelmointi "Lecture05" JSON... Lataa .jar tiedosto ja importoi
*
*/
package com.example.sovellus;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Collections;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class Mega {
    private String currentDate;
    private ArrayList<dataOlio> SaveList = new ArrayList<dataOlio>();
    private String currentSavePackage = null;
    private static Context activityContext;
    private dataOlio today;
    private dataOlio dayToHandle;
    private int x = 1;

    private static final String LOGTAG = "MEGA.JAVA";

    /** LUOKKA TARVII MainActivity:STÄ TAI MUUSTA
     * AKTIVITEETISTÄ Context - KONTEKSTIN JOTTA
     * SharedPreferences:in getSharedPreferences
     * TOIMISI */
    public Mega(Context context){
        this.activityContext = context;
        Log.d(LOGTAG, "Constructing");
    }

    /** Katsoo onko päivämäärälle jo olemassa dataa
     * Jos ei ole, niin tekee uuden "päivän" listaan
     * ja tallentaa listan saveData() metodilla
     * */
    public void todayData(){
        Log.d(LOGTAG, "todayData()");
        today = searchDateFromList(getDate());
        if(today == null){
            Log.d(LOGTAG, "no data for this day");
            today = new dataOlio();
            if(SaveList.size() != 0){
                today.insertWeight(SaveList.get(SaveList.size()-1).returnWeight());
                Log.d(LOGTAG, "adding new day to array with weight value");
            }
            SaveList.add(today);
            saveData();
        } else {
            Log.d(LOGTAG, "this day has data");
        }
    }

    /**
     * Katsoo listan läpi mahdollisilta virheiltä
     */
    public void searchList(){
        Log.d(LOGTAG, "searchList()");
        if(SaveList.size()>0) {

            boolean onkoJarjestuksessa = true;

            /**
             * Verrataan listan olioiden päivämääriä, jos väärässä järjestyksessä ->
             * järjestetään lista uudelleen
             */

             for (int i = 0; i < SaveList.size();i++){
                 if(SaveList.get(i).getDateInt() > SaveList.get(i++).getDateInt()){
                     onkoJarjestuksessa = false;
                     break;
                 }
             }
             /** JOS LISTA EI OLE JÄRJESTYKSESSÄ */
             if(!onkoJarjestuksessa){
                 Log.d(LOGTAG, "reorganizing datalist");
                 /**
                  * Ensiksi tehdään ArrayList johon otetaan dataOlio ArrayLististä
                  * päivämäärä Integer muodossa. Nämä päivämäärät laitetaan vastaavaan
                  * indexiin Integer listassa.
                  */

                 ArrayList<Integer> objectNumber = new ArrayList<Integer>();
                 Log.d(LOGTAG,"Inserting values from ObjectList to IntegerList");
                 for(int i = 0; i < SaveList.size();i++){
                     objectNumber.set(i, SaveList.get(i).getDateInt());
                     Log.d(LOGTAG,"Index "+i+" - value: "+objectNumber.get(i));
                 }

                 /**
                  * Kutsutaan kokoelmasta sort metodisa, joka järjestää Integer listan
                  * muuttujat arvojärjestykseen.
                  */

                 Collections.sort(objectNumber);

                 /** LUODAAN UUSI ARRAYLISTI JOHON VÄLIAIKAISESTI TALLENETAAN
                  * OLIOLISTAN DATA JA SIIRRETÄÄN JÄRJESTYKSESSÄ TAKAISIN */

                 ArrayList<dataOlio> tempData = new ArrayList<dataOlio>();

                 Log.d(LOGTAG,"inserting values to new temporary ObjectList");

                 for(int i = 0; i < SaveList.size();i++){
                    tempData.set(i, searchIntDateFromList(objectNumber.get(i)));
                     Log.d(LOGTAG,"Index "+i+" - value: "+tempData.get(i).getDateInt());
                 }

                 Log.d(LOGTAG,"inserting values from temporary ObjectList to ObjectList");

                 /** SIIRRETÄÄN TEMP LISTALTA TIEDOT PÄÄ ARRAYLISTIIN */
                 for(int i = 0; i < tempData.size();i++){
                     SaveList.set(i, tempData.get(i));
                     Log.d(LOGTAG,"TmpList Key: "+tempData.get(i).getDateInt()+" - ObjList key: "+SaveList.get(i).getDateInt());
                 }
             }
             saveData();
        }

    }

    /**
     * Ottaa muuttujan Stringin joka on muotoa
     * dd/MM/yyyy
     * Vertailee syötettyä stringiä listan jokaisen
     * indexin olioihin, kunnes löytyy mahdollinen
     * olio tällä päivämäärällä
     */
    public dataOlio searchDateFromList(String date) {
        Log.d(LOGTAG,"searchDateFromList()");
        int size = SaveList.size();
        Log.d(LOGTAG,"ObjectList size: "+size);
        if (size > 0) {
            Log.d(LOGTAG, size+" > 0");
            for (int i = (size - 1); i >= 0; i--) {
                String dateToCompare = SaveList.get(i).getSaveDate();
                Log.d(LOGTAG,"  Comparing: "+date + " - "+dateToCompare);
                if (dateToCompare.equals(date)) {
                    Log.d(LOGTAG,"Object with the same date found!");
                    return SaveList.get(i);
                }
            }
        }
        Log.d(LOGTAG,"Date: "+date+" is not stored...");
        return null;
    }

    /**
     * Ottaa muuttujan int joka on muotoa
     * yyyyMMdd
     * Vertailee syötettyä stringiä listan jokaisen
     * indexin olioihin, kunnes löytyy mahdollinen
     * olio tällä päivämäärällä
     */
    public dataOlio searchIntDateFromList(int date) {
        Log.d(LOGTAG,"searchIntDateFromList()");
        loadData();
        Log.d(LOGTAG, "Searching object with date: "+date);
        int size = SaveList.size();
        if (size > 0) {
            for (int i = size - 1; i <= 0; i--) {
                if (SaveList.get(i).getDateInt() == date) {
                    Log.d(LOGTAG,"Object found with date: "+date);
                    return SaveList.get(i);
                }
            }
        }
        Log.d(LOGTAG,"Object with date: "+date+" not found....");
        return null;
    }

    /** METODI DATAN TALLENTAMISEEN JSON MUOTOON */
    public void saveData(){
        Log.d(LOGTAG,"saveData()");
        SharedPreferences dataSaving = activityContext.getSharedPreferences("saveData", Activity.MODE_PRIVATE);
        SharedPreferences.Editor dataEditor = dataSaving.edit();
        Gson gson = new Gson();
        String json = gson.toJson(SaveList);
        Log.d(LOGTAG,"JSON format:");
        Log.d(LOGTAG,json);
        dataEditor.putString("data", json);
        dataEditor.commit();
    }

    /** DATA PAKETIN TALLENNUS METODI */
    public void saveDataPackage(){
        Log.d(LOGTAG,"saveDataPackage()");
        SharedPreferences dataSaving = activityContext.getSharedPreferences("saveData", Activity.MODE_PRIVATE);
        SharedPreferences.Editor dataEditor = dataSaving.edit();
        Gson gson = new Gson();
        String json = gson.toJson(SaveList);
        Log.d(LOGTAG,"JSON format:");
        Log.d(LOGTAG,json);
        dataEditor.putString(currentSavePackage, json);
        dataEditor.commit();
    }

    /** TALLENNA TIETTY LISTA TIETTYYN DATA PAKETTIIN */
    private void savePackage(String v, String m, ArrayList<dataOlio> dataList){
        Log.d(LOGTAG,"savePackage()");
        v = customDigit(v,4);
        m = customDigit(m, 2);
        String saveName = "DataY"+v+"M"+m;
        SharedPreferences dataSaving = activityContext.getSharedPreferences("saveData", Activity.MODE_PRIVATE);
        SharedPreferences.Editor dataEditor = dataSaving.edit();
        Gson gson = new Gson();
        String json = gson.toJson(dataList);
        Log.d(LOGTAG,"JSON format:");
        Log.d(LOGTAG,json);
        dataEditor.putString(saveName, json);
        dataEditor.commit();
    }

    /** JSON TALLENNUKSEN AVAAMINEN JA MUUTTAMINEN ARRAYLISTIKSI */
    public void loadData(){
        Log.d(LOGTAG,"loadData()");
        SharedPreferences dataSaving = activityContext.getSharedPreferences("saveData", Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = dataSaving.getString("data", "Empty");
        Log.d(LOGTAG,json);
        this.SaveList = gson.fromJson(json, new TypeToken<ArrayList<dataOlio>>(){}.getType());
    }

    /** VERTAA ONKO PAKETTI SAMAN NIMINEN KUIN "String date"
     *  String date:n TULISI OLLA MUOTO "DataY2020M12"
     */
    public boolean packageDateEquals(String date){
        if(currentSavePackage.equals(date)){
            return true;
        }
        return false;
    }

    public ArrayList<dataOlio> loadData(String year, String month){
        Log.d(LOGTAG,"loadData() with parameters");
        Log.d(LOGTAG,"Parameters: y:"+year+" m: "+month);
        if(year.matches("[0-9]+")&&month.matches("[0-9]+")){
            Log.d(LOGTAG,"all Strings have only digits[OK]");
            if(year.length() == 4 && month.length() == 2){
                Log.d(LOGTAG,"every parameter has correct length [OK]");
            } else {
                Log.d(LOGTAG,"not every parameter has the correct length [CONVERTING]");
                year = customDigit(year,4);
                month = customDigit(month,2);
                Log.d(LOGTAG,"Parameters: y:"+year+" m: "+month);
            }
            saveDataPackage();
            getSavedData(year,month);
        } else {
            Log.d(LOGTAG,"some of the values have characters [FAILED]");
            Log.d(LOGTAG,"Parameters: y:"+year+" m: "+month);
        }
        return null;
    }

    private ArrayList<dataOlio> getSavedData(String v, String m){
        String saveName = "DataY"+v+"M"+m;
        Log.d(LOGTAG,"getSavedData() - "+saveName);
        SharedPreferences dataSaving = activityContext.getSharedPreferences("saveData", Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = dataSaving.getString(saveName, "Empty");
        Log.d(LOGTAG,json);
        if(!json.equals("Empty")){
            this.SaveList = gson.fromJson(json, new TypeToken<ArrayList<dataOlio>>(){}.getType());
            currentSavePackage = saveName;
            Log.d(LOGTAG,"Data package '"+saveName+"' loaded successfully! [OK]");
            return gson.fromJson(json, new TypeToken<ArrayList<dataOlio>>(){}.getType());
        } else {
            Log.d(LOGTAG,"no data package with name: "+saveName+" [FAILED]");
        }
        return null;
    }

    public dataOlio dateToHandle(int d){
        Log.d(LOGTAG,"dateToHandle()");
        if(dayToHandle.getDay() == d){
            Log.d(LOGTAG,"this date data already open! [OK]");
            return dayToHandle;
        }else if(this.SaveList.size() > 0){
            for(int i=this.SaveList.size()-1; i >= 0; i--){
                if(Integer.toString(this.SaveList.get(i).getDay()).equals(d)){
                    dayToHandle = this.SaveList.get(i);
                    Log.d(LOGTAG,"this date data found! [OK]");
                    return dayToHandle;
                }
            }
        }
        Log.d(LOGTAG,"No date found! Returning null. [FAILED]");
        return null;
    }

    /** PÄIVÄMÄÄRÄN MÄÄRITTÄMISEN METODI */
    private String getDate(){
        Log.d(LOGTAG,"getDate()");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        currentDate = formatter.format(date);
        Log.d(LOGTAG,"Today date: "+currentDate);
        return currentDate;
    }

    /** MUUNTAA ANNETUSTA ARVOSTA NIIN MONTA DIGISEN ARVON KUIN HALUAA */
    private String customDigit(String x, int dig){
        Log.d(LOGTAG,"customDigit()");
        if(dig > 0){
            if(x.length()<dig){
                Log.d(LOGTAG,"X wrong length: "+x.length()+" and length should be: "+dig+" [CONVERTING]");
                for(int i = (dig-x.length()); i > 0;i--){
                    x="0"+x;
                }
                Log.d(LOGTAG,"conversion done. Returning value: "+x+" [OK]");
                return x;
            }
        } else {
            Log.d(LOGTAG,"given dig number was 0 or under: "+dig+". Returning starting value [FAILED]");
            return x;
        }
        Log.d(LOGTAG,"given value: "+x+" already had "+dig+" digits [OK]");
        return x;
    }

    /** KATSOO ONKO PÄIVÄMÄÄRÄLLE JO OMA DATA PAKETTI true = on, false = ei*/
    private boolean checkForDataPackage(String v, String m){
        String saveName = "DataY"+v+"M"+m;
        Log.d(LOGTAG,"checkForDataPackage() - "+saveName);
        SharedPreferences dataSaving = activityContext.getSharedPreferences("saveData", Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = dataSaving.getString(saveName, "Empty");
        if(!json.equals("Empty")){
            Log.d(LOGTAG,"Data package '"+saveName+"' found! [OK]");
            return true;
        } else {
            Log.d(LOGTAG,"no data package with name: "+saveName);
        }
        return false;
    }

    /** ETSII LISTASTA HALUTUN PÄIVÄN */
    private dataOlio dayFromList(int d,ArrayList<dataOlio> objectList){
        Log.d(LOGTAG,"dayFromList()");
        Log.d(LOGTAG, "Searching object with day: "+d);
        if(objectList != null){
            int size = objectList.size();
            if (size > 0) {
                for (int i = size - 1; i <= 0; i--) {
                    if (objectList.get(i).getDay() == d) {
                        Log.d(LOGTAG,"Object found with day: "+d+"[OK]");
                        return objectList.get(i);
                    }
                }
            }
        } else {
            Log.d(LOGTAG,"object list empty: value = null [FAILED]");
            return null;
        }

        Log.d(LOGTAG,"Object with day: "+d+" not found... Returning null [FAILED]");
        return null;
    }

    /** LUO UUDEN TIEDOSTO PAKETIN */
    private void createDataPackage(String v, String m){
        Log.d(LOGTAG,"createDataPackage()");
        if(!checkForDataPackage(v,m)){
            String packageName = "DataY"+v+"M"+m;
            Log.d(LOGTAG,"creating new data package with name: "+packageName);
            SharedPreferences newPackage = activityContext.getSharedPreferences("saveData",Activity.MODE_PRIVATE);
            SharedPreferences.Editor newPackageEditor = newPackage.edit();
            newPackageEditor.putString(packageName,null);
            newPackageEditor.commit();
            Log.d(LOGTAG,"new package created! [OK]");
        } else {
            Log.d(LOGTAG,"data package with year: "+v+" and month: "+m+" already exists! [FAILED]");
        }
    }

    /** PALAUTTAA AUKI OLEVAN LISTAN PITUUDEN */
    public int listSize(){
        return SaveList.size();
    }

    /** AUKI OLEVAN LISTAN TYHJETÄJÄ */
    public void clearData(){
        SaveList.clear();
        saveData();
    }

    /** ENEMMÄN DEBUGGAAMISTA VARTEN TEHTY METODI JOLLA LISÄTÄ OLIOTA */
    public void insertData(String v, String m, int d, double sport, double screen, double weight, boolean day){
        Log.d(LOGTAG,"insertData()");
        boolean packageFound = false;
        ArrayList<dataOlio> dataList;
        dataOlio insertDay;
        Log.d(LOGTAG,"inserting date year:"+v+"month: "+m+" day: "+d+" sport "+sport+" screet "+screen+" weight "+weight);
        v = customDigit(v,4);
        m = customDigit(m,2);

        packageFound = checkForDataPackage(v,m);
        if(packageFound){
            dataList = loadData(v,m);
        }else{
            createDataPackage(v,m);
            dataList = loadData(v,m);
        }

        if(dayFromList(d,dataList) != null){
            Log.d(LOGTAG,"inserting values to arraylist");
            insertDay = dayFromList(d,dataList);
            insertDay.insertSport(sport);
            insertDay.insertScreen(screen);
            insertDay.insertWeight(weight);
            insertDay.insertWeightBoolean(day);
            savePackage(v,m,dataList);
        }
    }
}