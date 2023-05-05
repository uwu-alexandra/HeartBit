package com.heartbit_mobile.ui.home;

import android.bluetooth.BluetoothSocket;

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
                // Procesare -> trimitere in Cloud
            }
        }
    }
}


