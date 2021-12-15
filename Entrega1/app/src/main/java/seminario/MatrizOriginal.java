package seminario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.entrega1.R;

import java.util.ArrayList;

public class MatrizOriginal extends ActivityConTablero {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matriz_original);
        if (getIntent().getExtras().size() > 0){
            FILAS = getIntent().getExtras().getInt("FILAS");
            COLUMNAS = getIntent().getExtras().getInt("COLUMNAS");
            colores_existentes = getIntent().getExtras().getIntegerArrayList("COLORES");
        }

        if (savedInstanceState == null){
            crearCuadro();
        }else{
            restaurarCuadro(savedInstanceState);
        }

    }

    private void restaurarCuadro(Bundle savedInstanceState) {
        FILAS = savedInstanceState.getInt("FILAS");
        COLUMNAS = savedInstanceState.getInt("COLUMNAS");
        colores_existentes = savedInstanceState.getIntegerArrayList("COLORES");

        crearCuadro();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("FILAS", FILAS);
        outState.putInt("COLUMNAS", COLUMNAS);
        outState.putIntegerArrayList("COLORES", colores_existentes);

    }

    @Override
    protected void crearCuadro(){
        int i = 0;
        LinearLayout original = (LinearLayout) findViewById(R.id.original);

        for (int y = 0; y < COLUMNAS; y++){
            LinearLayout columna = new LinearLayout(this);
            columna.setOrientation(LinearLayout.VERTICAL);
            columna.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1));
            original.addView(columna);

            for (int x = 0; x < FILAS; x++){
                Button boton = new Button(this);
                boton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1));
                boton.setBackgroundColor(colores_existentes.get(i));
                columna.addView(boton);
                i++;
            }
        }

        contador();
    }

    private void contador() {
        Thread contador = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 5; i > 0; i--) {
                        Thread.sleep(1000);
                        ((TextView) findViewById(R.id.segundos)).setText((i) + "");
                    }
                    volver();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        contador.start();
    }

    public void volver(){
        finish();
    }

}

