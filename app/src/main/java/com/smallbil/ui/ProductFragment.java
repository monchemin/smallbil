package com.smallbil.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.smallbil.R;
import com.smallbil.repository.AppDatabase;
import com.smallbil.repository.Product;
import com.smallbil.service.ProductService;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment implements BarCodeRequest  {

    private OnFragmentInteractionListener mListener;
    private TextInputEditText currentQuantity, code, name, currentAmount, newAmount, newQuantity, addQuantity;
    private MaterialButton scannerButton, addButton;
    private ProductService service;


    public ProductFragment() {

    }

    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_product, container, false);
        currentQuantity = view.findViewById(R.id.product_current_quantity);
        code = view.findViewById(R.id.product_code_view);
        name = view.findViewById(R.id.product_name);
        currentAmount = view.findViewById(R.id.product_current_amount);
        currentQuantity = view.findViewById(R.id.product_current_quantity);
        newAmount = view.findViewById(R.id.product_new_amount);
        newQuantity = view.findViewById(R.id.product_new_quantity);
        scannerButton = view.findViewById(R.id.scanner_button);
        addButton = view.findViewById(R.id.add_product_button);
        addQuantity = view.findViewById(R.id.product_add_quantity);
        currentQuantity.setEnabled(false);
        currentAmount.setEnabled(false);
        scannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.readBarCode();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upsert();
            }
        });

        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Toast.makeText(getContext(), "Scanned: "+ s, Toast.LENGTH_LONG).show();
                getProductByCode(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onBarCodeRead(String barCode) {
        code.setText(barCode);
    }

    public void setDao(AppDatabase db) {
        service = new ProductService(db);
    }

    public void getProductByCode(String code) {
       Product current = service.getProductByCode(code);
       if (current != null) {
           name.setText(current.name);
       }
    }

    protected void upsert() {
        String mcode = code.getText().toString();
        String mname = name.getText().toString();
        int quantity = 0;
        double amount = 0;
        try {
            quantity =  Integer.parseInt(newQuantity.getText().toString());
            amount = Double.parseDouble(newAmount.getText().toString());
        } catch (NumberFormatException ex) {
            newAmount.setHint("required");
            addQuantity.setHint("required");
        }

      long a =  service.upsert(mcode, mname, quantity, amount);
        Toast.makeText(getContext(), "retour: "+ a, Toast.LENGTH_LONG).show();
    }



}
