package com.heartbit_mobile.ui.calendar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.heartbit_mobile.R;

public class ProgramareFragment extends Fragment {

    private Button sendBtn;
    private Button returnBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_programare,container,false);
        sendBtn=view.findViewById(R.id.sendCerereBtn);
        returnBtn=view.findViewById(R.id.returnFromProgramareBtn);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReturnClick();
            }
        });
        return view;
    }
    private void onReturnClick()
    {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.remove(this);
        //transaction.show(previousFragment);
        transaction.commit();
    }
}