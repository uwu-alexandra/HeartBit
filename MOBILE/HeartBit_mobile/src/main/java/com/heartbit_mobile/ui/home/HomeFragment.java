package com.heartbit_mobile.ui.home;

import static android.content.ContentValues.TAG;
import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.heartbit_mobile.MainActivity;
import com.heartbit_mobile.R;
import com.heartbit_mobile.databinding.FragmentHomeBinding;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class HomeFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            this.mainActivity = (MainActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SocketConnectionListener");
        }
    }

    // Clear the listener when the fragment detaches from the activity
    @Override
    public void onDetach() {
        super.onDetach();

    }

    private FragmentHomeBinding binding;
    private boolean isConnected;
    public static final int REQUEST_BLUETOOTH_PERMISSION = 1;
    BluetoothDevice arduinoBTModule = null;
    UUID arduinoUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    boolean gasit = false;
    boolean conectat = false;
    //private Queue<String> buffer = new LinkedList<>();
    private MainActivity mainActivity;
    private BluetoothSocket mmSocket;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        //View root = binding.getRoot();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        isConnected= mainActivity.isThreadsRunning();

        //setare stare buton in caz de fragment exchange

        Button connectBtn = view.findViewById(R.id.connectBtn);

        if(isConnected)
        {
            connectBtn.setText("Deconecteaza device wearable");
            connectBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
        }
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (!isConnected) {
                    // Connect the device
                    isConnected = true;

                    // Add code to connect device here

                    // Check if we have the necessary permissions
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                        // If we don't have the necessary permissions, request them
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                                REQUEST_BLUETOOTH_PERMISSION);
                    }
                    //Intances of BT Manager and BT Adapter needed to work with BT in Android.
                    BluetoothManager bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
                    BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

                    //verificare STATE de adaugat
                    Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                    if (pairedDevices.size() > 0) {
                        // There are paired devices. Get the name and address of each paired device.
                        for (BluetoothDevice device : pairedDevices) {
                            String deviceName = device.getName();
                            String deviceHardwareAddress = device.getAddress(); // MAC address

                            if (deviceName.equals("HC-05")) { //HC-05 gasit -> putem sa incepem comunicarea intre dispozitive
                                arduinoUUID = device.getUuids()[0].getUuid();
                                arduinoBTModule = device;
                                gasit = true;
                                Toast.makeText(getContext(), "Found", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (gasit == true) {
                            BluetoothSocket tmp = null;
                            try {
                                tmp = arduinoBTModule.createRfcommSocketToServiceRecord(arduinoUUID);
                            } catch (IOException e) {
                                Log.e(TAG, "Socket's create() method failed", e);
                            }
                            mmSocket = tmp;
                        }
                        Toast.makeText(getContext(), "Attempt connection", Toast.LENGTH_SHORT).show();
                        //Check if Socket connected
                        boolean check = false;
                        while (!check) {
                            try {
                                // Connect to the remote device through the socket. This call blocks
                                // until it succeeds or throws an exception.
                                mmSocket.connect();
                                check = mmSocket.isConnected();
                            } catch (IOException connectException) {
                                // Unable to connect; close the socket and return.
                                Toast.makeText(getActivity(), connectException.toString(), Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "connectException: " + connectException);

                                try {
                                    mmSocket.close();
                                } catch (IOException closeException) {
                                    Toast.makeText(getActivity(), closeException.toString(), Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "Could not close the client socket", closeException);
                                }
                            }
                            if (check) {
                                conectat = true;
                                if (mainActivity != null) {
                                    // Call the method in MainActivity
                                    mainActivity.runThreads(mmSocket, isConnected);
                                }

                            } else
                                Toast.makeText(getContext(), "Connection failed", Toast.LENGTH_SHORT).show();
                        }
                        if (gasit == false) {
                            Toast.makeText(getContext(), "Dispozitivul wearable nu a fost detectat. Verificaţi dacă dispozitivul este conectat", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Niciun dispozitiv conectat. Conectaţi dispozitivul wearable şi încercaţi din nou.", Toast.LENGTH_SHORT).show();
                    }

                    // Delay the button state change for 100ms
                    if (conectat) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                connectBtn.setText("Deconecteaza device wearable");
                                connectBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
                            }
                        }, 100);
                    } else
                        isConnected = false;
                } else {
                    // Disconnect the device
                    isConnected = false;
                    // Delay the button state change for 100ms
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            connectBtn.setText("Conecteaza device wearable");
                            connectBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green));
                        }
                    }, 100);

                    // Add code to disconnect device here
                    if (mainActivity.comunicareThread != null) {
                        mainActivity.closeThreads();
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}