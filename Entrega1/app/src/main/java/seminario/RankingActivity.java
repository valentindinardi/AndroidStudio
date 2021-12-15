package seminario;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.gridlayout.widget.GridLayout;

import com.example.entrega1.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import seminario.dialogs.SalirDialog;

public class RankingActivity extends AppCompatActivity {
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        llenarTabla();
    }

    private void llenarTabla() {
        sharedpreferences = getSharedPreferences("RANKINGS", Context.MODE_PRIVATE);
        Map rankings = sharedpreferences.getAll();
        Iterator it = rankings.entrySet().iterator();
        androidx.gridlayout.widget.GridLayout tabla = null;

        while(it.hasNext()){
            Map.Entry posicion = (Map.Entry) it.next();

            try{
                JSONObject jugador = new JSONObject(posicion.getValue().toString());
                TextView username = crearTextView(jugador.getString("username"));

                TextView level = crearTextView(jugador.getString("level"));

                TextView score = crearTextView(jugador.getString("score"));

                if (jugador.getString("level").equals(getResources().getString(R.string.facil))){
                    tabla = findViewById(R.id.easyTbl);
                }else if (jugador.getString("level").equals(getResources().getString(R.string.medio))){
                    tabla = findViewById(R.id.mediumTbl);
                }else if (jugador.getString("level").equals(getResources().getString(R.string.dificil))){
                    tabla = findViewById(R.id.hardTbl);
                }

                tabla.addView(username);
                tabla.addView(level);
                tabla.addView(score);
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

    private TextView crearTextView(String texto){

        //textview y su weight (para que quede bien dividido)
        TextView textview = new TextView(this);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        textview.setLayoutParams(params);

        textview.setText(texto);
        return textview;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rankings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.salirBtn:
                salir();
                break;
            case R.id.compatirBtn:
                compartir();
                break;
            case R.id.nuevoTableroBtn:
                Intent inicioIntent = new Intent(this, Inicio.class);
                finish();
                this.startActivity(inicioIntent);
                break;
            default:
                break;
        }
        return true;
    }

    private void compartir(){
        if (obtenerMejorJugador() != null){
            try{
                String message = getResources().getString(R.string.mimejorscorees) + " " + obtenerMejorJugador().getString("score") + " " + getResources().getString(R.string.endificultad) + " " + obtenerMejorJugador().getString("level");
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);

                startActivity(Intent.createChooser(share, getResources().getString(R.string.dondecompartir)));
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getBaseContext(), getResources().getString(R.string.notenesrankings), Toast.LENGTH_SHORT).show();
        }


    }

    private JSONObject obtenerMejorJugador(){
        int mejor = 1000;
        JSONObject result = null;

        Map rankings = sharedpreferences.getAll();
        Iterator it = rankings.entrySet().iterator();

        while(it.hasNext()){
            Map.Entry posicion = (Map.Entry) it.next();

            try{
                JSONObject jugador = new JSONObject(posicion.getValue().toString());
                if (jugador.getInt("score") < mejor) {
                    result = jugador;
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return result;
    }

    private void salir(){
        new SalirDialog().show(getSupportFragmentManager(), "");
    }

}
