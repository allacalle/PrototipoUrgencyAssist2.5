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


public class Recientes extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recientes);
        LinearLayout lm = (LinearLayout) findViewById(R.id.LytContenedor);
        TextView texto = new TextView(this);

        setTitle("ERA");


        //Abro la base de datos.
        FormulasSQLiteHelper usdbh =
                new FormulasSQLiteHelper(this, "DbEra", null, 1);

        SQLiteDatabase db = usdbh.getReadableDatabase();

        //creamos un cursos, en el string(0) tenemos el parametro, en el string(1) tenemos el tipo de formula
        Cursor Contador = db.rawQuery("SELECT COUNT(*) FROM Recientes", null);
        Contador.moveToFirst();
        int numeroCampos = Contador.getInt(0);
        Contador.close();
        Cursor c = db.rawQuery("SELECT IdFormula,Fecha FROM Recientes ORDER BY Fecha DESC ", null);
        c.moveToFirst();

        String cadena = "";

        //Solo quiero mostrar las 5 primeras como mucho.
        if (numeroCampos > 7)
            numeroCampos = 7;

        for (int i =0; i < numeroCampos; i++)
        {

            //Creamos un objeto drawable para dar formato a los elementos auxiliares.
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setStroke(5, Color.parseColor("#BDBDBD"));


            //Cojo cada una de las ids de las formulas
            int idFormula = c.getInt(0);

            //Busco en la tabla Formulas el nombre abreviado de las IDS y se lo asigno al boton.

            //Creamos un cursor para las formulas
            Cursor cursorFormula = db.rawQuery("SELECT Abreviatura FROM Formulas WHERE IdFormula = '"+ idFormula +"'  ", null);
            cursorFormula.moveToFirst();
            String abreviatura = cursorFormula.getString(0);
            cursorFormula.close();


            //Busco en la tabla Prioridad la prioridad asignada a esas IDS y dependiendo de la que sea asigno un color a ese boton.
            Cursor cursorPrioridad = db.rawQuery("SELECT Tipo FROM Prioridad WHERE IdFormula = '"+ idFormula +"'  ", null);
            cursorPrioridad.moveToFirst();
            String prioridad = cursorPrioridad.getString(0);
            cursorPrioridad.close();

            //Aplico al drawable el color de la prioridad actual

            switch (prioridad){
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


            //Creo un boton
            final Button boton = new Button(this);

            //Le asigno el texto
            boton.setText(abreviatura);

            //Le aplico el layout
            boton.setBackgroundDrawable(drawable);

            //Meto el boton en el layout
            lm.addView(boton);


            //Definimos la funcion del boton
            boton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Creamos el Intent
                    Intent intent =
                            new Intent(Recientes.this, DetallesFormula.class);

                    //Creamos la información a pasar entre actividades
                    Bundle b = new Bundle();
                    b.putString("NOMBRE", (String) boton.getText());

                    //Añadimos la información al intent
                    intent.putExtras(b);

                    //Iniciamos la nueva actividad
                    startActivity(intent);

                }
            });


            //Paso al valor siguiente del cursor
            c.moveToNext();
        }

        c.close();
        db.close();




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recientes, menu);
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
                    new Intent(Recientes.this, FormulasPrincipal.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
