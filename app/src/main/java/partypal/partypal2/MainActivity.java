package partypal.partypal2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button setup = (Button) findViewById(R.id.button2);
        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSetup();
            }
        });

        Button party = (Button) findViewById(R.id.button3);
        party.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoParty();
            }
        });
    }

    private void gotoSetup() {
        Intent intent = new Intent(this, Setup.class);
        startActivity(intent);
    }

    private void gotoParty() {
        Intent intent = new Intent(this, Partying.class);
        startActivity(intent);
    }
}
