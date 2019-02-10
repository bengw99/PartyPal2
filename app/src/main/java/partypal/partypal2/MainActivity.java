package partypal.partypal2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView mainimg = (ImageView) findViewById(R.id.shotImage);  //HI TROY
        int imageResource = getResources().getIdentifier("@drawable/shot", null, this.getPackageName());  //THIS TOO
        mainimg.setImageResource(imageResource); //LEMONPARTY

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
