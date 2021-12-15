//
//
//    CUADRO CROMATICO
//    RE-ENTREGA #3
//    INTEGRANTES: JOSEMARÍA GONZALEZ, VALENTIN DI NARDI
//

package seminario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.entrega1.R;

import seminario.dialogs.SalirDialog;

public class Inicio extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Intent mainActivityIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        mainActivityIntent = new Intent(this, MainActivity.class);

        int orientation = getResources().getConfiguration().orientation;
        ImageView logo = findViewById(R.id.logo);

        String[] arraySpinner = new String[] {
                getResources().getString(R.string.facil), getResources().getString(R.string.medio), getResources().getString(R.string.dificil)
        };
        Spinner s = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(this);

        findViewById(R.id.vamosbtn).setEnabled(true);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {

            case 0:
                mainActivityIntent.putExtra("matriz", 5);
                break;
            case 1:
                mainActivityIntent.putExtra("matriz", 6);
                break;
            case 2:
                mainActivityIntent.putExtra("matriz", 7);
                break;
            default:
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    public void exit(View v){
        new SalirDialog().show(getSupportFragmentManager(), "");
    }
    public void rank(View v){
        startActivity(new Intent(Inicio.this, RankingActivity.class));
    }

    public void vamos(View v){
        this.startActivity(mainActivityIntent);
    }



}
