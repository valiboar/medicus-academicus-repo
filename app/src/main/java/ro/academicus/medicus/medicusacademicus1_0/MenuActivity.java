package ro.academicus.medicus.medicusacademicus1_0;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        String patientName = intent.getStringExtra(LoginActivity.EXTRA_PATIENT_NAME);

        TextView welcomeTextView = (TextView) findViewById(R.id.welcome_text_view);

        Typeface typeFace = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Bold.ttf");
        welcomeTextView.setTypeface(typeFace);


        welcomeTextView.append(",\n" + patientName + "!");
        welcomeTextView.setTextSize(40);
    }

    public void signOut(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        MenuActivity.this.finish();
        startActivity(intent);
    }
}
