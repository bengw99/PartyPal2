package partypal.partypal2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

public class Setup extends AppCompatActivity {
    EditText weightvalue;
    Button weightbutton;
    RadioButton malebutton;
    RadioButton femalebutton;
    Button selectcontactbutton;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String WEIGHT = "weight";
    public static final String SEX = "sex";
    public static final String LAST_CALCULATED_TIME = "last_calculated_time";
    public static final String ALCOHOL_WEIGHT = "alcohol_weight";

    private final int PICK_CONTACT = 1;

    String weight;
    boolean sex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView mainimg = (ImageView) findViewById(R.id.wineImage);  //HI TROY
        int imageResource = getResources().getIdentifier("@drawable/wine", null, this.getPackageName());  //THIS TOO
        mainimg.setImageResource(imageResource); //LEMONPARTY

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        weightvalue = (EditText) findViewById(R.id.weightValue);
        weightbutton = (Button) findViewById(R.id.weightButton);
        malebutton = (RadioButton) findViewById(R.id.maleButton);
        femalebutton = (RadioButton) findViewById(R.id.femaleButton);
        selectcontactbutton = (Button) findViewById(R.id.addcontactbutton);

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

        selectcontactbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getcontact();
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

    private void getcontact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        System.out.println("456456nosepotatocheesekatendfjaslkdjflkasjdlkfjalsdjfl");
        startActivityForResult(intent, PICK_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONTACT) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                Uri contactData = data.getData();
                Cursor c = getContentResolver().query(contactData, null, null, null, null);

                if (c.moveToFirst()) {
                    String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                    System.out.println(name + "nosepotatocheesekatendfjaslkdjflkasjdlkfjalsdjfl");
                }
            }
        }
    }
}
