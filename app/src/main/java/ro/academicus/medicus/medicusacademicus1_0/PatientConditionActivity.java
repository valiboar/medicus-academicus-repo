package ro.academicus.medicus.medicusacademicus1_0;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

import static android.content.Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class PatientConditionActivity extends AppCompatActivity {

    public static boolean intentFromPatient;

    private final Handler mHandler = new Handler();
    private Runnable mTimer1;
    private LineGraphSeries<DataPoint> mSeries1;

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

    @Override
    public void onResume() {
        super.onResume();
        mTimer1 = new Runnable() {
            @Override
            public void run() {
                mSeries1.resetData(generateData());
                mHandler.postDelayed(this, 300);
            }
        };
        mHandler.postDelayed(mTimer1, 300);
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacks(mTimer1);
        super.onPause();
    }
}
