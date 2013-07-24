package com.lilmak.charty;

import com.lilmak.charty.PieChart.LableType;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PieChart chart =  (PieChart) findViewById(R.id.pieChart1);
        int[] perc = {50,50};
        int[] colors = {0xFF33B5E5,0xFFAA66CC};
        String[] names = {"Android","iOS"};
        chart.init(perc,colors).withSectionNames(names);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
