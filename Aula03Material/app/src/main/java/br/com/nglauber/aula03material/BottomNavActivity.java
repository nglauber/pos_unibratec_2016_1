package br.com.nglauber.aula03material;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ca.gcastle.bottomnavigation.view.BottomNavigationView;

public class BottomNavActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);
        BottomNavigationView bottomNavigationView =
                (BottomNavigationView)findViewById(R.id.bottomBar);
        bottomNavigationView.invalidate();
    }
}
