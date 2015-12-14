package net.allacalle.android.prototipourgencyassist25;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ListadoFormulas extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_formulas);
        //Recuperamos la información pasada en el intent
        Bundle bundle = this.getIntent().getExtras();
        //Construimos el mensaje a mostrar
        final String prioridadRecibida = bundle.getString("Prioridad");

        LinearLayout lm = (LinearLayout) findViewById(R.id.LytContenedor);
        //Creamos un objeto drawable para dibujar los botones.
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(5, Color.parseColor("#BDBDBD") );
        setTitle("ERA");

        //Creamos un objeto drawable para dar formato a los elementos auxiliares.
        GradientDrawable drawableExtra = new GradientDrawable();
        drawableExtra.setShape(GradientDrawable.RECTANGLE);
        drawableExtra.setStroke(5, Color.parseColor("#BDBDBD"));
        drawableExtra.setColor(Color.parseColor("#9E9E9E"));


        /*

        TextView TextoInformativo = new TextView(this);
        TextoInformativo.setText(prioridadRecibida );
        TextoInformativo.setTextSize(20);
        TextoInformativo.setBackgroundDrawable(drawableExtra);
        lm.addView(TextoInformativo);

        */

        //Abro la base de datos.
        FormulasSQLiteHelper usdbh =
                new FormulasSQLiteHelper(this, "DbEra", null, 1);

        SQLiteDatabase db = usdbh.getReadableDatabase();

        //creamos un cursos, en el string(0) tenemos el parametro, en el string(1) tenemos el tipo de formula
        Cursor cursorPrioridad = db.rawQuery("SELECT IdFormula FROM Prioridad WHERE Tipo = '"+prioridadRecibida+"'", null);
        cursorPrioridad.moveToFirst();
        int numeroFormulas =cursorPrioridad.getCount();
        //Ya tenemos el identificador de las formulas de la prioridad recibida.

        //Creamos una formula por cada identificador de formula.

        String abreviaturas [] = new String[numeroFormulas];
        String cadena ="";

        for (int i =0; i < numeroFormulas; i++)
        {
            Cursor cursorFormula = db.rawQuery("SELECT Abreviatura FROM Formulas WHERE IdFormula  = '"+cursorPrioridad.getString(0)+"'", null);
            cursorFormula.moveToFirst();
            //Abreviatura de la formula
            abreviaturas[i] =cursorFormula.getString(0) ;
            cadena = cadena + abreviaturas[i] + "\n";
            cursorPrioridad.moveToNext();
            cursorFormula.close();
        }



        switch (prioridadRecibida){
            case "Alta":
                drawable.setColor(Color.parseColor("#FF8A80"));
                break;
            case "Media":
                drawable.setColor(Color.parseColor("#FFF59D"));
                break;
            case "Baja":
                drawable.setColor(Color.parseColor("#CCFF90"));
                break;
        }


        cursorPrioridad.close();
        for (int i =0; i < numeroFormulas; i++) {
            final Button boton = new Button(this);

            boton.setText(abreviaturas[i]);
            boton.setBackgroundDrawable(drawable);
            lm.addView(boton);
            boton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Creamos el Intent
                    Intent intent =
                            new Intent(ListadoFormulas.this, DetallesFormula.class);

                    //Creamos la información a pasar entre actividades
                    Bundle b = new Bundle();
                    b.putString("NOMBRE", (String) boton.getText());

                    //Añadimos la información al intent
                    intent.putExtras(b);

                    //Iniciamos la nueva actividad
                    startActivity(intent);

                }
            });


        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listado_formulas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if ( id == R.id.action_Formulas )
        {
            Intent intent =
                    new Intent(ListadoFormulas.this, FormulasPrincipal.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
