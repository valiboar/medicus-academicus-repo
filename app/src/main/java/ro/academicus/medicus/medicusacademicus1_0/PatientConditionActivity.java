package ro.academicus.medicus.medicusacademicus1_0;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import static android.content.Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class PatientConditionActivity extends AppCompatActivity {

    public static boolean intentFromPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_condition);
        intentFromPatient = false;
    }

    public void backToMenu(View view) {
        Intent intent = new Intent(this, MenuActivity.class);

        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        //PatientConditionActivity.this.finish();

        intentFromPatient = true;
        startActivity(intent);
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(this, MenuActivity.class));
    }*/
}
