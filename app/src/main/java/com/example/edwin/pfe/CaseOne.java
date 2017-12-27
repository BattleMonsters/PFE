package com.example.edwin.pfe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.kontakt.proximity.KontaktProvider;
import com.kontakt.proximity.KontaktProximitySDK;
import com.kontakt.proximity.callback.InitCallback;
import com.kontakt.proximity.exception.TriggerExecutionException;
import com.kontakt.proximity.listener.RegionListener;
import com.kontakt.proximity.listener.TriggerListener;
import com.kontakt.sdk.android.ble.device.BeaconRegion;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.model.Trigger;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Edwin on 27/12/2017.
 */

public class CaseOne extends AppCompatActivity {

    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, CaseOne.class);
    }


    public static final int REQUEST_CODE_PERMISSIONS = 100;
    //public  TextView text = (TextView)findViewById(R.id.responseView);
    private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;


    protected void api_try(){
        // Initialize Proximity SDK
        String apiKey = "NOpUjpWwpCckCePHQnjZngXENPFcmZMj";

        final TextView enterText = (TextView)findViewById(R.id.enter);
        final TextView leaveText = (TextView)findViewById(R.id.leave);;
        final TextView triggerText = (TextView)findViewById(R.id.trigger);;
        KontaktProximitySDK sdk = KontaktProvider.provideProximitySDK(getApplicationContext(), apiKey);

        //Add regions
        IBeaconRegion iBeaconRegion = new BeaconRegion.Builder()
                .identifier("NWs8 Region")
                .proximity(UUID.fromString("f7826da6-4fa2-4e98-8024-bc5b71e0893e"))
                .major(6344)
                .minor(28035)
                .build();

        sdk.iBeacon(iBeaconRegion, new RegionListener() {
            @Override
            public void onEntered(IBeaconRegion region, List<IBeaconDevice> iBeacons) {
                // Entered region with iBeacons
                Toast.makeText(CaseOne.this, "Entered Region", Toast.LENGTH_SHORT).show();
                int i;
                TextView text = (TextView)findViewById(R.id.responseView);
                for(i=0;i<iBeacons.size();i++) {
                    String test1 = iBeacons.get(i).toString();
                    String recupText = enterText.getText().toString();
                    Toast.makeText(CaseOne.this, test1, Toast.LENGTH_SHORT).show();
                    SimpleDateFormat heure = new SimpleDateFormat("HH:mm:ss");
                    Calendar c = Calendar.getInstance();
                    String heure1 = heure.format(c.getTime());
                    String test = heure1 + " : " + test1 + "\n\n" + recupText;
                    enterText.setText(test);
                }

            }

            @Override
            public void onAbandoned(IBeaconRegion region) {
                String test2 = "Left Region : " + region.getProximity();
                String recupText = leaveText.getText().toString();
                Toast.makeText(CaseOne.this, test2, Toast.LENGTH_SHORT).show();
                SimpleDateFormat heure = new SimpleDateFormat("HH:mm:ss");
                Calendar c = Calendar.getInstance();
                String heure2 = heure.format(c.getTime());
                String test = heure2 + " : " + test2 + "\n" + recupText;;
                leaveText.setText(test);
            }
        });

       /*sdk.eddystone(eddystoneNamespace, new NamespaceListener() {
            @Override
            public void onEntered(IEddystoneNamespace namespace, List<IEddystoneDevice> eddystones) {
                // Entered namespace with eddystones
            }

            @Override
            public void onAbandoned(IEddystoneNamespace namespace) {
                // Namespace abandoned
            }
        });*/


//        KontaktProximitySDK sdk = KontaktProvider.provideProximitySDK(getApplicationContext(), apiKey);
        //Toast.makeText(MainActivity.this, "whattt", Toast.LENGTH_SHORT).show();
// Define trigger initialization callback

        InitCallback initCallback = new InitCallback() {
            @Override
            public void onSuccess() {
                // Triggers fetched and initialized
                Toast.makeText(CaseOne.this, "Trigger initCallback onSuccess", Toast.LENGTH_SHORT).show();
                SimpleDateFormat heure = new SimpleDateFormat("HH:mm:ss");
                String triggerString1 = "innit Trigger";
                String recupText = triggerText.getText().toString();
                Calendar c = Calendar.getInstance();
                String heure1 = heure.format(c.getTime());
                String test = heure1 + " : " + triggerString1 + "\n" + recupText;
                triggerText.setText(test);
            }

            @Override
            public void onFailure(com.kontakt.proximity.error.ErrorCause cause) {
                Toast.makeText(CaseOne.this, "Trigger initCallback onFailure", Toast.LENGTH_SHORT).show();
            }

        };

// Monitor triggers
        sdk.triggers(initCallback, new TriggerListener() {
            @Override
            public void onHandled(Trigger trigger) {
                // Trigger executed successfully
                Toast.makeText(CaseOne.this, "Monitor trigger onHandled", Toast.LENGTH_SHORT).show();
                SimpleDateFormat heure = new SimpleDateFormat("HH:mm:ss");
                String triggerString1 = "Trigger monitor";
                String recupText = triggerText.getText().toString();
                Calendar c = Calendar.getInstance();
                String heure1 = heure.format(c.getTime());
                String test = heure1 + " : " + triggerString1 + "\n" + recupText;
                triggerText.setText(test);
            }

            @Override
            public void onExecutionFailed(Trigger trigger, TriggerExecutionException e) {
                // Trigger execution error
                Toast.makeText(CaseOne.this, "Monitor trigger Exec failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case1);
        KontaktSDK.initialize("NOpUjpWwpCckCePHQnjZngXENPFcmZMj");
        api_try();

    }
}
