package com.example.edwin.pfe;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kontakt.proximity.KontaktProvider;
import com.kontakt.proximity.KontaktProximitySDK;
import com.kontakt.proximity.callback.InitCallback;
import com.kontakt.proximity.exception.TriggerExecutionException;
import com.kontakt.proximity.listener.NamespaceListener;
import com.kontakt.proximity.listener.RegionListener;
import com.kontakt.proximity.listener.TriggerListener;
import com.kontakt.sdk.android.ble.connection.ErrorCause;
import com.kontakt.sdk.android.ble.device.BeaconRegion;
import com.kontakt.sdk.android.common.model.Trigger;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_PERMISSIONS = 100;

    private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;


    protected void api_try(){
        // Initialize Proximity SDK
        String apiKey = "NOpUjpWwpCckCePHQnjZngXENPFcmZMj";


        KontaktProximitySDK sdk = KontaktProvider.provideProximitySDK(getApplicationContext(), apiKey);

        //Add regions
        IBeaconRegion iBeaconRegion = new BeaconRegion.Builder()
                .identifier("iBeacon Region")
                .proximity(UUID.fromString("f7826da6-4fa2-4e98-8024-bc5b71e0893e"))
                .major(6344)
                .minor(28035)
                .build();

        sdk.iBeacon(iBeaconRegion, new RegionListener() {
            @Override
            public void onEntered(IBeaconRegion region, List<IBeaconDevice> iBeacons) {
                // Entered region with iBeacons
                Toast.makeText(MainActivity.this, "Entered Region", Toast.LENGTH_SHORT).show();
                String test1 = iBeacons.get(0).toString();
                Toast.makeText(MainActivity.this, test1, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAbandoned(IBeaconRegion region) {
                // Region abandoned
            }
        });

       /* sdk.eddystone(eddystoneNamespace, new NamespaceListener() {
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
        /*InitCallback initCallback = new InitCallback() {
            @Override
            public void onSuccess() {
                // Triggers fetched and initialized
                Toast.makeText(MainActivity.this, "Trigger initCallback onSuccess", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(com.kontakt.proximity.error.ErrorCause cause) {
                Toast.makeText(MainActivity.this, "Trigger initCallback onFailure", Toast.LENGTH_SHORT).show();
            }

        };

// Monitor triggers
        sdk.triggers(initCallback, new TriggerListener() {
            @Override
            public void onHandled(Trigger trigger) {
                // Trigger executed successfully
                Toast.makeText(MainActivity.this, "Monitor trigger onHandled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onExecutionFailed(Trigger trigger, TriggerExecutionException e) {
                // Trigger execution error
                Toast.makeText(MainActivity.this, "Monitor trigger Exec failed", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    protected void test() {


        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null){
            //Toast.makeText(MainActivity.this, "Pas de Bluetooth", Toast.LENGTH_SHORT).show();
    }else{
           // Toast.makeText(MainActivity.this, "Avec Bluetooth", Toast.LENGTH_SHORT).show();
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBlueTooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBlueTooth, REQUEST_CODE_ENABLE_BLUETOOTH);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Button bouton_ble =(Button) findViewById(R.id.button_bluetooth);
        if (requestCode != REQUEST_CODE_ENABLE_BLUETOOTH){
            bouton_ble.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_action_name));
            return;
        }

        if (resultCode == RESULT_OK) {
            Toast.makeText(MainActivity.this, "Bluetooth activé", Toast.LENGTH_SHORT).show();
            bouton_ble.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_action_name));
        } else {
            bouton_ble.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_bluetooth_off));
        }
    }

    private LinearLayout buttonsLayout;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test();
        setupButtons();
        checkPermissions();

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setupButtons() {
        //buttonsLayout = (LinearLayout) findViewById(R.id.buttons_layout);

        final Button bluetoothButton = (Button) findViewById(R.id.button_bluetooth);
        final Button search = (Button) findViewById(R.id.queryButton);
        final Button secondActivity = (Button) findViewById(R.id.secondActivity);

        bluetoothButton.setOnClickListener(this);
        search.setOnClickListener(this);
        secondActivity.setOnClickListener(this);
    }

    private void checkPermissions() {
        int checkSelfPermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (PackageManager.PERMISSION_GRANTED != checkSelfPermissionResult) {
            //Permission not granted so we ask for it. Results are handled in onRequestPermissionsResult() callback.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSIONS);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_bluetooth:
                test();
                break;
            case R.id.queryButton:
                api_try();
                break;
            case R.id.secondActivity:
                startActivity(SecondActivity.createIntent(this));
                break;
            //Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
            //test();

        }
    }


    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }


    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}