package ro.academicus.medicus.medicusacademicus1_0;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {

    private static String patientName; // folosit pt. memorarea numelui pacientului pentru refolosirea
                    // lui cand revenim in MenuActivity dintr-un Activity diferit de LoginActivity

    public final static String EXTRA_RECOMMENDATIONS = "ro.academicus.medicus.medicusacademicus1_0.RECOMMENDATIONS";

    private final static int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (!PatientConditionActivity.intentFromPatient && !CalendarActivity.intentFromCalendar) {
            Intent intent = getIntent();
            String patientName = intent.getStringExtra(LoginActivity.EXTRA_PATIENT_NAME);

            MenuActivity.patientName = patientName;

            TextView welcomeTextView = (TextView) findViewById(R.id.welcome_text_view);

            Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.ttf");
            welcomeTextView.setTypeface(typeFace);

            welcomeTextView.append(",\n" + patientName + "!");
            welcomeTextView.setTextSize(40);
        } else if (PatientConditionActivity.intentFromPatient) {
            TextView welcomeTextView = (TextView) findViewById(R.id.welcome_text_view);

            Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.ttf");
            welcomeTextView.setTypeface(typeFace);

            welcomeTextView.append(",\n" + patientName + "!");
            welcomeTextView.setTextSize(40);

            PatientConditionActivity.intentFromPatient = false;
        } else if (CalendarActivity.intentFromCalendar) {
            TextView welcomeTextView = (TextView) findViewById(R.id.welcome_text_view);

            Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.ttf");
            welcomeTextView.setTypeface(typeFace);

            welcomeTextView.append(",\n" + patientName + "!");
            welcomeTextView.setTextSize(40);

            CalendarActivity.intentFromCalendar = false;
        }
    }

    // Method to start the service
    public void startAccService(View view) {
        Button startAccButton = (Button) findViewById(R.id.startAccButton);
        startAccButton.setText("Accelerometrul e pornit!");
        startAccButton.setEnabled(false);

        Snackbar.make(view, "Accelerometrul a fost activat!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Închide", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .show();
        startService(new Intent(getBaseContext(), AccelerometerService.class));
    }

    // Method to stop the service
    public void stopAccService(View view) {
        stopService(new Intent(getBaseContext(), AccelerometerService.class));
    }

    public void signOut(final View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MenuActivity.this);    //sau view.getContext();
        alertDialogBuilder.setMessage(R.string.sign_out_dialog_message)
                .setTitle("Deconectare")
                .setPositiveButton(R.string.sign_out_dialog_pos_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(view.getContext(), LoginActivity.class); // view accesed from an inner class (needs to be final)
                        // putem avea getBaseContext() sau getApplicationContext() in loc de view.getContext()?

                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        MenuActivity.this.finish();

                        startActivity(intent);

                        stopAccService(view);
                    }
                })
                .setNegativeButton(R.string.sign_out_dialog_neg_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setIcon(R.drawable.ic_sign_out_dialog_alert)
                .show();

        /*AlertDialog signOutDialog = alertDialogBuilder.create();
        signOutDialog.show();*/
    }

    public void goToPatientConditionActivity(View view) {

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                onActivityResult(REQUEST_ENABLE_BT, RESULT_OK, null);
            }
        } else {
            String status = "";
            status = "Your device does not support Bluetooth!";
            Toast.makeText(this, status, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(this, PatientConditionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(intent);
            }
        }
    }

    public void goToCalendarActivity(View view) {
        Intent intent = new Intent(this, CalendarActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

        HashMap<String, String[]> recommendations = new HashMap<>();

        String[] recommendations5th = new String[] {"aleargă 30 min", "20 abdomene", "30 genoflexiuni"};
        String[] recommendations6th = new String[] {"mers 60 min", "20 flotări"};

        recommendations.put("5/5/2016", recommendations5th);
        recommendations.put("6/5/2016", recommendations6th);

        intent.putExtra(EXTRA_RECOMMENDATIONS, recommendations);

        startActivity(intent);
    }
}
