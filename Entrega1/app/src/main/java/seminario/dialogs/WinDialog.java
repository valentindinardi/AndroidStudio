package seminario.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.example.entrega1.R;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import seminario.Inicio;
import seminario.MainActivity;
import seminario.RankingActivity;

public class WinDialog extends DialogFragment {

    int matriz;
    int score;
    EditText usernameInput;
    boolean isHighScore;
    SharedPreferences sharedpreferences;

    public WinDialog(int matriz, int score, SharedPreferences sharedpreferences){
        this.matriz = matriz;
        this.score = score;
        this.isHighScore = false;
        this.sharedpreferences = sharedpreferences;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (score < obtenerMejorScore() || levelSize(matriz) < 5){
            isHighScore = true; //aplica para ser guardado en el sharedPreferences
            builder.setMessage(R.string.entrelosmejores);

            // input del nombre
            usernameInput = new EditText(getContext());
            usernameInput.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(usernameInput);
        }else{
            builder.setMessage(R.string.ganador);
        }

        // volver a jugar boton
        builder.setNeutralButton(R.string.volverajugar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getContext(), MainActivity.class);
                i.putExtra("matriz", matriz);
                getContext().startActivity(i);
            }
        });

        builder.setPositiveButton(R.string.nuevotablero, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getContext(), Inicio.class);
                getActivity().finish();
                getContext().startActivity(i);
            }
        }).setNegativeButton("RANKING", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isHighScore) guardarEnScores();
                Intent i = new Intent(getContext(), RankingActivity.class);
                getActivity().finish();
                getContext().startActivity(i);
            }
        });
        return builder.create();
    }

    private int obtenerMejorScore(){
        int mejor = 1000;
        Map rankings = sharedpreferences.getAll();
        Iterator it = rankings.entrySet().iterator();

        while(it.hasNext()){
            Map.Entry posicion = (Map.Entry) it.next();

            try{
                JSONObject jugador = new JSONObject(posicion.getValue().toString());
                if (jugador.getInt("score") < mejor && jugador.getString("level").equals(getNivel(matriz))) {
                    mejor = jugador.getInt("score");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return mejor;
    }

    private int levelSize(int level){
        int size = 0;
        Map rankings = sharedpreferences.getAll();
        Iterator it = rankings.entrySet().iterator();

        while(it.hasNext()){
            Map.Entry posicion = (Map.Entry) it.next();

            try{
                JSONObject jugador = new JSONObject(posicion.getValue().toString());
                if (jugador.getString("level").equals(getNivel(level))) {
                    size++;
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return size;
    }

    private String obtenerPeorJugador(){
        String result = null;
        int actualPeor = -1;
        Map rankings = sharedpreferences.getAll();
        Iterator it = rankings.entrySet().iterator();

        while(it.hasNext()){
            Map.Entry posicion = (Map.Entry) it.next();

            try{
                JSONObject jugador = new JSONObject(posicion.getValue().toString());
                if (jugador.getInt("score") > actualPeor && jugador.getString("level").equals(getNivel(matriz))) {
                    actualPeor = jugador.getInt("score");
                    result = posicion.getKey().toString();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return result;
    }

    private void guardarEnScores(){
        //antes de guardar, verificamos que siempre tengamos sólo 5 de cada dificultad
        SharedPreferences.Editor editor = sharedpreferences.edit();

        if (levelSize(matriz) >= 5) {
            editor.remove(obtenerPeorJugador());
        }

        //cada jugador es guardado como un JSON
        try{
            JSONObject jugador = new JSONObject();
            jugador.put("username", usernameInput.getText().toString());
            jugador.put("level", getNivel(matriz));
            jugador.put("score", score);
            String id_unico = "p" + usernameInput.getText().toString() + matriz + score;
            editor.putString(id_unico, jugador.toString());

            editor.commit();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    private String getNivel(int n){
        if (n == 5){
            return getResources().getString(R.string.facil);
        }else if (n == 6){
            return getResources().getString(R.string.medio);
        }else if (n == 7){
            return getResources().getString(R.string.dificil);
        }else{
            return "null";
        }
    }
}

