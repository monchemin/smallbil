package com.smallbil.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.smallbil.R;
import com.smallbil.adapters.ProductListAdapter;
import com.smallbil.repository.AppDatabase;
import com.smallbil.repository.Product;
import com.smallbil.service.ProductService;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BarCodeRequest} interface
 * to handle interaction events.
 * Use the {@link SaleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaleFragment extends Fragment implements BarCodeRequest {
    private OnFragmentInteractionListener mListener;
    private ProductService service;
    private ProductListAdapter adapter;
    private TextInputEditText name, quantity, total;
    private int i = 0;
    private int position;

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            position = viewHolder.getAdapterPosition();
            loadItem();
            //Toast.makeText(getContext(), "You Clicked: " + position, Toast.LENGTH_SHORT).show();
        }
    };

    public SaleFragment() {

    }



    public static SaleFragment newInstance(String param1, String param2) {
        SaleFragment fragment = new SaleFragment();
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
        View view = inflater.inflate(R.layout.fragment_sale, container, false);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.sale_floating_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.readBarCode();
                /*i++;
                Toast.makeText(getContext(), ""+i, Toast.LENGTH_LONG).show();
                Product p = new Product();
                p.code = String.valueOf(i);
                p.name = "name"+i;
                p.amount = 1+i;
                adapter.addProduct(p);*/
            }
        });
        name = view.findViewById(R.id.sale_current_name);
        name.setEnabled(false);
        quantity = view.findViewById(R.id.sale_current_quantity);
        MaterialButton mButton = view.findViewById(R.id.sale_change_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemChange();
            }
        });
        total = view.findViewById(R.id.sale_total_amount);
        MaterialButton totalButton = view.findViewById(R.id.sale_btn_total);
        totalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total.setText(String.valueOf(adapter.getItemsAmount()));
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.sale_product_list);
         adapter = new ProductListAdapter();
         recyclerView.setAdapter(adapter);
         recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
         adapter.setOnItemClickListener(onItemClickListener);
         return view;
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

    public void setDao(AppDatabase db) {
        service = new ProductService(db);
    }

    private void loadItem() {

        Product item = adapter.getItem(position);
        if(item != null) {
            name.setText(item.name);
            quantity.setText(String.valueOf(item.quantity));
        }
    }

    private void onItemChange() {

            adapter.updateList(position, quantity.getText().toString());
    }

    @Override
    public void onBarCodeRead(String barCode) {
        Toast.makeText(getContext(), barCode, Toast.LENGTH_LONG).show();
        if(barCode != null){
            service.getProductByCode(barCode).observe(this, new Observer<Product>() {
                @Override
                public void onChanged(@Nullable Product product) {
                    if(product != null) {
                        adapter.addProduct(product);
                    }
                    else {
                        Toast.makeText(getContext(), R.string.operation_failed, Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }
}
