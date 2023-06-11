package com.heartbit_mobile;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.heartbit_mobile.databinding.ActivityMainBinding;
import com.heartbit_mobile.ui.calendar.CalendarFragment;
import com.heartbit_mobile.ui.calendar.Programare;
import com.heartbit_mobile.ui.dashboard.DashboardFragment;
import com.heartbit_mobile.ui.home.ComunicareThread;
import com.heartbit_mobile.ui.home.HomeFragment;
import com.heartbit_mobile.ui.home.ProcesareThread;
import com.heartbit_mobile.ui.settings.SettingsFragment;
import com.heartbit_mobile.ui.support.SupportFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private Queue<String> buffer = new LinkedList<>();

    private ReentrantLock bufferLock = new ReentrantLock();
    private ArrayList<Float> listaPraguriHigh = new ArrayList<>();
    private ArrayList<Float> listaPraguriLow = new ArrayList<>();

    public ComunicareThread comunicareThread;
    public ProcesareThread procesareThread;
    private BluetoothSocket mmSocket;

    public int contorProgramari = 0, contorRecomandari = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadPraguriArray();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
        countProgramari();
        countRecomandari();
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

    public void runThreads(BluetoothSocket mmSocket, boolean isConnected) {
        this.mmSocket = mmSocket;
        comunicareThread = new ComunicareThread(mmSocket, isConnected, buffer);
        comunicareThread.start();
        Toast.makeText(MainActivity.this, "Connection established", Toast.LENGTH_SHORT).show();

        procesareThread = new ProcesareThread(this, buffer, listaPraguriHigh, listaPraguriLow);
        procesareThread.start();
    }

    public void closeThreads() {
        comunicareThread.stopThread();
        comunicareThread.cancel();
        procesareThread.stopProcessing();
    }

    public boolean isThreadsRunning() {
        if (comunicareThread != null)
            return comunicareThread.isAlive();
        else
            return false;
    }

    public void loadPraguriArray() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("path/to/praguri");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaPraguriLow.clear(); // Clear the array before adding new values
                listaPraguriHigh.clear(); // Clear the array before adding new values
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    if (childSnapshot.getKey().equals("low")) {
                        for (DataSnapshot dataSnapshot : childSnapshot.getChildren()) {
                            if (dataSnapshot.getValue() instanceof Number) {
                                Float value = ((Number) dataSnapshot.getValue()).floatValue();
                                bufferLock.lock();
                                listaPraguriLow.add(value);
                                bufferLock.unlock();
                            }
                        }
                    } else if (childSnapshot.getKey().equals("high")) {
                        for (DataSnapshot dataSnapshot : childSnapshot.getChildren()) {
                            if (dataSnapshot.getValue() instanceof Number) {
                                Float value = ((Number) dataSnapshot.getValue()).floatValue();
                                bufferLock.lock();
                                listaPraguriHigh.add(value);
                                bufferLock.unlock();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void countProgramari() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String currentDateStr = dateFormat.format(new Date());
        Date currentDateTemp;
        try {
            currentDateTemp = dateFormat.parse(currentDateStr);
        } catch (ParseException e) {
            // Handle the exception if the parsing fails
            currentDateTemp = null;
        }
        final Date currentDate = currentDateTemp;
        String userUUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("path/to/Programari/" + userUUID)
                .addValueEventListener(new ValueEventListener() {
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        contorProgramari = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            // Retrieve the data from the snapshot and do something with it
                            Programare programare = snapshot.getValue(Programare.class);
                            Date data;
                            try {
                                data = dateFormat.parse(programare.getData());
                            } catch (ParseException e) {
                                data = null;
                                throw new RuntimeException(e);
                            }
                            if (data.compareTo(currentDate) > 0) {
                                contorProgramari++;
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    public void countRecomandari() {
        String userUUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("path/to/Recomandari/" + userUUID)
                .addValueEventListener(new ValueEventListener() {
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        contorRecomandari = (int) dataSnapshot.getChildrenCount();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle the error
                    }
                });
    }

    public int getContorProgramari() {
        return contorProgramari;
    }

    public int getContorRecomandari() {
        return contorRecomandari;
    }
}