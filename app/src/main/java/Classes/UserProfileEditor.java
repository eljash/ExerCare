package Classes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

public class UserProfileEditor {
    private int defaultSportGoal = 3600;
    private int defaultScreenGoal = 7200;

    private Context context;
    private User userProfile;

    public UserProfileEditor(Context c){
        this.context = c;
        getProfile();
    }

    private void getProfile(){
        SharedPreferences dataSaving = context.getSharedPreferences("profile", Activity.MODE_PRIVATE);
        String n = dataSaving.getString("profile_name","No name");
        double sW = Double.longBitsToDouble(dataSaving.getLong("start_weight",Double.doubleToLongBits(0)));
        double wG = Double.longBitsToDouble(dataSaving.getLong("weight_goal",Double.doubleToLongBits(0)));
        int sportTG = dataSaving.getInt("sport_time_goal",defaultSportGoal);
        int screenTG = dataSaving.getInt("screen_time_goal",defaultScreenGoal);
        int y = dataSaving.getInt("birth_year",2020);
        int m = dataSaving.getInt("birth_month",1);
        int d = dataSaving.getInt("birth_day",1);
        Date cD = new Date(dataSaving.getLong("creation_date",0));
        userProfile = new User(n,sW,wG,sportTG,screenTG,y,m,d,cD);
    }

    public User returnProfile(){
        return this.userProfile;
    }

    public void saveProfile(){
        SharedPreferences dataSaving = context.getSharedPreferences("profile", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editSaving = dataSaving.edit();
        editSaving.putString("profile_name",userProfile.name());
        editSaving.putLong("start_weight",Double.doubleToLongBits(userProfile.startWeight()));
        editSaving.putLong("weight_goal",Double.doubleToLongBits(userProfile.weightGoal()));
        editSaving.putInt("sport_time_goal",userProfile.sportTimeGoal());
        editSaving.putInt("screen_time_goal",userProfile.screenTimeGoal());
        editSaving.putInt("birth_year",userProfile.year());
        editSaving.putInt("birth_month",userProfile.month());
        editSaving.putInt("birth_day",userProfile.day());
        editSaving.putLong("creation_date",userProfile.date().getTime());
        editSaving.apply();
    }

    /** KÄYTETTY NEWUSERACTIVITY-LUOKASSA UUDEN PROFIILIN TALLENTAMISEEN */

    public void saveProfile(User user){
        SharedPreferences dataSaving = context.getSharedPreferences("profile", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editSaving = dataSaving.edit();
        editSaving.putString("profile_name",user.name());
        editSaving.putLong("start_weight",Double.doubleToLongBits(user.startWeight()));
        editSaving.putLong("weight_goal",Double.doubleToLongBits(user.weightGoal()));
        editSaving.putInt("sport_time_goal",user.sportTimeGoal());
        editSaving.putInt("screen_time_goal",user.screenTimeGoal());
        editSaving.putInt("birth_year",user.year());
        editSaving.putInt("birth_month",user.month());
        editSaving.putInt("birth_day",user.day());
        editSaving.putLong("creation_date",user.date().getTime());
        editSaving.apply();
    }
}
