package com.example.usuario.sodamovil;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.sodamovil.BaseDeDatos.DataBase;
import com.example.usuario.sodamovil.Entidades.Restaurante;
import com.example.usuario.sodamovil.Fragmentos.GMFragmento;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        FirebaseAuth firebaseAuth;
        TextView emailUser;
        TextView nombreUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseAuth= FirebaseAuth.getInstance();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

      ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
       toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        android.app.FragmentManager fm = getFragmentManager();

        GMFragmento fragmentoMap= new GMFragmento();
        fragmentoMap.setDrawerLayout(drawer);

        fm.beginTransaction().replace(R.id.fragment,fragmentoMap).commit();
        //getSupportActionBar().setTitle("Soda Movil");
        cambiarNavHeader();
    }



    public void cambiarNavHeader(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        TextView nav_email = (TextView)hView.findViewById(R.id.EmailUser);
        TextView nav_nombre = (TextView)hView.findViewById(R.id.NombreUser);
        nav_email.setText(user.getEmail());
        nav_nombre.setText(user.getDisplayName());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Desea salir de la aplicación?");
            alertDialogBuilder
                    //.setMessage("Click 'SI' para salir!")
                    .setCancelable(false)
                    .setPositiveButton("Salir",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(Intent.ACTION_MAIN);
                                    intent.addCategory(Intent.CATEGORY_HOME);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            })

                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Mensaje("HALLO");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuItemAgregarRestaurante) {
//            Intent intento = new Intent(getApplicationContext(), AgregarRestauranteActivity.class);
            VariablesGlobales.getInstance().restauranteAgregar = null;
            VariablesGlobales.getInstance().posicionAgregarRestaurante =null;
            Intent intento = new Intent(getApplicationContext(), AgregarRestauranteNombre.class);
            startActivity(intento);
        } else if (id == R.id.menuItemAcercaDe) {
            Intent intento = new Intent(getApplicationContext(), AcercaDeActivity.class);
            startActivity(intento);
        } else if (id == R.id.menuItemCerrarSesion) {
            DialogoSiNoLogOut(findViewById(R.id.nav_view));
            //Mensaje("Cerrar Sesión");

        } else if (id == R.id.menuItemMisRestaurantes) {
            //Mensaje("Mis Restaurantes");
            Intent intento = new Intent(getApplicationContext(), MisRestaurantes.class);
            startActivity(intento);

        }else if (id == R.id.menuItemDemo) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/")));
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};

    public void DialogoSiNoLogOut(View view){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
        builder1.setMessage("Estas seguro de que quieres cerrar sesión");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Cerrar Sesión",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {logOut(); } });
        builder1.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { } });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    };

    public void logOut(){
        firebaseAuth.signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }



}
