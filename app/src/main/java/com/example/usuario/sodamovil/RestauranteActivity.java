package com.example.usuario.sodamovil;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.usuario.sodamovil.Fragmentos.FragmentoContacto;
import com.example.usuario.sodamovil.Fragmentos.GMFragmento;
import com.example.usuario.sodamovil.Fragmentos.Informacion;
import com.example.usuario.sodamovil.Fragmentos.MenuFragmento;

public class RestauranteActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    changeFragment(1);
                    return true;
                case R.id.navigation_menu:
                    changeFragment(2);
                    return true;
                case R.id.navigation_contacto:
                    changeFragment(3);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante);

        getSupportActionBar().setTitle(VariablesGlobales.getInstance().getRestauranteActual().getNombre());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        changeFragment(1);
    }

    public void changeFragment(int frg){
        switch (frg){
            case 1:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_Rest, new Informacion());
                ft.commit();
                break;
            case 2:
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_Rest, new MenuFragmento());
                ft.commit();
                break;
            case 3:
               ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_Rest, new FragmentoContacto());
                ft.commit();
                break;
            default: break;
        }

    }

}
