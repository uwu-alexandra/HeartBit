package com.heartbit_mobile.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.heartbit_mobile.R;
import com.heartbit_mobile.ui.home.Data_procesata;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DashboardFragment extends Fragment /*implements OnChartGestureListener, OnChartValueSelectedListener*/ {

    private static final String TAG = "DashBoard";
    private LineChart lineChart;
    Spinner dataOptionsSpinner;
    Button obtineIstoricBtn;
    private LineDataSet lineDataSet = new LineDataSet(null, "Readings");
    private ArrayList<ILineDataSet>iLineDataSets=new ArrayList<>();
    private LineData lineData;
    int desiredVisibleRange = 10; // Number of data entries to display

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        lineChart = view.findViewById(R.id.lineChart);



        ArrayList<Entry> yValues = new ArrayList<>();
        LineDataSet set = new LineDataSet(yValues, "Data set 1");

        set.setFillAlpha(120);
        dataOptionsSpinner = view.findViewById(R.id.dataOptions_spinner);
        obtineIstoricBtn=view.findViewById(R.id.obtineIstoricBtn);

      obtineIstoricBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              getData();
          }
      });
        return view;
    }

    public void getData() {
        String dataOptions = dataOptionsSpinner.getSelectedItem().toString();
        String identificator = "";
        switch (dataOptions) {
            case "Puls":
                identificator = "PULS";
                break;
            case "Umiditate":
                identificator = "UMD";
                break;
            case "EKG":
                identificator = "EKG";
                break;
            case "Temperatura":
                identificator = "TEMP";
                break;
        }
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference myRef = firebaseDatabase.getReference("path/to/Senzori/" + UID + "/" + identificator);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Entry> dataValues = new ArrayList<>();
                if (snapshot.hasChildren()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Data_procesata dataProcesata = dataSnapshot.getValue(Data_procesata.class);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy:HH.mm.ss.SSS");
                        Date date;
                        try {
                            date = sdf.parse(dataProcesata.getTime_stamp());
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }

                        // Extract hours, minutes, seconds, and milliseconds from the date
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutes = calendar.get(Calendar.MINUTE);
                        int seconds = calendar.get(Calendar.SECOND);
                        int milliseconds = calendar.get(Calendar.MILLISECOND);

                        // Convert hours, minutes, seconds, and milliseconds to milliseconds
                        long timestampMillis = (hours * 3600 + minutes * 60 + seconds) * 1000 + milliseconds;
                        float xValue = (float) (timestampMillis / 1000.0);
                        float yValue = Float.valueOf(dataProcesata.getValoare());
                        dataValues.add(new Entry(xValue, yValue)); // Create Entry object directly
                    }
                    showChart(dataValues);
                } else {
                    lineChart.clear();
                    lineChart.invalidate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void showChart(ArrayList dataValues) {
        lineDataSet.setValues(dataValues);
        iLineDataSets.clear();
        iLineDataSets.add(lineDataSet);
        lineData=new LineData(iLineDataSets);
        lineChart.clear();
        lineChart.setData(lineData);
        lineChart.animateX(500,Easing.EaseInBounce);

        // Move the view to the latest entry
        lineChart.moveViewToX(lineData.getXMax());
        lineDataSet.setColor(Color.RED);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        lineChart.invalidate();
    }
}