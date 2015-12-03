package net.allacalle.android.prototipourgencyassist25;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class Encuesta extends ActionBarActivity {

    private String cadena = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta);
        final TableLayout lm = (TableLayout) findViewById(R.id.TablaEncuesta);
        FormulasSQLiteHelper usdbh =
                new FormulasSQLiteHelper(this, "DbEra", null, 1);
        final SQLiteDatabase db = usdbh.getWritableDatabase();
        final List<RadioGroup> allRgs = new ArrayList<RadioGroup>();

        //Hacer una consulta para coger todas las formulas de la base de datos.
        Cursor abreviatura = db.rawQuery(" SELECT  Abreviatura FROM Formulas", null);

        //Contamos el numero de formulas
        int numeroFormulas;
        numeroFormulas = abreviatura.getCount();
        //Movemos el cursor a la primera.
        abreviatura.moveToFirst();
        TableRow filaInicial = new TableRow(this);
        //TextView vacio = new TextView(this);
        //vacio.setText(" ");
        //TextView cabecera = new TextView(this);
        //cabecera.setText("Alta Media Baja");
        //filaInicial.addView(vacio);
        //filaInicial.addView(cabecera);
        //lm.addView(filaInicial);

        int j = 0;

        for(int i=0;i< numeroFormulas; i++)
        {
            TableRow fila = new TableRow(this);
            TextView label = new TextView(this);
            //Cogemos la abreviatura de la formula
            cadena = abreviatura.getString(0);
            label.setText(cadena);
            //label.setText("prueba" +i );
            fila.addView(label);
            RadioGroup rg;
            rg = new RadioGroup(this);
            rg.setOrientation(RadioGroup.HORIZONTAL);
            RadioButton radial1 = new RadioButton(this);
            radial1.setId(j);
            radial1.setBackgroundColor(Color.parseColor("#FFCDD2"));
            RadioButton radial2 = new RadioButton(this);
            radial2.setId(j+1);
            radial2.setBackgroundColor(Color.parseColor("#FFF59D"));
            RadioButton radial3 = new RadioButton(this);
            radial3.setId(j + 2);
            radial3.setBackgroundColor(Color.parseColor("#CCFF90"));
            rg.addView(radial1);
            rg.addView(radial2);
            rg.addView(radial3);
            allRgs.add(rg);
            fila.addView(rg);
            lm.addView(fila);
            //Incrementamos el indice para crear los valores de los id
            j = j +3;
            abreviatura.moveToNext();
        }

        //Creamos un vector de String con los valores Alto,Medio,Bajo cada posicion del vector coincidira con
        // la id de los botones creados, asi sera muy facil tomar su valor.

        final String vectorPrioridad [] = new String[numeroFormulas*3] ;

        j = 0;
        final TextView resultado = new TextView(this);
        cadena = "";


        for (int i =0; i < numeroFormulas ; i++)
        {
            vectorPrioridad[j] = "Alta";
            vectorPrioridad[j+1] = "Media";
            vectorPrioridad[j+2] = "Baja";
            j = j+3;


        }

        resultado.setText(cadena);
        lm.addView(resultado);

        final Button btnConfiguracion = new Button(this);
        btnConfiguracion.setText("Aceptar Resultados");
        lm.addView(btnConfiguracion);
        //final TextView resultado = new TextView(this);
        //cadena = "";



        btnConfiguracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Voy a coger las ids de los botones.
                //Creo un bucle para recorrer la lista de radial group

                boolean noseleccionado = false;
                cadena = "";
                int idFormula;

                for (int i = 0; i < allRgs.size(); i++) {
                    if (allRgs.get(i).getCheckedRadioButtonId() == -1) {
                        cadena = " Por favor debe rellenar todas las posibles opciones antes de continuar ";
                        noseleccionado = true;
                    }
                }

                if (!noseleccionado) {

                    for (int i = 0; i < allRgs.size(); i++) {
                        cadena = cadena + "Formula" + (i+1) + ": Valor:" + vectorPrioridad[allRgs.get(i).getCheckedRadioButtonId()] + "\n";
                        //Ahora mismo se coge asi porque estan en orden, si ese orden se cambiase habra que crear un vector con los valores de la id en cada posicion
                        db.execSQL("INSERT INTO Prioridad (IdPrioridad,IdFormula,Tipo)  VALUES ('"+(i+1)+"','"+(i+1)+"','"+vectorPrioridad[allRgs.get(i).getCheckedRadioButtonId()]+"');");

                    }
                }

                //resultado.setText(cadena);
                Intent intent = new Intent(Encuesta.this, MensajePostEncuesta.class);

                //Iniciamos la nueva actividad
                startActivity(intent);

            }
        });

        //resultado.setText(cadena);
        //lm.addView(resultado);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_encuesta, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
