package com.heartbit_mobile.ui.calendar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heartbit_mobile.R;


public class CalendarFragment extends Fragment {
    private CalendarViewModel calendarViewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendarViewModel=new ViewModelProvider(this).get(CalendarViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_calendar,container,false);
        return view;
    }
}