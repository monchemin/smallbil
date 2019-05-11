package com.smallbil.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.smallbil.R;
import com.smallbil.constants.AppConstants;
import com.smallbil.repository.AppDatabase;



public class SettingsFragment extends BaseFragment {

    TextInputEditText customerNumber, redThreshold, yellowThreshold;

    public SettingsFragment() {
        // Required empty public constructor
    }


    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        customerNumber = view.findViewById(R.id.customer_number);
        redThreshold = view.findViewById(R.id.red_threshold);
        yellowThreshold = view.findViewById(R.id.yellow_threshold);
        MaterialButton mButton = view.findViewById(R.id.add_settings);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });

        getSettings();
        return view;
    }

    private void getSettings() {
        Context context = getContext();
        SharedPreferences settings =  context.getSharedPreferences(getString(R.string.preference_file_key), context.MODE_PRIVATE);
        //SharedPreferences.Editor edit = settings.edit();
        customerNumber.setText(settings.getString(AppConstants.CUSTOMER_NUMBER, ""));
        redThreshold.setText(String.valueOf(settings.getInt(AppConstants.RED_THRESHOLD, AppConstants.DEFAULT_RED_THRESHOLD)));
        yellowThreshold.setText(String.valueOf(settings.getInt(AppConstants.YELLOW_TRESHOLD, AppConstants.DEFAULT_YELLOW_TRESHOLD)) );
    }

    private void saveSettings() {
        Context context = getContext();
        SharedPreferences settings =  context.getSharedPreferences(getString(R.string.preference_file_key), context.MODE_PRIVATE);
        SharedPreferences.Editor edit = settings.edit();
        if (customerNumber.getText() != null) {
            edit.putString(AppConstants.CUSTOMER_NUMBER, customerNumber.getText().toString());
        }
        try {
            if(redThreshold.getText() != null)
            {
                edit.putInt(AppConstants.RED_THRESHOLD, Integer.parseInt(redThreshold.getText().toString()));
            }
            if(yellowThreshold.getText() != null)
            {
                edit.putInt(AppConstants.YELLOW_TRESHOLD, Integer.parseInt(yellowThreshold.getText().toString()));
            }
        } catch (NumberFormatException ex) {
            Toast.makeText(getContext(), R.string.not_number, Toast.LENGTH_LONG).show();
        }

        edit.apply();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    void setDao(AppDatabase db) {

    }

    @Override
    void onBarCodeRead(String barCode) {

    }
}
