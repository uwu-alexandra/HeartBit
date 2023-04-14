package com.heartbit_mobile.ui.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.heartbit_mobile.R;
import com.heartbit_mobile.ui.logare.Login;
import com.heartbit_mobile.ui.support.SolicitareFragment;

public class SettingsFragment extends Fragment {

    private LinearLayout schimbareParolaLayout;
    private LinearLayout deconectareContLayout;
    private LinearLayout stergereContLayout;
    private LinearLayout permisiuneBluetoothLayout;
    private LinearLayout cadruLegalLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        schimbareParolaLayout = view.findViewById(R.id.schimbareParolaLayout);
        deconectareContLayout = view.findViewById(R.id.deconectareContLayout);
        stergereContLayout = view.findViewById(R.id.stergereContLayout);
        permisiuneBluetoothLayout = view.findViewById(R.id.permisiuneBluetoothLayout);
        cadruLegalLayout = view.findViewById(R.id.cadruLegalLayout);

        schimbareParolaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickParolaLayout();
            }
        });
        deconectareContLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDeconectareContLayout();
            }
        });

        stergereContLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickStergereContLayout();
            }
        });
        permisiuneBluetoothLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBluetoothLayout();
            }
        });

        cadruLegalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCandruLegalLayout();
            }
        });

        return view;
    }

    private void onClickParolaLayout() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.hide(this);
        transaction.commit();

        FragmentTransaction transaction2 = getParentFragmentManager().beginTransaction();
        transaction2.add(R.id.frame_layout, new SchimbareParolaFragment());
        transaction2.commit();
    }

    private void onClickDeconectareContLayout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.logout_message)
                .setTitle(R.string.logout_title)
                .setPositiveButton(R.string.logout_positive_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // deconectați utilizatorul aici
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(), Login.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                })
                .setNegativeButton(R.string.logout_negative_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // închideți dialogul fără a face nimic
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onClickStergereContLayout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.stergere_message)
                .setTitle(R.string.stergere_title)
                .setPositiveButton(R.string.stergere_positive_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // stergeti utilizatorul aici
                    }
                })
                .setNegativeButton(R.string.stergere_negative_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // închideți dialogul fără a face nimic
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onClickBluetoothLayout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_custom_bluetooth, null);
        builder.setView(dialogView);

        Button cancelButton = dialogView.findViewById(R.id.bluetooth_dialog_cancel_button);
        Button confirmButton = dialogView.findViewById(R.id.bluetooth_dialog_confirm_button);
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

    private void onClickCandruLegalLayout() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.hide(this);
        transaction.commit();

        FragmentTransaction transaction2 = getParentFragmentManager().beginTransaction();
        transaction2.add(R.id.frame_layout, new CadruLegalFragment());
        transaction2.commit();
    }
}