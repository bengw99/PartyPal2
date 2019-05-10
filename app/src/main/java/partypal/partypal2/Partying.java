package partypal.partypal2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.lang.Object;

import org.w3c.dom.Text;

import java.util.Set;

public class Partying extends AppCompatActivity {

    TextView bacvaluetext;
    //Button refreshbutton;
    Button adddrinkbutton;
    //Button gohomebutton;
    Button sendsmsbutton;
    TextView timetosobertext;

    //TODO time until sober
    //TODO create ranges for BAC, color changes associated to each
    //TODO suggest walking, getting a ride, or hospital for BAC ranges
    //TODO emergency SMS to contact when BAC too high (should definitely be optional)
    //TODO fix crashing on not choosing contact
    //TODO allow multiple contacts
    //TODO allow multiple geofences, selection between them
    //TODO estimated walk time home, possible SMS if not there within 2x time
    //TODO modify background images, make more professional
    //TODO add relative layouts to fix position of buttons
    //TODO force setup before partying is accessible
    //TODO feedback when user sets location


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partying);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bacvaluetext = (TextView) findViewById(R.id.bacValue);
        timetosobertext = (TextView) findViewById(R.id.timeToSober);

        postBAC(calculateBAC());
        updateSoberTimer(calculateSoberTimer(calculateBAC()));

        //refreshbutton = (Button) findViewById(R.id.refreshButton);
        adddrinkbutton = (Button) findViewById(R.id.addDrinkButton);
        //gohomebutton = (Button) findViewById(R.id.goHomeEarlyButton);
        sendsmsbutton = (Button) findViewById(R.id.sendsmsbutton);

        /*refreshbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postBAC(calculateBAC());
            }
        });*/

        adddrinkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double bac = calculateBAC();
                adddrink();
                postBAC(bac);
                updateSoberTimer(calculateSoberTimer(bac));
            }
        });

        sendsmsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendsms();
            }
        });

    }

    private double calculateBAC() {
        double blood_weight, alcohol_weight, elapsed_milliseconds, bac;
        int user_weight;
        boolean sex;
        long current_time, last_calculated_time;

        current_time = System.currentTimeMillis();
        SharedPreferences sharedPreferences = getSharedPreferences(Setup.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        user_weight = Integer.parseInt(sharedPreferences.getString(Setup.WEIGHT, "0"));
        last_calculated_time = Long.parseLong(sharedPreferences.getString(Setup.LAST_CALCULATED_TIME, "0"));
        sex = sharedPreferences.getBoolean(Setup.SEX, false);
        alcohol_weight = Double.parseDouble(sharedPreferences.getString(Setup.ALCOHOL_WEIGHT, "0"));

        blood_weight = .07 * user_weight * 45.3592;
        elapsed_milliseconds = current_time - last_calculated_time;
        if(sex) alcohol_weight = alcohol_weight - (14 * (elapsed_milliseconds/(3600000)));
        else alcohol_weight = alcohol_weight - (7 * (elapsed_milliseconds/(3600000)));
        if(alcohol_weight < 0.0) alcohol_weight = 0.0;
        bac = alcohol_weight/blood_weight;

        last_calculated_time = System.currentTimeMillis();
        editor.putString(Setup.LAST_CALCULATED_TIME, Long.toString(last_calculated_time));
        editor.putString(Setup.ALCOHOL_WEIGHT, Double.toString(alcohol_weight));
        editor.apply();

        return bac;
    }

    private int calculateSoberTimer(double bac){
        boolean sex;
        int counter;

        SharedPreferences sharedPreferences = getSharedPreferences(Setup.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        sex = sharedPreferences.getBoolean(Setup.SEX, false);

        counter = 0;
        while(bac>=0){
            if(sex) bac -= .03;
            else bac -= .015;
            counter++;
        }

        return counter;
    }

    private void updateSoberTimer(int soberTimerNum){
        timetosobertext.setText(Integer.toString(soberTimerNum));
    }

    private void postBAC(double bacnum) {
        bacvaluetext.setText(Double.toString(bacnum+.005).concat("0000").substring(0, 4));
    }

    private void adddrink () {
        SharedPreferences sharedPreferences = getSharedPreferences(Setup.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        double alcohol_weight = Double.parseDouble(sharedPreferences.getString(Setup.ALCOHOL_WEIGHT, "0"));
        alcohol_weight += 14.0;
        System.out.println(alcohol_weight);
        editor.putString(Setup.ALCOHOL_WEIGHT, Double.toString(alcohol_weight));
        editor.apply();
    }

    public void sendsms(){
        SharedPreferences sharedPreferences = getSharedPreferences(Setup.SHARED_PREFS, MODE_PRIVATE);
        String address = sharedPreferences.getString(Setup.CONTACT_NUMBER, "0");
        String message = String.format("Hi! I am home safe and am going to go to bed. My BAC is %2.2f", calculateBAC());
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.getDefault().sendTextMessage(address, null, message, null,null);
    }
}
