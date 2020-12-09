package Classes;

import android.util.Log;

public class SuperMetodit {

    private static final String LOGTAG = "SuperMetodit.java";

    public String customDigit(String x, int dig){
        Log.d(LOGTAG,"customDigit()");
        if(dig > 0){
            if(x.length()<dig){
                //Log.d(LOGTAG,"X wrong length: "+x.length()+" and length should be: "+dig+" [CONVERTING]");
                for(int i = (dig-x.length()); i > 0;i--){
                    x="0"+x;
                }
                //Log.d(LOGTAG,"conversion done. Returning value: "+x+" [OK]");
                return x;
            }
        } else {
            //Log.d(LOGTAG,"given dig number was 0 or under: "+dig+". Returning starting value [FAILED]");
            return x;
        }
        //Log.d(LOGTAG,"given value: "+x+" already had "+dig+" digits [OK]");
        return x;
    }

    public String createPackageName(String v, String m){
        Log.d(LOGTAG,"createPackageName()");
        v = customDigit(v,4);
        m = customDigit(m,2);
        return "DataY"+v+"M"+m;
    }

    public String convertSeconds(int sec){
        Log.d(LOGTAG,"convertSeconds()");
        int h = 0;
        int m = 0;
        int x = 0;
        while(sec >= 3600){
            h++;
            sec-=3600;
            x++;
            if(x>24)break;
        }
        x = 0;
        while(sec >= 60){
            m++;
            sec-=60;
            x++;
            if(x>60)break;
        }
        if(h>0){
            return h+"h "+m+"min "+sec+"s";
        } else if(m>0){
            return m+"min "+sec+"s";
        }
        return sec+"s";
    }
}
