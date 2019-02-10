package partypal.partypal2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        //insert formula for BAC
        return 0.08;
    }

    private void postBAC(double bacnum) {
        bacvaluetext.setText(Double.toString(bacnum));
    }

}
