package ro.academicus.medicus.medicusacademicus1_0;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import static android.content.Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

import android.provider.Settings.Secure;

enum ReceivedState {
    CITIRE_TAG_PULS, CITIRE_VAL_PULS,
    CITIRE_TAG_TEMP, CITIRE_VAL_TEMP,
    CITIRE_TAG_UMID, CITIRE_VAL_UMID,
    CITIRE_TAG_EKG, CITIRE_VAL_EKG,
};

public class PatientConditionActivity extends AppCompatActivity {

    public static boolean intentFromPatient;

    private final Handler mHandler = new Handler();
    private Runnable mTimer1;
    private LineGraphSeries<DataPoint> mSeries1;

    private ArrayList<String> mPairedDevicesArrayAdapter;

    private static String deviceName = "RN42-DA9F";

    private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private BluetoothSocket bluetoothSocket = null;

    public ArrayList<String> getDevices() {
        mPairedDevicesArrayAdapter = new ArrayList<String>();

        // Get a set of currently paired devices and append to 'pairedDevices'
        Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();

        // Add previosuly paired devices to the array
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            mPairedDevicesArrayAdapter.add("null");
        }

        return mPairedDevicesArrayAdapter;
    }

    private Runnable bluetoothRunnable;

    private ConnectedThread mConnectedThread = null;

    public void socketOpen(String fullDeviceAddress) {
        //Get the MAC address from the DeviceListActivty via EXTRA
        String address = fullDeviceAddress.substring(fullDeviceAddress.length() - 17);

        //create device and set the MAC address
        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);

        try {
            bluetoothSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
        }

        try {
            bluetoothSocket.connect();
            Toast.makeText(getBaseContext(), "Bluetooth connected! - " + device.getName(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket connect failed", Toast.LENGTH_LONG).show();
            socketClose();
        }

        mConnectedThread = new ConnectedThread(bluetoothSocket);

        socketWrite("1");

        mConnectedThread.start();

    }

    public void socketWrite(String mess) {
        if (mConnectedThread != null)
            mConnectedThread.write(mess);
    }


    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "Error opening streams", Toast.LENGTH_LONG).show();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


        @Override
        public void run() {
            byte[] buffer = new byte[100];

            ReceivedState receivedState = ReceivedState.CITIRE_TAG_PULS;

            ArrayList<String> values = new ArrayList<>();

            while (true) {
                try {
                    while (mmInStream.read(buffer, 0, buffer.length) != -1) {

                        String received = "";

                        switch (receivedState) {
                            case CITIRE_TAG_PULS:
                                received = new String(buffer);
                                received = received.replaceAll("\0", "");

                                if (received.equals("Puls"))
                                {
                                    values.add(received);
                                    receivedState = ReceivedState.CITIRE_VAL_PULS;
                                }

                                break;

                            case CITIRE_VAL_PULS:
                                received = new String(buffer);
                                values.add(received);
                                double pulseValue = Double.valueOf(received);
                                receivedState = ReceivedState.CITIRE_TAG_TEMP;

                                break;

                            case CITIRE_TAG_TEMP:
                                received = new String(buffer);

                                if (received.equals("Temperatura"))
                                {
                                    values.add(received);
                                    receivedState = ReceivedState.CITIRE_VAL_TEMP;
                                }

                                break;

                            case CITIRE_VAL_TEMP:
                                received = new String(buffer);
                                values.add(received);
                                double tempValue = Double.valueOf(received);
                                receivedState = ReceivedState.CITIRE_TAG_UMID;

                                break;

                            case CITIRE_TAG_UMID:
                                received = new String(buffer);

                                if (received.equals("Umiditate"))
                                {
                                    values.add(received);
                                    receivedState = ReceivedState.CITIRE_VAL_UMID;
                                }

                                break;

                            case CITIRE_VAL_UMID:
                                received = new String(buffer);
                                values.add(received);
                                double umidValue = Double.valueOf(received);
                                receivedState = ReceivedState.CITIRE_TAG_EKG;

                                break;

                            case CITIRE_TAG_EKG:
                                received = new String(buffer);

                                if (received.equals("EKG"))
                                {
                                    values.add(received);
                                    receivedState = ReceivedState.CITIRE_VAL_EKG;
                                }

                                break;

                            case CITIRE_VAL_EKG:
                                received = new String(buffer);
                                values.add(received);
                                double ekgValue = Double.valueOf(received);
                                receivedState = ReceivedState.CITIRE_TAG_PULS;

                                break;
                        }

                    }

                    CharSequence[] charSequences = values.toArray(new CharSequence[values.size()]);
                    Toast.makeText(getBaseContext(), Arrays.toString(charSequences), Toast.LENGTH_LONG).show();
                } catch (IOException IOex) {
                    IOex.printStackTrace();
                }
            }
        }


        public void write(String input) {
            byte[] msgBuffer = input.getBytes(); // converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer); // write bytes over BT connection via mmOutStream
                mmOutStream.flush();
            } catch (IOException e) {
                // if you cannot write, close the application
                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
            }
        }
    }


    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        //TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //String uuidString = tManager.getDeviceId();
        UUID myDeviceUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        return device.createRfcommSocketToServiceRecord(myDeviceUUID);
    }

    public void socketClose() {
        try {
            bluetoothSocket.close();
        } catch (IOException e2) {
            Toast.makeText(getBaseContext(), "Socket close failed", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_condition);
        intentFromPatient = false;

        GraphView graph = (GraphView) findViewById(R.id.ecg_graph);
        /*LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });*/

        mSeries1 = new LineGraphSeries<>(generateData());
        graph.addSeries(mSeries1);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(30);
        graph.setBackgroundColor(Color.parseColor("#FFFFFF"));
        graph.setDrawingCacheBackgroundColor(Color.rgb(255, 64, 129));

        TextView txtTemp = (TextView) findViewById(R.id.txt_temp);
        TextView txtPulse = (TextView) findViewById(R.id.txt_pulse);
        TextView txtHumidity = (TextView) findViewById(R.id.txt_humidity);

        txtTemp.setText(Html.fromHtml("30.5<sup><small>◦</small></sup>C"));
        txtPulse.setText("80");
        txtHumidity.setText("50.6");
    }

    public void backToMenu(View view) {
        Intent intent = new Intent(this, MenuActivity.class);

        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        //PatientConditionActivity.this.finish();

        intentFromPatient = true;
        startActivity(intent);

        socketClose();
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(this, MenuActivity.class));
    }*/

    Random mRand = new Random();

    private DataPoint[] generateData() {
        int count = 30;
        DataPoint[] values = new DataPoint[count];
        for (int i = 0; i < count; i++) {
            double x = i;
            double f = mRand.nextDouble() * 0.15 + 0.3;
            double y = Math.sin(i * f + 2) + mRand.nextDouble() * 0.3;
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }

    private boolean connected = false;

    @Override
    public void onResume() {
        super.onResume();

        mTimer1 = new Runnable() {
            @Override
            public void run() {
                mSeries1.resetData(generateData());
                mHandler.postDelayed(this, 300);

                if (!connected) {
                    ArrayList<String> listOfDevices = getDevices();
                    if (listOfDevices.size() != 0) {
                        for (String fullName : listOfDevices) {
                            if (fullName.indexOf(deviceName) != -1) {
                                deviceName = fullName;
                                break;
                            }
                        }
                    }

                    socketOpen(deviceName);

                    connected = true;
                }
            }
        };

        mHandler.postDelayed(mTimer1, 300);
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacks(mTimer1);
        socketClose();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socketClose();
    }
}
