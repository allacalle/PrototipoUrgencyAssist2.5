package net.allacalle.android.prototipourgencyassist25;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class FormulasPrincipal extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulas_principal);

        //Buscamos nuestros botones de Alto,Medio, Bajo

        Button BtnAlta = (Button) findViewById(R.id.BtnAlta);
        Button BtnMedia = (Button) findViewById(R.id.BtnMedia);
        Button BtnBaja = (Button) findViewById(R.id.BtnBaja);


        //Al pulsar uno de estos botones cargara la actividad listado_formulas con el valor de cada boton. (Alto,Medio,Bajo)
        BtnAlta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creamos el Intent
                Intent intent =
                        new Intent(FormulasPrincipal.this, ListadoFormulas.class);

                //Creamos la información a pasar entre actividades
                Bundle b = new Bundle();
                b.putString("Prioridad", "Alta");

                //Añadimos la información al intent
                intent.putExtras(b);

                //Iniciamos la nueva actividad
                startActivity(intent);

            }
        });

        BtnMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creamos el Intent
                Intent intent =
                        new Intent(FormulasPrincipal.this, ListadoFormulas.class);

                //Creamos la información a pasar entre actividades
                Bundle b = new Bundle();
                b.putString("Prioridad", "Media");

                //Añadimos la información al intent
                intent.putExtras(b);

                //Iniciamos la nueva actividad
                startActivity(intent);

            }
        });

        BtnBaja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creamos el Intent
                Intent intent =
                        new Intent(FormulasPrincipal.this, ListadoFormulas.class);

                //Creamos la información a pasar entre actividades
                Bundle b = new Bundle();
                b.putString("Prioridad", "Baja");

                //Añadimos la información al intent
                intent.putExtras(b);

                //Iniciamos la nueva actividad
                startActivity(intent);

            }
        });







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_formulas_principal, menu);
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
