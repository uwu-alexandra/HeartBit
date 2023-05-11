package com.heartbit_mobile.ui.home;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class ProcesareThread extends Thread {
    private Queue<String> bufferQueue;
    private ReentrantLock bufferLock = new ReentrantLock();
    private boolean isProcessing;
    private Activity mActivity;
    private int nr_citire = 0;

    public ProcesareThread(Queue<String> bufferQueue, Activity mActivity) {
        this.bufferQueue = bufferQueue;
        this.mActivity = mActivity;
        isProcessing = true;
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
                nr_citire++;
                String procesare[] = data.split(";");
                String identificator = procesare[0];
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy : hh.mm.ss.SSS");
                Date time_stamp = null;
                try {
                    time_stamp = (Date) simpleDateFormat.parse(procesare[1]);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                float valoare = Float.valueOf(procesare[2]);
                Data_procesata dataProcesata = new Data_procesata(identificator, time_stamp, valoare);

                //trimitere in cloud in functie de tipul de data in functie de identificator
                FirebaseDatabase.getInstance().getReference("path/to/Senzori")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(identificator)
                        .child((identificator + nr_citire))
                        .setValue(dataProcesata)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Dacă programarea a fost salvată cu succes, afișați un mesaj și închideți dialogul
                                    Toast.makeText(mActivity, "Eroare la procesare", Toast.LENGTH_SHORT).show();
                                } else {
                                    // În caz contrar, afișați un mesaj de eroare
                                    Toast.makeText(mActivity, "Eroare la inserarea in cloud! Încercați din nou mai târziu.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // În cazul în care apariție o eroare, afișați un mesaj corespunzător
                                Toast.makeText(mActivity, "Eroare la procesare", Toast.LENGTH_SHORT).show();
                                Log.w(TAG, "Eroare la procesare", e);
                            }
                        });
            }
        }
    }
}


