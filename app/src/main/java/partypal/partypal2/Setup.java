package partypal.partypal2;

import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import static android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

public class Setup extends AppCompatActivity {
    EditText weightvalue;
    Button weightbutton;
    RadioButton malebutton;
    RadioButton femalebutton;
    Button selectcontactbutton;
    TextView contactnametext;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String WEIGHT = "weight";
    public static final String SEX = "sex";
    public static final String LAST_CALCULATED_TIME = "last_calculated_time";
    public static final String ALCOHOL_WEIGHT = "alcohol_weight";
    public static final String CONTACT_NAME = "contact_name";
    public static final String CONTACT_NUMBER = "contact_number";

    private final int PICK_CONTACT = 1;

    String weight;
    boolean sex;
    String contactname;


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
        contactnametext = (TextView) findViewById(R.id.contactname);


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
        contactname = sharedPreferences.getString(CONTACT_NAME, "NO CONTACT");
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
        contactnametext.setText(contactname);

    }

    private void getcontact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Cursor cursor = getContentResolver().query(data.getData(),null, null, null, null);
        while (cursor.moveToNext()) {
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            if (Integer.parseInt(hasPhone) > 0) {
                Cursor phones = getContentResolver().query(CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                while (phones.moveToNext()) {
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    //System.out.println("Name: " + name + ", Phone Number: " + phoneNumber);
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(CONTACT_NAME, name);
                    editor.putString(CONTACT_NUMBER, phoneNumber);
                    editor.apply(); //TODO load and update
                    loadData();
                    updateVisible();
                }
                phones.close();
            }
        }
    }
}
