package com.heartbit_mobile.ui.home;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;


import com.heartbit_mobile.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ComunicareThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private boolean isConnected;
    private Queue<String> bufferQueue;
    private ReentrantLock bufferLock = new ReentrantLock();
    private Activity mActivity; //temporar
    private boolean isRunning;

    public ComunicareThread(BluetoothSocket socket, boolean isConnected, Queue<String> bufferQueue) {
        mmSocket = socket;
        this.isRunning = isConnected;
        this.bufferQueue = bufferQueue;
        InputStream tmpIn = null;

        // Get the input and output streams; using temp objects because
        // member streams are final.
        try {
            tmpIn = socket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating input stream", e);
        }

        //Input and Output streams members of the class
        mmInStream = tmpIn;
    }
    public void stopThread() {
        isRunning = false;
    }
    public void run() {

        byte[] buffer = new byte[1024];
        int bytes = 0; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs.
        while (isRunning) {
            try {
                if (mmInStream.available() > 0) {
                    buffer[bytes] = (byte) mmInStream.read();
                    String readMessage;
                    // If I detect a "\n" means I already read a full measurement
                    if (buffer[bytes] == '\n') {
                        readMessage = new String(buffer, 0, bytes);
                        Log.e(TAG, readMessage);
                        bufferLock.lock();
                        bufferQueue.add(readMessage);
                        bufferLock.unlock();
                        Arrays.fill(buffer, (byte) 0); //resetare buffer pentru a preveni reziduuri
                        bytes = 0;
                        String message = "The size of the array is " + buffer.length;
                    } else {
                        bytes++;
                    }

                }
            } catch (IOException e) {
                Log.e(TAG, "Error reading from InputStream: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }


    // Call this method from the main activity to shut down the connection.
    public void cancel() {
        try {
            mmInStream.close();
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }
}
