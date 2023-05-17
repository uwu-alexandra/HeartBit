package com.heartbit_mobile;
import static android.content.ContentValues.TAG;
import static androidx.test.InstrumentationRegistry.getContext;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.heartbit_mobile.databinding.ActivityMainBinding;
import com.heartbit_mobile.ui.calendar.CalendarFragment;
import com.heartbit_mobile.ui.dashboard.DashboardFragment;
import com.heartbit_mobile.ui.home.ComunicareThread;
import com.heartbit_mobile.ui.home.HomeFragment;
import com.heartbit_mobile.ui.home.ProcesareThread;
import com.heartbit_mobile.ui.settings.SettingsFragment;
import com.heartbit_mobile.ui.support.SupportFragment;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends AppCompatActivity{
    private ActivityMainBinding binding;
    private Queue<String> buffer = new LinkedList<>();

    public ComunicareThread comunicareThread;

    public ProcesareThread procesareThread;
    private BluetoothSocket mmSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        replaceFragment(new HomeFragment());
        binding.navView.setOnItemSelectedListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.navigation_support:
                            replaceFragment(new SupportFragment());
                            break;
                        case R.id.navigation_calendar:
                            replaceFragment(new CalendarFragment());
                            break;
                        case R.id.navigation_home:
                            replaceFragment(new HomeFragment());
                            break;
                        case R.id.navigation_dashboard:
                            replaceFragment(new DashboardFragment());
                            break;
                        case R.id.navigation_settings:
                            replaceFragment(new SettingsFragment());
                            break;
                    }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public void runThreads(BluetoothSocket mmSocket,boolean isConnected)
    {
        this.mmSocket=mmSocket;
        comunicareThread = new ComunicareThread(mmSocket, isConnected, buffer);
        comunicareThread.start();
        Toast.makeText(MainActivity.this, "Connection established", Toast.LENGTH_SHORT).show();

        procesareThread = new ProcesareThread(buffer);
        procesareThread.start();
    }
    public void closeThreads()
    {
        comunicareThread.stopThread();
        comunicareThread.cancel();
        procesareThread.stopProcessing();
    }
    public boolean isThreadsRunning() {
        if(comunicareThread!=null)
            return comunicareThread.isAlive();
        else
            return false;
    }
}