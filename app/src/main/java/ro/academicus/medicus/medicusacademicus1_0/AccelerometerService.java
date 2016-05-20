package ro.academicus.medicus.medicusacademicus1_0;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public class AccelerometerService extends Service implements SensorEventListener {

    private SensorManager sensorManager;

    private double ax, ay, az;  // these are the acceleration values in x, y and z axis

    private CountDownTimer accTimer = new CountDownTimer(3000, 100) {

        public void onTick(long millisUntilFinished) {

        }

        public void onFinish() {
            if (ax == 0 && ay == 0 && az == 0) {
                Toast.makeText(getBaseContext(), ax + " - " + ay + " - " + az, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getBaseContext(), ax + " - " + ay + " - " + az, Toast.LENGTH_SHORT).show();
            }

            accTimer.start();
        }

    };

    public AccelerometerService() { }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        accTimer.start();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_NORMAL);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Accelerometrul  a fost dezactivat!", Toast.LENGTH_LONG).show();
        accTimer.cancel();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            ax = event.values[0];
            ay = event.values[1];
            az = event.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }
}
