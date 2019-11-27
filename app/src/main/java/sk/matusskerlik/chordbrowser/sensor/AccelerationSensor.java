package sk.matusskerlik.chordbrowser.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.annotation.NonNull;

public class AccelerationSensor {

    private static final float THRESHOLD = 12f;
    private SensorManager sensorManager;
    private Sensor sensor;
    private float[] gravSensorVals = {0, 0, 0};

    public AccelerationSensor(@NonNull Context context) {

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void onAcceleration(final AccelerationCallback callback) {

        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if ((Math.abs(gravSensorVals[0] - event.values[0]) > THRESHOLD) ||
                        (Math.abs(gravSensorVals[1] - event.values[1]) > THRESHOLD) ||
                        (Math.abs(gravSensorVals[2] - event.values[2]) > THRESHOLD)) {
                    // WE GOT MOVE
                    callback.onAcceleration();
                }

                /*System.out.println(
                        "X=" +  Math.abs(gravSensorVals[0] - event.values[0]) +
                        " Y=" + Math.abs(gravSensorVals[1] - event.values[1]) +
                        " Z=" + Math.abs(gravSensorVals[2] - event.values[2])
                );*/

                gravSensorVals[0] = event.values[0];
                gravSensorVals[1] = event.values[1];
                gravSensorVals[2] = event.values[2];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // TODO
            }
        }, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public interface AccelerationCallback {

        void onAcceleration();
    }
}
