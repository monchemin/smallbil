package com.smallbil.ui;

import androidx.fragment.app.Fragment;

import com.smallbil.repository.AppDatabase;

public abstract class BaseFragment extends Fragment {
    abstract void setDao(AppDatabase db);
    abstract void onBarCodeRead(String barCode);
}
