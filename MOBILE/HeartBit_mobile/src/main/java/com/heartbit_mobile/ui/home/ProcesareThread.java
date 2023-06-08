package com.heartbit_mobile.ui.home;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class ProcesareThread extends Thread {

    private float[] gravity = new float[3];
    private float[] linear_acceleration = new float[3];
    private Queue<String> bufferQueue;
    private ArrayList<Float> high;
    private ArrayList<Float> low;
    private ReentrantLock bufferLock = new ReentrantLock();
    private boolean isProcessing;
    private boolean isRunning = false;
    private Context mContext;

    public ProcesareThread(Context context, Queue<String> bufferQueue, ArrayList<Float> high, ArrayList<Float> low) {
        this.mContext = context.getApplicationContext();
        this.bufferQueue = bufferQueue;
        this.high = high;
        this.low = low;
        isProcessing = true;

        SensorManager sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    public void stopProcessing() {
        isProcessing = false;
    }

    public void run() {
        while (isProcessing) {

            String data = null;
            bufferLock.lock();
            if (!bufferQueue.isEmpty()) {
                data = bufferQueue.poll();
            }
            bufferLock.unlock();
            if (data != null) {
                //Procesare
                //String split in tip_data si time_stamp
                data = data.replaceAll("[\r]", "");
                String procesare[] = data.split(";");
                if (procesare.length == 2) {
                    String identificator = procesare[0];
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy:HH.mm.ss.SSS");
                    String formattedDateTime = formatter.format(currentDateTime);
                    float valoare = Float.valueOf(procesare[1]);
                    boolean alerta = false;
                    if (!isRunning) {
                        switch (identificator) {
                            case "EKG":
                                if (valoare > high.get(0) || valoare < low.get(0)) alerta = true;
                                break;
                            case "PULS":
                                if (valoare > high.get(1) || valoare < low.get(1)) alerta = true;
                                break;
                            case "TEMP":
                                if (valoare > high.get(2) || valoare < low.get(2)) alerta = true;
                                break;
                            case "UMD":
                                if (valoare > high.get(3) || valoare < low.get(3)) alerta = true;
                                break;
                        }
                    }
                    Data_procesata dataProcesata = new Data_procesata(identificator, formattedDateTime, valoare, alerta);
                    if (!(dataProcesata.getDenumire().equals("EROARE") || (dataProcesata.getValoare() == 999))) {
                        //trimitere in cloud in functie de tipul de data in functie de identificator
                        FirebaseDatabase.getInstance().getReference("path/to/Senzori")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child(identificator)
                                .push()
                                .setValue(dataProcesata)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Dacă programarea a fost salvată cu succes, afișați un mesaj și închideți dialogul
                                        } else {
                                            // În caz contrar, afișați un mesaj de eroare
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // În cazul în care apariție o eroare, afișați un mesaj corespunzător
                                        Log.w(TAG, "Eroare la procesare", e);
                                    }
                                });
                    }
                }
            }
        }
    }

    private final SensorEventListener accelerometerListener = new SensorEventListener() {
        private static final float RUNNING_THRESHOLD = 10.0f; // Adjust this threshold as needed

        @Override
        public void onSensorChanged(@NonNull SensorEvent event) {
            // alpha is calculated as t / (t + dT)
            // with t, the low-pass filter's time-constant
            // and dT, the event delivery rate

            final float alpha = (float) 0.8;

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            gravity[0] = alpha * gravity[0] + (1 - alpha) * x;
            gravity[1] = alpha * gravity[1] + (1 - alpha) * y;
            gravity[2] = alpha * gravity[2] + (1 - alpha) * z;

            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];

            float magnitude = (float) Math.sqrt(
                    linear_acceleration[0] * linear_acceleration[0]
                            + linear_acceleration[1] * linear_acceleration[1]
                            + linear_acceleration[2] * linear_acceleration[2]
            );


            // Check if the magnitude exceeds the running threshold
            if (magnitude > RUNNING_THRESHOLD) {
                // Person is likely running
                // Perform further actions or set a flag to indicate running
                isRunning = true;
            } else {
                // Person is not running
                // Perform actions for non-running state
                isRunning = false;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Handle accuracy changes if needed
        }
    };
}


