package com.heartbit_mobile.ui.home;

import static android.content.ContentValues.TAG;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ComunicareThread extends Thread{
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private boolean isConnected;
    private String valueRead;

    public ComunicareThread(BluetoothSocket socket,boolean isConnected) {
        mmSocket = socket;
        this.isConnected=isConnected;
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

    public String getValueRead(){
        return valueRead;
    }
    public void run() {

        byte[] buffer = new byte[1024];
        int bytes = 0; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs.
        //We just want to get 1 temperature readings from the Arduino
        while (isConnected) {
            try {

                buffer[bytes] = (byte) mmInStream.read();
                String readMessage;
                // If I detect a "\n" means I already read a full measurement
                if (buffer[bytes] == '\n') {
                    readMessage = new String(buffer, 0, bytes);
                    Log.e(TAG, readMessage);
                    //Value to be read by the Observer streamed by the Obervable
                    valueRead=readMessage;
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
