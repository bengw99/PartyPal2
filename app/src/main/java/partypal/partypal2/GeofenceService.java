package partypal.partypal2;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceService extends IntentService {

    public GeofenceService(){
        super("GeofenceService");
    }

    @Override
    protected void onHandleIntent(Intent intent){
        //System.out.println("YOU MADE IT HERE");
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if(event.hasError()){}
        else {
            int transition = event.getGeofenceTransition();
            List<Geofence> geofences = event.getTriggeringGeofences();
            Geofence geofence = geofences.get(0);
            String requestId = geofence.getRequestId();

            if(transition == geofence.GEOFENCE_TRANSITION_ENTER){

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

                String address = sharedPreferences.getString(Setup.CONTACT_NUMBER, "0");
                String message = String.format("Hi! I am home safe and am going to go to bed. My BAC is %2.2f", bac);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.getDefault().sendTextMessage(address, null, message, null,null);

            } else if(transition == geofence.GEOFENCE_TRANSITION_EXIT){
                System.out.println("LEAVING THE SANCTUARY");
            }
        }
    }
}
