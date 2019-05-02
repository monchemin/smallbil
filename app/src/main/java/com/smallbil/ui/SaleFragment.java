package com.smallbil.ui;

import android.content.Context;
import android.icu.text.SymbolTable;
import android.os.Bundle;
import android.util.Log;
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
import com.smallbil.repository.entities.Order;
import com.smallbil.repository.entities.OrderDetail;
import com.smallbil.repository.entities.Product;
import com.smallbil.service.OrderService;
import com.smallbil.service.ProductService;
import com.smallbil.service.ServiceResponse;

import java.util.ArrayList;
import java.util.List;

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
    private int position;
    private List<Product> productList = new ArrayList<>();
    private AppDatabase db;

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            position = viewHolder.getAdapterPosition();
            loadItem();
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
                total.setText(String.valueOf(getItemsAmount()));
            }
        });

        MaterialButton saveButton = view.findViewById(R.id.sale_btn_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave();
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
        this.db = db;
        service = new ProductService(db);
    }

    private void loadItem() {
        Product item = adapter.getItem(position);
        if(item != null) {
            name.setText(item.name);
            quantity.setText(String.valueOf(item.saleQuantity));
        }
    }

    private void onItemChange() {
        if (quantity.getText() != null)
            updateList(position, quantity.getText().toString());
    }

    @Override
    public void onBarCodeRead(String barCode) {
        Toast.makeText(getContext(), barCode, Toast.LENGTH_LONG).show();
        if(barCode != null){
            service.getProductByCode(barCode).observe(this, new Observer<Product>() {
                @Override
                public void onChanged(@Nullable Product product) {
                    if(product != null && product.quantity > 0) {
                        addProduct(product);
                    }
                    else {
                        Toast.makeText(getContext(), R.string.not_stock, Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }

    private void addProduct(Product product) {
        product.saleQuantity = 1;
        Boolean inList = false;
        for(Product prod: productList) {
            if (prod.code.equals(product.code)){
                inList = true;
                if (product.quantity > product.saleQuantity)
                    prod.saleQuantity++;
                else
                    Toast.makeText(getContext(), R.string.less_stock, Toast.LENGTH_LONG).show();
                break;
            }
        }
        if(!inList) productList.add(product);
        adapter.setProductList(productList);
    }

    private void updateList(int position, String quantity) {
        try {
            if(productList.get(position).quantity > Integer.parseInt(quantity))
                productList.get(position).saleQuantity = Integer.parseInt(quantity);
            else
                Toast.makeText(getContext(), R.string.less_stock, Toast.LENGTH_LONG).show();
        }
        catch (NumberFormatException ex) {
            productList.remove(position);
        }
        finally {
           adapter.setProductList(productList);
        }
    }

    private double getItemsAmount() {
        if(productList == null) return 0;
        double total = 0;
        for(Product prod: productList) {
            total += prod.amount*prod.saleQuantity;
        }
        return total;
    }

    private void doSave() {
        OrderService orderService = new OrderService(db);
        orderService.getOrders().observe(this, new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                Log.d("SMB", "order :" + orders.size());
                for(Order or: orders) {
                    Log.d("SMB", or.orderNumber + " : " + or.orderDate);
                }
            }
        });

        orderService.getDetails().observe(this, new Observer<List<OrderDetail>>() {
            @Override
            public void onChanged(List<OrderDetail> orderDetails) {
                Log.d("SMB", "order detail :" + orderDetails.size());
                for(OrderDetail or: orderDetails) {
                    Log.d("SMB", or.orderNumber + " : " + or.productCode);
                }
            }
        });

        if (productList.size() == 0) {
            Log.d("SMB", "list null");
            return;
        }

       OrderService.WriteOrder writeOrder = orderService.write(productList);
       writeOrder.setCallback(new ServiceResponse() {
           @Override
           public void didFinish(Boolean result) {
               if (result) Toast.makeText(getContext(), R.string.operation_success, Toast.LENGTH_LONG).show();
               else {
                   productList.clear();
                   adapter.setProductList(productList);
                   Toast.makeText(getContext(), R.string.operation_failed, Toast.LENGTH_LONG).show();
               }
           }
       });
       writeOrder.execute();
    }
}
