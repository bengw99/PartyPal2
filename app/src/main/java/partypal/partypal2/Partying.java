package partypal.partypal2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Set;

public class Partying extends AppCompatActivity {

    TextView bacvaluetext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partying);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bacvaluetext = (TextView) findViewById(R.id.bacValue);

        postBAC(calculateBAC());
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

        blood_weight = .07 * user_weight * 453.592;
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
        bacvaluetext.setText(Double.toString(bacnum));
    }

}
