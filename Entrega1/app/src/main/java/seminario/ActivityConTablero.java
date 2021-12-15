package seminario;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import seminario.dialogs.SalirDialog;
import seminario.dialogs.VolverDialog;

public class ActivityConTablero extends AppCompatActivity {
    int FILAS;
    int COLUMNAS;
    ArrayList<Integer> colores_existentes;

    public ActivityConTablero(){
        colores_existentes = new ArrayList<Integer>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        colores_existentes = new ArrayList<Integer>();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onBackPressed() {
        (new VolverDialog() ).show(getSupportFragmentManager(), "");
    }

    protected void salir(){
        new SalirDialog().show(getSupportFragmentManager(), "");
    }

    protected void crearCuadro(){
        //subclass
    }

    protected void crearCuadro(boolean esRestaurado){
        //subclass
    }
}
