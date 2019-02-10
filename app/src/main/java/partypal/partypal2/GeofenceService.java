package partypal.partypal2;

import android.app.IntentService;
import android.content.Intent;

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
                System.out.println("REENTERING THE BATTLEFIELD");
                Partying p = new Partying();
                p.sendsms();
            } else if(transition == geofence.GEOFENCE_TRANSITION_EXIT){
                System.out.println("LEAVING THE SANCTUARY");
            }
        }
    }
}
