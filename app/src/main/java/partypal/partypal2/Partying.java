package partypal.partypal2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Set;

public class Partying extends AppCompatActivity {

    TextView bacvaluetext;
    Button refreshbutton;
    Button adddrinkbutton;
    Button gohomebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partying);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bacvaluetext = (TextView) findViewById(R.id.bacValue);

        postBAC(calculateBAC());

        refreshbutton = (Button) findViewById(R.id.refreshButton);
        adddrinkbutton = (Button) findViewById(R.id.addDrinkButton);
        gohomebutton = (Button) findViewById(R.id.goHomeEarlyButton);

        refreshbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postBAC(calculateBAC());
            }
        });

        adddrinkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adddrink();
                postBAC(calculateBAC());
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
        if(sex) alcohol_weight = alcohol_weight - (14 * (elapsed_milliseconds/(360000)));
        else alcohol_weight = alcohol_weight - (7 * (elapsed_milliseconds/(360000)));
        if(alcohol_weight < 0.0) alcohol_weight = 0.0;
        bac = alcohol_weight/blood_weight;

        last_calculated_time = System.currentTimeMillis();
        editor.putString(Setup.LAST_CALCULATED_TIME, Long.toString(last_calculated_time));
        editor.putString(Setup.ALCOHOL_WEIGHT, Double.toString(alcohol_weight));
        editor.apply();

        return bac;
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

}
