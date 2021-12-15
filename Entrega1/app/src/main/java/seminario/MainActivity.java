//
//
//  ENTREGA #1
//  Seminario de lenguajes - Android
//
//  Integrantes de grupo #17
//  Josemaría González
//  Valentin Dinardi
//
//

package seminario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.entrega1.R;

import org.json.JSONObject;

import java.util.ArrayList;

import seminario.dialogs.SalirDialog;
import seminario.dialogs.VolverDialog;
import seminario.dialogs.WinDialog;

public class MainActivity extends ActivityConTablero {
    ArrayList<Integer> colores_tomados = new ArrayList<Integer>(); //aca vamos guardando los colores ya tomados (para que nose repitan)
    Intent originalActivity;
    int[][] matriz_original; //la matriz con la que compararemos si está bien armada la matriz o no
    int[][] matriz_usuario; //la matriz que hace el usuario


    int CANTIDAD_COLORES = 7;

    int contador = 0;

    //con estas variables se hace el intercambio de colores
    View seleccionado1; //variable para guardar el primer elemento seleccionado
    View seleccionado2; //variable para guardar el segundo elemento selecciondo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        obtenerMatrizDeUsuario();


        if (getIntent().getExtras() != null){
            if (getIntent().getExtras().size() > 0){
                FILAS = getIntent().getExtras().getInt("matriz");
                COLUMNAS = getIntent().getExtras().getInt("matriz");
            }
        }

        matriz_original = new int[FILAS][COLUMNAS];
        matriz_usuario = new int[FILAS][COLUMNAS];

        //cargar colores para usarlos en un array
        cargarColores(FILAS);

        obtenerMatrizOriginal();

        if (savedInstanceState == null){
            crearCuadro(false);
            verOriginal();
        }else{
            restaurarCuadro(savedInstanceState);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.salirBtn){
            salir();
        }
        if (item.getItemId() == R.id.volverBtn){
            (new VolverDialog() ).show(getSupportFragmentManager(), "");
        }
        return true;
    }

    private void restaurarCuadro(Bundle savedInstanceState) {
        FILAS = savedInstanceState.getInt("FILAS");
        COLUMNAS = savedInstanceState.getInt("COLUMNAS");

        for (int y = 0; y < FILAS; y++) {
            int[] arreglo = savedInstanceState.getIntArray("fila" + y);
            for (int x = 0; x < COLUMNAS; x++) {
                matriz_usuario[x][y] = arreglo[x];
            }
        }

        crearCuadro(true);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("FILAS", FILAS);
        outState.putInt("COLUMNAS", COLUMNAS);

        obtenerMatrizDeUsuario();

        for (int y = 0; y < FILAS; y++){
            int[] arreglo = new int[FILAS];
            for (int x = 0; x < COLUMNAS; x++){
                arreglo[x] = matriz_usuario[x][y];
            }
            outState.putIntArray("fila"+y, arreglo);
        }

    }

    private int obtenerColorExistente(){
        //Si el color está ya ocupado/tomado por un View, buscamos otro.
        java.util.Random rnd = new java.util.Random();
        int color = colores_existentes.get(rnd.nextInt(colores_existentes.size()));
        while(colores_tomados.contains(color)){
            color = colores_existentes.get(rnd.nextInt(colores_existentes.size()));
        }
        //cuando conseguimos un color libre, lo pasamos al array de colores ocupados/tomados
        colores_tomados.add(color);
        return color;
    }

    public void clickeado(View v){
        //obtener el color del View seleccionado
        ColorDrawable color_de_seleccionado = (ColorDrawable) v.getBackground();
        if (seleccionado1 == null){
            //si no ha seleccionado un primer View, lo guardamos
            seleccionado1 = v;
        }else if (seleccionado2 == null){

            seleccionado2 = v;

            Drawable color1 = seleccionado1.getBackground();

            seleccionado1.setBackground(seleccionado2.getBackground());

            seleccionado2.setBackground(color1);

            seleccionado1 = null;
            seleccionado2 = null;
            color1 = null;

            verificar();
        }
    }

    private void cargarColores(int N){

        int[] rojos = getResources().getIntArray(R.array.reds);
        int[] pinks = getResources().getIntArray(R.array.pinks);
        int[] deep_purples = getResources().getIntArray(R.array.deep_purples);
        int[] indigos = getResources().getIntArray(R.array.indigos);
        int[] blues = getResources().getIntArray(R.array.blues);
        int[] light_blues = getResources().getIntArray(R.array.light_blues);
        int[] cyans = getResources().getIntArray(R.array.cyans);

        // PROCESO DE ELIMINACION DE COLORES
        int aEliminar = CANTIDAD_COLORES-N;

        addColor(rojos);
        //sacar aEliminar cantidad de rojos
        removeColor(aEliminar);
        addColor(pinks);
        //sacar aEliminar cantidad de pinks
        removeColor(aEliminar);
        addColor(deep_purples);
        //sacar aEliminar cantidad de ...
        removeColor(aEliminar);
        addColor(indigos);
        removeColor(aEliminar);
        addColor(blues);
        removeColor(aEliminar);
        addColor(light_blues);
        removeColor(aEliminar);
        addColor(cyans);
        removeColor(aEliminar);

        CANTIDAD_COLORES = N;

        removeColor(aEliminar*CANTIDAD_COLORES);
    }

    private void addColor(int[] colors){
        for (int i = 0; i < colors.length; i++){
            colores_existentes.add(colors[i]);
        }
    }

    private void removeColor(int n){
        for (int i = 0; i < n; i++){
            colores_existentes.remove(colores_existentes.size()-1);
        }
    }

    private void obtenerMatrizDeUsuario(){
        LinearLayout desordenado = (LinearLayout) findViewById(R.id.desordenado);

        for (int y = 0; y < FILAS; y++){
            View fila = desordenado.getChildAt(y);
            ArrayList<View> botones = fila.getTouchables();
            for (int x = 0; x < COLUMNAS; x++){
                View v = botones.get(x);
                if (v instanceof Button){
                    ColorDrawable color_de_seleccionado = (ColorDrawable) v.getBackground();
                    matriz_usuario[x][y] = color_de_seleccionado.getColor();
                }

            }
        }

    }

    @Override
    protected void crearCuadro(boolean esRestaurado){
        LinearLayout desordenado = (LinearLayout) findViewById(R.id.desordenado);

        for (int y = 0; y < FILAS; y++){
            LinearLayout fila = new LinearLayout(this);
            fila.setOrientation(LinearLayout.HORIZONTAL);
            fila.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1));
            desordenado.addView(fila);

            for (int x = 0; x < COLUMNAS; x++){
                Button boton = new Button(this);

                boton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        clickeado(v);
                    }
                });
                boton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1));

                if (esRestaurado){
                    boton.setBackgroundColor(matriz_usuario[x][y]);
                }else{
                    boton.setBackgroundColor(obtenerColorExistente());
                }

                fila.addView(boton);
            }
        }
    }

    private void obtenerMatrizOriginal(){
        int i = 0;
        for (int x = 0; x < COLUMNAS; x++){
            for (int y = 0; y < FILAS; y++){
                matriz_original[x][y] = colores_existentes.get(i);
                i++;
            }
        }
    }

    public void verificar(){
        boolean ganador = true;
        contador++;
        ((TextView) findViewById(R.id.contador)).setText(" "+contador + "");
        obtenerMatrizDeUsuario();
        for (int i = 0; i < FILAS; i++){
            for (int y = 0; y < COLUMNAS; y++){
                if (matriz_usuario[y][i] != matriz_original[y][i]){
                    ganador = false;
                }
            }
        }
        if (ganador){
            ganador();
        }
    }

    private void ganador(){
        SharedPreferences sharedpreferences = getSharedPreferences("RANKINGS", Context.MODE_PRIVATE);
        new WinDialog(FILAS, contador, sharedpreferences).show(getSupportFragmentManager(), "");
    }

    public void verOriginal(){
        originalActivity = new Intent(this, MatrizOriginal.class);
        originalActivity.putExtra("FILAS", FILAS);
        originalActivity.putExtra("COLUMNAS",COLUMNAS);
        originalActivity.putExtra("COLORES", colores_existentes);
        this.startActivity(originalActivity);
    }

}
