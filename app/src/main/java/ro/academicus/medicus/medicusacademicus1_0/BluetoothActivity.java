package ro.academicus.medicus.medicusacademicus1_0;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.bluetooth.*;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {

//    private ArrayAdapter<String> mDiscoveredBluetoothDevicesAdapter;
//    private ListView listViewDiscoveredDevices;
//
//    private ArrayList<BluetoothDevice> arrayListBluetoothDevices;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

//        arrayListBluetoothDevices = new ArrayList<>();
//
//        mDiscoveredBluetoothDevicesAdapter = new ArrayAdapter<>(BluetoothActivity.this, android.R.layout.simple_list_item_single_choice);
//
//        listViewDiscoveredDevices = (ListView) findViewById(R.id.listViewDevices);
//        listViewDiscoveredDevices.setAdapter(mDiscoveredBluetoothDevicesAdapter);
//
//        mDiscoveredBluetoothDevicesAdapter.notifyDataSetChanged();
//
//        //startSearching();
//
//        listViewDiscoveredDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//
//                //bluetoothAdapter.cancelDiscovery();
//                BluetoothSocket bluetoothSocket = null;
//
//                BluetoothDevice clickedBluetoothDevice = arrayListBluetoothDevices.get(position);
//
//                BluetoothDevice larisaPhone = bluetoothAdapter.getRemoteDevice("A0:32:99:53:77:14");
//
//                if (clickedBluetoothDevice.equals(larisaPhone)) {
//                    Toast.makeText(getBaseContext(), "egale", Toast.LENGTH_LONG).show();
//                    try {
//                        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                        String uuid = tManager.getDeviceId();
//                        bluetoothSocket = larisaPhone.createRfcommSocketToServiceRecord(UUID.fromString(uuid));
//                    } catch (IOException e1) {
//                        // TODO Auto-generated catch block
//                        e1.printStackTrace();
//                    }
//
//                    Log.d("ceva", "++++ Connecting");
//                    try {
//                        bluetoothSocket.connect();
//                    } catch (IOException e1) {
//                        // TODO Auto-generated catch block
//                        e1.printStackTrace();
//                    }
//                    Log.d("ceva", "++++ Connected");
//                } else {
//                    Toast.makeText(getBaseContext(), "inegale", Toast.LENGTH_LONG).show();
//                }
//
//            }
//        });


    }

}

//    private void startSearching() {
//        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        BluetoothActivity.this.registerReceiver(mReceiver, intentFilter);
//        bluetoothAdapter.startDiscovery();
//    }

//    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            String action = intent.getAction();
//
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//
//                if (arrayListBluetoothDevices.size() < 1) {
//                    mDiscoveredBluetoothDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
//                    arrayListBluetoothDevices.add(device);
//                    mDiscoveredBluetoothDevicesAdapter.notifyDataSetChanged();
//                } else {
//                    boolean flag = true; // flag to indicate that particular device is already in the arrayList or not
//                    for (int i = 0; i < arrayListBluetoothDevices.size(); i++) {
//                        if (device.getAddress().equals(arrayListBluetoothDevices.get(i).getAddress())) {
//                            flag = false;
//                        }
//                    }
//
//                    if (flag) {
//                        mDiscoveredBluetoothDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
//                        arrayListBluetoothDevices.add(device);
//                        mDiscoveredBluetoothDevicesAdapter.notifyDataSetChanged();
//                    }
//                }
//            }
//        }
//    };

