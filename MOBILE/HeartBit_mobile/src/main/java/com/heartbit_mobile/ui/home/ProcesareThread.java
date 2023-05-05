package com.heartbit_mobile.ui.home;

import android.bluetooth.BluetoothSocket;

import java.sql.Date;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class ProcesareThread extends Thread{
    private Queue<String> bufferQueue;
    private ReentrantLock bufferLock = new ReentrantLock();
    private boolean isProcessing;

    public ProcesareThread(Queue<String>bufferQueue) {
        this.bufferQueue=bufferQueue;
        isProcessing=true;
    }

    public void stopProcessing() {
        isProcessing = false;
    }

    public void run() {
        while (isProcessing) {
            String data=null;
            bufferLock.lock();
            if(!bufferQueue.isEmpty())
            {
                data=bufferQueue.poll();
            }
            bufferLock.unlock();
            if (data != null) {
                // Procesare
                //String split in tip_data si time_stamp
                String procesare[]=data.split(";");
                String tip_data=procesare[0];
                Date time_stamp= Date.valueOf(procesare[1]);
                Data_procesata dataProcesata=new Data_procesata(tip_data,time_stamp);

                //trimitere in cloud in functie de tipul de data
                if(dataProcesata.getTip_data().compareTo("EKG")==0)
                {
                    //->trimitere in tabela cu EKG
                }
                else
                {
                    //->trimitere in tabela cu ceilalti senzori
                }
            }
        }
    }
}

