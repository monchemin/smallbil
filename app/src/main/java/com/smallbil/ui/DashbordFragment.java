package com.smallbil.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.textfield.TextInputEditText;
import com.smallbil.R;
import com.smallbil.adapters.ProductListAdapter;
import com.smallbil.constants.AppConstants;
import com.smallbil.repository.AppDatabase;
import com.smallbil.repository.entities.Order;
import com.smallbil.repository.entities.Product;
import com.smallbil.service.LocalStorageService;
import com.smallbil.service.OrderService;
import com.smallbil.service.ProductService;
import com.smallbil.utils.DateUtils;
import com.smallbil.utils.MyPieDataSet;
import com.smallbil.constants.TresholdEnum;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class DashbordFragment extends BaseFragment {
    private PieChart pieChart;
    private ProductService productService;
    private OrderService orderService;
    private List<Product> thresholdProducts;
    private List<Product> redList = new ArrayList<>(), yellowList = new ArrayList<>();
    private int redThreshold, yellowThreshold;
    private ProductListAdapter adapter;
    private TextInputEditText dayAccount, weekAccount, monthAccount;

    public DashbordFragment() {
        // Required empty public constructor
    }


    public static DashbordFragment newInstance(String param1, String param2) {
        DashbordFragment fragment = new DashbordFragment();
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
        View view = inflater.inflate(R.layout.fragment_dashbord, container, false);
        pieChart = view.findViewById(R.id.piechart);

        RecyclerView recyclerView = view.findViewById(R.id.threshold_product_list);
        adapter = new ProductListAdapter(1);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dayAccount =  view.findViewById(R.id.day_account);
        weekAccount =  view.findViewById(R.id.week_account);
        monthAccount =  view.findViewById(R.id.month_account);
        dayAccount.setEnabled(false);
        monthAccount.setEnabled(false);
        weekAccount.setEnabled(false);
        return view;

    }

    private void critacalProductChart(ArrayList<PieEntry> rawData) {
        PieDataSet dataSet = new MyPieDataSet(rawData, getString(R.string.threshold_legend));
        PieData data = new PieData();
        data.setDataSet(dataSet);
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.WHITE);

        // In Percentage
        //data.setValueFormatter(new PercentFormatter());
        // Default value
       /* data.setValueFormatter(new ValueFormatter() {
                                   @Override
                                   public String getPieLabel(float value, PieEntry pieEntry) {
                                       return String.valueOf(pieEntry.getValue());
                                   }
                               }
        ); */
        pieChart.setData(data);
        pieChart.setDescription(null);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(58f);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setHoleRadius(58f);
        pieChart.getLegend().setEnabled(true);
        pieChart.setDrawEntryLabels(false);
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                String pieClick =  e.getData().toString();
                if(pieClick.equals(TresholdEnum.YELLOW.toString())) {
                    adapter.setProductList(yellowList);
                } else {
                    adapter.setProductList(redList);
                }

            }

            @Override
            public void onNothingSelected() {

            }
        });
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();
        //dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //getThresholdProducts();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    //    mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        getThresholdProducts();
        getAccounts();
       // DateUtils.getWeek();
        //getDayOrders();
    }

    private void getThresholdProducts() {
        setThreshold();
        productService.getThresholdProduct(yellowThreshold).observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                thresholdProducts = products;
                makePieList();
            }
        });
    }

    private void makePieList() {
        if (thresholdProducts == null || thresholdProducts.size()==0) return;
        int red = 0, yellow = 0;
        for (Product prod: thresholdProducts){
            if(prod.quantity <= redThreshold) {
                red++;
                redList.add(prod);
            }
            else {
                yellow++;
                yellowList.add(prod);
            }
        }
        ArrayList<PieEntry> chartData = new ArrayList<>();
        if(red > 0) {
            chartData.add(new PieEntry(red, TresholdEnum.RED.toString(), TresholdEnum.RED));
        }
        if(yellow > 0) {
            chartData.add(new PieEntry(yellow, TresholdEnum.YELLOW.toString(), TresholdEnum.YELLOW));
        }
        if( chartData.size() > 0) {
            critacalProductChart(chartData);
        }
    }

    private void setThreshold() {
        SharedPreferences settings = LocalStorageService.getPreferences(getContext(), getString(R.string.preference_file_key));
        yellowThreshold = settings.getInt(AppConstants.YELLOW_TRESHOLD, AppConstants.DEFAULT_YELLOW_TRESHOLD);
        redThreshold = settings.getInt(AppConstants.RED_THRESHOLD, AppConstants.DEFAULT_RED_THRESHOLD);
    }

    @Override
    void setDao(AppDatabase db) {
        productService = new ProductService(db);
        orderService = new OrderService(db);
    }

    @Override
    void onBarCodeRead(String barCode) {

    }
    private void getAccounts(){
        orderService.getDayAccount().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                dayAccount.setText(String.valueOf(aDouble));
            }
        });
        orderService.getWeekAccount().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                weekAccount.setText(String.valueOf(aDouble));
            }
        });
        orderService.getMonthAccount().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                monthAccount.setText(String.valueOf(aDouble));
            }
        });
    }
    private void getDayOrders(){
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        orderService.getDayOrders().observe(this, new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                for(Order or: orders) {
                    System.out.println("SMB order " + format.format(or.orderDate));
                }
            }
        });
    }
}
