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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class ProcesareThread extends Thread {
    private Queue<String> bufferQueue;
    private ReentrantLock bufferLock = new ReentrantLock();
    private boolean isProcessing;
    private Activity mActivity;

    public ProcesareThread(Queue<String> bufferQueue) {
        this.bufferQueue = bufferQueue;
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
                data = data.replaceAll("[\r]", "");
                String procesare[] = data.split(";");
                if (procesare.length == 2) {
                    String identificator = procesare[0];
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy:HH.mm.ss.SSS");
                    String formattedDateTime = formatter.format(currentDateTime);
                    float valoare = Float.valueOf(procesare[1]);
                    Data_procesata dataProcesata = new Data_procesata(identificator, formattedDateTime, valoare);
                    if (!(dataProcesata.getDenumire().equals("EROARE"))) {
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
}


