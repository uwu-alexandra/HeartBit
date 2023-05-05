package com.heartbit_mobile.ui.home;

import static android.content.ContentValues.TAG;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ComunicareThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private boolean isConnected;
    private Queue<String> bufferQueue;
    private ReentrantLock bufferLock = new ReentrantLock();

    public ComunicareThread(BluetoothSocket socket, boolean isConnected, Queue<String> bufferQueue) {
        mmSocket = socket;
        this.isConnected = isConnected;
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
       public void run() {

        byte[] buffer = new byte[1024];
        int bytes = 0; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs.
        while (isConnected) {
            try {

                buffer[bytes] = (byte) mmInStream.read();
                String readMessage;
                // If I detect a "\n" means I already read a full measurement
                if (buffer[bytes] == '\n') {
                    readMessage = new String(buffer, 0, bytes);
                    Log.e(TAG, readMessage);
                    bufferLock.lock();
                    bufferQueue.add(readMessage);
                    bufferLock.unlock();
                    bytes = 0;
                } else {
                    bytes++;
                }

            } catch (IOException e) {
                Log.d(TAG, "Input stream was disconnected", e);
                break;
            }
        }
    }

    // Call this method from the main activity to shut down the connection.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }
}
