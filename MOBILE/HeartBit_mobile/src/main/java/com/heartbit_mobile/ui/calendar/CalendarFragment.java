package com.heartbit_mobile.ui.calendar;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.heartbit_mobile.R;


public class CalendarFragment extends Fragment {
    private CalendarViewModel calendarViewModel;
    private LinearLayout programareLayout;
    private LinearLayout recomandariLayout;

    private Button pastBtn;
    private Button futureBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendarViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        programareLayout = view.findViewById(R.id.programareLayout);
        recomandariLayout = view.findViewById(R.id.recomandariLayout);
        pastBtn = view.findViewById(R.id.pastBtn);
        futureBtn = view.findViewById(R.id.futureBtn);
        programareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLayoutProgramareClick();
            }
        });

        recomandariLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLayoutRecomandariClick();
            }
        });

        pastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPastBtnClick();
            }
        });

        futureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFutureBtnClick();
            }
        });
        return view;
    }

    private void onLayoutProgramareClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_custom_programare, null);
        builder.setView(dialogView);

        Button cancelButton = dialogView.findViewById(R.id.returnFromProgramareBtn);
        Button confirmButton = dialogView.findViewById(R.id.sendCerereBtn);
        AlertDialog dialog = builder.create();
// Setarea listenerilor de click pentru butoane
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cod pentru anularea acțiunii
                dialog.dismiss();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cod pentru confirmarea acțiunii
            }
        });
        dialog.show();
    }


    private void onLayoutRecomandariClick() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.hide(this);
        transaction.commit();

        FragmentTransaction transaction2 = getParentFragmentManager().beginTransaction();
        transaction2.add(R.id.frame_layout, new RecomandariFragment());
        transaction2.commit();
    }

    private void onPastBtnClick() {
        pastBtn.setClickable(false);
        pastBtn.setBackgroundColor(Color.WHITE);
        futureBtn.setClickable(true);
        futureBtn.setBackgroundColor(Color.GRAY);
        afisareDateIstoric();
    }

    private void onFutureBtnClick() {
        pastBtn.setClickable(true);
        pastBtn.setBackgroundColor(Color.GRAY);
        futureBtn.setClickable(false);
        futureBtn.setBackgroundColor(Color.WHITE);
        afisareDateViitoare();
    }

    private void afisareDateIstoric() {

    }

    private void afisareDateViitoare() {

    }
}