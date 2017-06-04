package com.example.usuario.sodamovil;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.usuario.sodamovil.Entidades.Dia;
import com.example.usuario.sodamovil.Entidades.Hora;
import com.example.usuario.sodamovil.Entidades.Horario;
import com.example.usuario.sodamovil.Fragmentos.AdaptadorHorario;
import com.example.usuario.sodamovil.Utilidades.*;

import java.util.ArrayList;

public class AgregarHorarioRestauranteActivity extends AppCompatActivity {

    private TextView tvLun;
    private TextView tvMar;
    private TextView tvMier;
    private TextView tvJue;
    private TextView tvVie;
    private TextView tvSab;
    private TextView tvDom;
    private TextView tvHoraAbrir;
    private TextView tvHoraCerrar;

    private boolean [] days = {false,false,false,false,false,false,false};
    private Hora horaAbrir;
    private Hora horaCerrar;
    private Horario horario;
    private VariablesGlobales vg;
    private Button btnSiguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_horario_restaurante);
        // alambramos el TextView
        vg =  VariablesGlobales.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAgregarHorarioRestaurante);
        toolbar.setTitle("Horario");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvLun = (TextView) findViewById(R.id.tvLun);
        tvMar = (TextView) findViewById(R.id.tvMar);
        tvMier = (TextView) findViewById(R.id.tvMier);
        tvJue = (TextView) findViewById(R.id.tvJue);
        tvVie = (TextView) findViewById(R.id.tvVie);
        tvSab = (TextView) findViewById(R.id.tvSab);
        tvDom = (TextView) findViewById(R.id.tvDom);
        btnSiguiente = (Button) findViewById(R.id.btnFinalizar);
        horaAbrir = new Hora(12,0);
        horaCerrar = new Hora(20,0);
        horario= new Horario();
        btnSiguiente.setEnabled(false);
        btnSiguiente.setAlpha(.5f);
        if( vg.restauranteAgregar != null ){
            if(vg.restauranteAgregar.getHorario()!=null){
                horario = vg.restauranteAgregar.getHorario();
                btnSiguiente.setEnabled(true);
                btnSiguiente.setAlpha(1);
            }
        }
        else if( vg.getHorario() != null){
            horario = vg.getHorario();
            btnSiguiente.setEnabled(true);
            btnSiguiente.setAlpha(1);
        }
        tvHoraAbrir= (TextView) findViewById(R.id.tvHoraAbrir);

        //Programamos el evento onclick

        tvHoraAbrir.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View arg0) {
                abrirTimePickerAbrir();
            }

        });


        tvHoraCerrar = (TextView) findViewById(R.id.tvHoraCerrar);


        tvHoraCerrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                abrirTimePickerCerrar();
            }

        });

        Button MiButton = (Button) findViewById(R.id.buttonAgregarHora);


        MiButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                actualizaHorarios();
                Mensaje("Horas Asignadas.");

                limpiaDiaSeleccionados();
                RecyclerView rv = (RecyclerView) findViewById(R.id.listaDias);
                rv.getAdapter().notifyDataSetChanged();
            }

        });


        //Programamos el evento onclick

        btnSiguiente.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View arg0) {
            pasarAUbicacion();
            }

        });


        OnclickDelTextView(R.id.tvLun);
        OnclickDelTextView(R.id.tvMar);
        OnclickDelTextView(R.id.tvMier);
        OnclickDelTextView(R.id.tvJue);
        OnclickDelTextView(R.id.tvVie);
        OnclickDelTextView(R.id.tvSab);
        OnclickDelTextView(R.id.tvDom);





        RecyclerView rv = (RecyclerView) findViewById(R.id.listaDias);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        AdaptadorHorario adapter = new AdaptadorHorario(horario);
        rv.setAdapter(adapter);
    }

    public void pasarAUbicacion(){
        if( vg.restauranteAgregar != null ){
            if(horario.getDias().size()>0){
                vg.restauranteAgregar.setHorario(horario);
                Intent intento = new Intent(this, AgregarResturanteUbicacion.class);
                startActivity(intento);
            }else{
                actualizaEstadoBoton();
            }
        }
        else{
            Intent output = new Intent();
            setResult(RESULT_OK, output);
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_agregar_horario, menu);
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        return true;
    }

    private void limpiaDiaSeleccionados(){
        for(int i =0;i<days.length;i++)
            days[i]=false;

        horaAbrir.setHora(12);
        horaAbrir.setMinutos(0);
        horaCerrar.setHora(20);
        horaCerrar.setMinutos(0);
        tvHoraAbrir.setText(horaAbrir.toString());
        tvHoraCerrar.setText(horaCerrar.toString());
        tvLun.setBackgroundResource(R.drawable.bg_days_transparent);
        tvLun.setTextColor(Color.BLACK);
        tvMar.setBackgroundResource(R.drawable.bg_days_transparent);
        tvMar.setTextColor(Color.BLACK);
        tvMier.setBackgroundResource(R.drawable.bg_days_transparent);
        tvMier.setTextColor(Color.BLACK);
        tvJue.setBackgroundResource(R.drawable.bg_days_transparent);
        tvJue.setTextColor(Color.BLACK);
        tvVie.setBackgroundResource(R.drawable.bg_days_transparent);
        tvVie.setTextColor(Color.BLACK);
        tvSab.setBackgroundResource(R.drawable.bg_days_transparent);
        tvSab.setTextColor(Color.BLACK);
        tvDom.setBackgroundResource(R.drawable.bg_days_transparent);
        tvDom.setTextColor(Color.BLACK);

    }

    public void actualizaHorarios(){
        ArrayList<Dia> nuevoHorario =new ArrayList<>();
        if(days[0]){
            nuevoHorario.add(new Dia(tvLun.getText().toString(),new Hora(horaAbrir.getHora(),horaAbrir.getMinutos()),new Hora(horaCerrar.getHora(),horaCerrar.getMinutos())));
        }
        else{
            Dia diaLun = horario.getDia(tvLun.getText().toString());
            if(diaLun!=null)
                nuevoHorario.add(diaLun);
        }
        if(days[1]){
            nuevoHorario.add(new Dia(tvMar.getText().toString(),new Hora(horaAbrir.getHora(),horaAbrir.getMinutos()),new Hora(horaCerrar.getHora(),horaCerrar.getMinutos())));
        }
        else{
            Dia diaMar = horario.getDia(tvMar.getText().toString());
            if(diaMar!=null)
                nuevoHorario.add(diaMar);
        }
        if(days[2]){
            nuevoHorario.add(new Dia(tvMier.getText().toString(),new Hora(horaAbrir.getHora(),horaAbrir.getMinutos()),new Hora(horaCerrar.getHora(),horaCerrar.getMinutos())));
        }
        else{
            Dia diaMier = horario.getDia(tvMier.getText().toString());
            if(diaMier!=null)
                nuevoHorario.add(diaMier);
        }
        if(days[3]){
            nuevoHorario.add(new Dia(tvJue.getText().toString(),new Hora(horaAbrir.getHora(),horaAbrir.getMinutos()),new Hora(horaCerrar.getHora(),horaCerrar.getMinutos())));
        }
        else{
            Dia diaJue = horario.getDia(tvJue.getText().toString());
            if(diaJue!=null)
                nuevoHorario.add(diaJue);
        }
        if(days[4]){
            nuevoHorario.add(new Dia(tvVie.getText().toString(),new Hora(horaAbrir.getHora(),horaAbrir.getMinutos()),new Hora(horaCerrar.getHora(),horaCerrar.getMinutos())));
        }
        else{
            Dia diaVie = horario.getDia(tvVie.getText().toString());
            if(diaVie!=null)
                nuevoHorario.add(diaVie);
        }
        if(days[5]){
            nuevoHorario.add(new Dia(tvSab.getText().toString(),new Hora(horaAbrir.getHora(),horaAbrir.getMinutos()),new Hora(horaCerrar.getHora(),horaCerrar.getMinutos())));
        }
        else{
            Dia diaSab = horario.getDia(tvSab.getText().toString());
            if(diaSab!=null)
                nuevoHorario.add(diaSab);
        }
        if(days[6]){
            nuevoHorario.add(new Dia(tvDom.getText().toString(),new Hora(horaAbrir.getHora(),horaAbrir.getMinutos()),new Hora(horaCerrar.getHora(),horaCerrar.getMinutos())));
        }
        else{
            Dia diaDom = horario.getDia(tvDom.getText().toString());
            if(diaDom!=null)
                nuevoHorario.add(diaDom);
        }

        horario.setDias(nuevoHorario);
        actualizaEstadoBoton();
    }

    public void actualizaEstadoBoton() {
        if(horario.getDias().size()<=0){
            btnSiguiente.setEnabled(false);
            btnSiguiente.setAlpha(.5f);
        }
        else{
            btnSiguiente.setEnabled(true);
            btnSiguiente.setAlpha(1);
        }
    }

    public void OnclickDelTextView(int ref) {

        // Ejemplo  OnclickDelTextView(R.id.MiTextView);
        // 1 Doy referencia al TextView
        View view =findViewById(ref);
        final TextView miTextView = (TextView) view;
        //  final String msg = miTextView.getText().toString();
        // 2.  Programar el evento onclick
        miTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // if(msg.equals("Texto")){Mensaje("Texto en el botón ");};
                int position=0;
                switch (v.getId()) {

                    case R.id.tvLun:
                        position=0;
                        break;

                    case R.id.tvMar:
                        position =1;
                        break;

                    case R.id.tvMier:
                        position=2;
                        break;

                    case R.id.tvJue:
                        position=3;
                        break;

                    case R.id.tvVie:
                        position=4;
                        break;

                    case R.id.tvSab:
                        position=5;
                        break;

                    case R.id.tvDom:
                        position=6;
                        break;
                    default:break; }// fin de casos
                days[position]=!days[position];
                if(days[position]){
                    v.setBackgroundResource(R.drawable.bg_days);
                    miTextView.setTextColor(Color.WHITE);
                }

                else{
                    v.setBackgroundResource(R.drawable.bg_days_transparent);
                    miTextView.setTextColor(Color.BLACK);
                }
            }// fin del onclick
        });
    }// fin de OnclickDelTextView

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, AgregarRestauranteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.action_add_hours:
                pasarAUbicacion();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void abrirTimePickerAbrir(){
        final TimePicker timePicker = new TimePicker(this);
        timePicker.setIs24HourView(false);

        int hour = horaAbrir.getHora();
        int minute = horaAbrir.getMinutos();
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);

        AlertDialog tp=  new AlertDialog.Builder(this)
                .setTitle("Hora Abrir")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        horaAbrir = new Hora(timePicker.getCurrentHour(),timePicker.getCurrentMinute());
                        tvHoraAbrir.setText(horaAbrir.toString());
                    }
                })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Log.d("Picker", "Cancelled!");
                            }
                        }).setView(timePicker).show();
    }

    public void abrirTimePickerCerrar(){
        final TimePicker timePicker = new TimePicker(this);
        timePicker.setIs24HourView(false);

        int hour = horaCerrar.getHora();
        int minute = horaCerrar.getMinutos();
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);

        AlertDialog tp=  new AlertDialog.Builder(this)
                .setTitle("Hora Cerrar")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        horaCerrar = new Hora(timePicker.getCurrentHour(),timePicker.getCurrentMinute());
                        tvHoraCerrar.setText(horaCerrar.toString());
                    }
                })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Log.d("Picker", "Cancelled!");
                            }
                        }).setView(timePicker).show();
    }

    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};
}

