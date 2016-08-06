package br.com.nglauber.aula03material;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

public class BottomSheetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet);

        TabLayout tabLayout= (TabLayout)findViewById(R.id.tab_layout);
        TabLayout.Tab tab1 =  tabLayout.newTab();
        tab1.setText("Aba 1");
        TabLayout.Tab tab2 =  tabLayout.newTab();
        tab2.setText("Aba 2");
        TabLayout.Tab tab3 =  tabLayout.newTab();
        tab3.setText("Aba 3");
        tabLayout.addTab(tab1);
        tabLayout.addTab(tab2);
        tabLayout.addTab(tab3);
    }
}
