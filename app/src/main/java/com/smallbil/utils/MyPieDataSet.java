package com.smallbil.utils;

import android.graphics.Color;

import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.smallbil.constants.TresholdEnum;

import java.util.List;

public class MyPieDataSet extends PieDataSet {
    public MyPieDataSet(List<PieEntry> yVals, String label) {
        super(yVals, label);
        this.setColors(Color.RED, Color.parseColor("#FFFF9800"));
    }
    @Override
    public int getColor(int index) {
        if(getEntryForIndex(index).getData().toString().equals(TresholdEnum.RED.toString()))
            return mColors.get(0);
        else
            return mColors.get(1);

    }


}
