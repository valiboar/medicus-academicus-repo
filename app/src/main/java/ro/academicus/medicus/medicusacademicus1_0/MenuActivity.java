package ro.academicus.medicus.medicusacademicus1_0;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        String patientName = intent.getStringExtra(LoginActivity.EXTRA_PATIENT_NAME);

        TextView welcomeTextView = (TextView) findViewById(R.id.welcome_text_view);

        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.ttf");
        welcomeTextView.setTypeface(typeFace);


        welcomeTextView.append(",\n" + patientName + "!");
        welcomeTextView.setTextSize(40);
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
                        MenuActivity.this.finish();
                        startActivity(intent);
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
        Intent intent = new Intent(this, PatientConditionActivity.class);
        startActivity(intent);
    }
}
