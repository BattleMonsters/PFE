package com.example.edwin.pfe;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.EddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleEddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleIBeaconListener;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;

public class SecondActivity extends AppCompatActivity {

    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, SecondActivity.class);
    }

    private Intent serviceIntent;
    private TextView monTexte1;
    private TextView monTexte2;

    private ProximityManager proximityManager;
    private boolean Beacon1recognized = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        KontaktSDK.initialize("NOpUjpWwpCckCePHQnjZngXENPFcmZMj");

        proximityManager = ProximityManagerFactory.create(this);
        proximityManager.setIBeaconListener(createIBeaconListener());
        proximityManager.setEddystoneListener(createEddystoneListener());
    }

    @Override
    protected void onStart() {
        super.onStart();
        startScanning();
    }

    @Override
    protected void onStop() {
        proximityManager.stopScanning();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        proximityManager.disconnect();
        proximityManager = null;
        super.onDestroy();
    }

    private void startScanning() {
        proximityManager.connect(new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                proximityManager.startScanning();
            }
        });
    }

    private IBeaconListener createIBeaconListener() {
        return new SimpleIBeaconListener() {
            @Override
            public void onIBeaconDiscovered(IBeaconDevice ibeacon, IBeaconRegion region) {
                Log.i("Sample", "IBeacon discovered: " + ibeacon.toString());
            }
        };
    }

    private EddystoneListener createEddystoneListener() {
        return new SimpleEddystoneListener() {
            @Override
            public void onEddystoneDiscovered(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
                Log.i("Sample", "Eddystone discovered: " + eddystone.toString() +  " Name : " + eddystone.getName() );

                monTexte1 = (TextView)findViewById(R.id.beacon1);
                if(!Beacon1recognized) {
                    if(eddystone.getInstanceId().equals("6e4b50795750"))
                        monTexte1.setText("Beacon JBqt detected");
                    else if (eddystone.getInstanceId().equals("756b72646934"))
                        monTexte1.setText("Beacon q5nW  detected");
                    else if (eddystone.getInstanceId().equals("444973484733"))
                        monTexte1.setText("Beacon NWs8  detected");
                    Beacon1recognized = true;
                }
                else{
                    monTexte2 = (TextView)findViewById(R.id.beacon2);
                    if(eddystone.getInstanceId().equals("6e4b50795750"))
                        monTexte2.setText("Beacon JBqt detected");
                    else if (eddystone.getInstanceId().equals("756b72646934"))
                        monTexte2.setText("Beacon q5nW  detected");
                    else if (eddystone.getInstanceId().equals("444973484733"))
                        monTexte2.setText("Beacon NWs8  detected");
                }

            }
        };
    }
}
