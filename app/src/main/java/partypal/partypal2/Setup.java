package partypal.partypal2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class Setup extends AppCompatActivity {
    EditText weightvalue;
    Button weightbutton;
    RadioButton malebutton;
    RadioButton femalebutton;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String WEIGHT = "weight";
    public static final String SEX = "sex";
    public static final String LAST_CALCULATED_TIME = "last_calculated_time";
    public static final String ALCOHOL_WEIGHT = "alcohol_weight";

    String weight;
    boolean sex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        weightvalue = (EditText) findViewById(R.id.weightValue);
        weightbutton = (Button) findViewById(R.id.weightButton);
        malebutton = (RadioButton) findViewById(R.id.maleButton);
        femalebutton = (RadioButton) findViewById(R.id.femaleButton);

        weightbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWeight();
            }
        });

        malebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMale(true);
            }
        });

        femalebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMale(false);
            }
        });

        loadData();
        updateVisible();
    }

    private void saveWeight() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(WEIGHT, weightvalue.getText().toString());
        editor.apply();
    }

    private void saveMale(boolean val) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SEX, val);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        weight = sharedPreferences.getString(WEIGHT, "0");
        sex = sharedPreferences.getBoolean(SEX, false);
    }

    private void updateVisible() {
        weightvalue.setText(weight);
        if (sex) {
            malebutton.setChecked(true);
            femalebutton.setChecked(false);
        } else {
            malebutton.setChecked(false);
            femalebutton.setChecked(true);
        }
    }

}
