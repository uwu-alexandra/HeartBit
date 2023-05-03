package com.heartbit_mobile.ui.support;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.heartbit_mobile.R;

public class SupportFragment extends Fragment {
    private SupportViewModel supportViewModel;
    private LinearLayout ghidLayout;
    private LinearLayout solicitareLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportViewModel = new ViewModelProvider(this).get(SupportViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support, container, false);
        ghidLayout = view.findViewById(R.id.ghidLayout);
        solicitareLayout = view.findViewById(R.id.solicitareLayout);
        ghidLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLayoutGhidClick();
            }
        });

        solicitareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLayoutSolicitareClick();
            }
        });
        return view;
    }

    private void onLayoutGhidClick() {

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.hide(this);
        transaction.commit();

        GhidFragment ghidFragment = new GhidFragment();

        // Setează argumentele fragmentului (dacă este necesar)
        Bundle args = new Bundle();
        args.putString("argument_key", "argument_value");
        ghidFragment.setArguments(args);

        // Adaugă referința către fragmentul anterior în argumentele fragmentului nou
        Bundle newFragmentArgs = new Bundle();
        newFragmentArgs.putString("previous_fragment_tag", this.getTag());
        ghidFragment.setArguments(newFragmentArgs);

        FragmentTransaction transaction2 = getParentFragmentManager().beginTransaction();
        transaction2.add(R.id.frame_layout, ghidFragment);
        transaction2.commit();
    }

    private void onLayoutSolicitareClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_custom_solicitare, null);
        builder.setView(dialogView);

        Button cancelButton = dialogView.findViewById(R.id.returnFromSolicitareBtn);
        Button confirmButton = dialogView.findViewById(R.id.sendSolicitareBtn);
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
}
