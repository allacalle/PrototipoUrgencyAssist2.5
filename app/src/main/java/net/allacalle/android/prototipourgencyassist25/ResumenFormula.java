package net.allacalle.android.prototipourgencyassist25;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ResumenFormula extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_formula);
        //Recuperamos la información pasada en el intent
        Bundle bundle = this.getIntent().getExtras();
        //Construimos el mensaje a mostrar
        final String valorRecibido = bundle.getString("RESUMEN");
        setTitle("ERA");

        //TextView txtResumen = (TextView)findViewById(R.id.TxtResumen);
        //txtResumen.setText(valorRecibido);

        final LinearLayout lm = (LinearLayout) findViewById(R.id.LytContenedor);

        //Creamos un objeto drawable para dar formato a los elementos auxiliares.
        GradientDrawable drawableExtra = new GradientDrawable();
        drawableExtra.setShape(GradientDrawable.RECTANGLE);
        drawableExtra.setStroke(5, Color.parseColor("#424242"));
        drawableExtra.setColor(Color.parseColor("#C5CAE9"));



        TextView txtResumen = new TextView(this);
        txtResumen.setBackgroundDrawable(drawableExtra);
        txtResumen.setTextSize(30);

        String valoresRecibido [] = valorRecibido.split(";");

        //El primer valor sera siempre el del id de la formula. Los demas valores seran los de los parametros.
        String idFormula = valoresRecibido[0];

        Formula formulaResumen = new Formula(idFormula, getBaseContext());

        Button botonAtras = (Button) findViewById(R.id.BtnAtras);
        botonAtras.setText(formulaResumen.getAbreviatura());

        //Como tengo el id de la Formula, vamos a meter la formula que se ha utilizado en la tabla recientes.
        //Antes de pasar a la siguiente pantalla guardaremos la formula en recientes.
        FormulasSQLiteHelper usdbh =
                new FormulasSQLiteHelper(getBaseContext(), "DbEra", null, 1);

        //Abrimos la base de datos en modo escritura
        SQLiteDatabase db = usdbh.getWritableDatabase();


        //vemos si hay una formula con esa id
        Cursor c = db.rawQuery("SELECT COUNT (IdFormula) FROM Recientes  WHERE IdFormula = '" + idFormula + "'  ", null);
        c.moveToFirst();
        //Cogemos el identificador de la formula
        int numeroFormulas = c.getInt(0);
        c.close();

        //Cogemos la fecha del sistema y le ponemos en formato dd/mm/aaaa hora:minuto:segundo
        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        //String fecha = curFormater.format(date);


        //Si existe la borramos e insertamos la id con la fecha actual en la tabla Recientes
        if (numeroFormulas != 0 ) {
            db.execSQL("DELETE FROM Recientes WHERE IdFormula = '" + idFormula + "' ;");
            db.execSQL("INSERT INTO Recientes (IdFormula,Fecha)  VALUES ('"+ idFormula +"','"+ curFormater.format(date) +"' );");

        }
        //Sino insertamos la id con la fecha actual en la tabla Recientes
        else
        {
            db.execSQL("INSERT INTO Recientes (IdFormula,Fecha)  VALUES ('"+ idFormula +"','"+ curFormater.format(date) +"' );");
        }

        c.close();
        db.close();




        int numeroParametros =formulaResumen.contarParametros();

        for (int i = 0; i < numeroParametros; i++)
        {
            formulaResumen.getParametro(i).setValor(valoresRecibido[i+1]);
        }

        //Lo que vamos a mostrar
        String cadena = "";
        float sumadorEscala = 0;


        for (int i = 0; i < numeroParametros; i++)
        {
            //cadena = cadena + formulaResumen.getParametro(i).getNombre() + ":" + formulaResumen.getParametro(i).getValor() + ". \n" ;
            TextView txtParametros = new TextView(this);
            txtParametros.setBackgroundDrawable(drawableExtra);
            txtParametros.setText(" " + formulaResumen.getParametro(i).getNombre() + ":" + formulaResumen.getParametro(i).getValor() + ".");
            lm.addView(txtParametros);
        }

        //txtResumen.setText(cadena);


        //Hay que calcular el resultado de una ecuacion es una formula generica
        if (formulaResumen.getExpresion().equals("escala")) {

            //Activamos el campo resultado.
            formulaResumen.setResultado(getBaseContext());
            String valorIntroducido;
            for (int i = 0; i < numeroParametros; i++) {
                //cadena = cadena + "El numero de parametros " +i +  "es igual a " + formulaResumen.getParametro(i).contarCriterios() + ". \n" ;
                //Vamos examinando cada parametros y si cumple alguna condicion de puntuacion aumentamos el contador.
                valorIntroducido = formulaResumen.getParametro(i).getValor();

                if (formulaResumen.getParametro(i).getTipo().equals("escalaA")) {
                    if (formulaResumen.getParametro(i).contarCriterios() == 1) {

                        if (formulaResumen.getParametro(i).getValor().equals("Marcado")) {
                            sumadorEscala = sumadorEscala + Float.parseFloat(formulaResumen.getParametro(i).getCriterioPuntuacion(0).getValor());
                        }
                    } else {
                        for (int j = 0; j < formulaResumen.getParametro(i).contarCriterios(); j++) {
                            if (formulaResumen.getParametro(i).getCriterioPuntuacion(j).getCriterio().equals(valorIntroducido)) {
                                sumadorEscala = sumadorEscala + Float.parseFloat(formulaResumen.getParametro(i).getCriterioPuntuacion(j).getValor());
                            }
                        }
                    }
                } else {
                    for (int j = 0; j < formulaResumen.getParametro(i).contarCriterios(); j++) {
                        if (formulaResumen.getParametro(i).getCriterioPuntuacion(j).getValor().equals("x"))
                            sumadorEscala = sumadorEscala + Float.parseFloat(formulaResumen.getParametro(i).getValor());
                        //cadena = cadena + "Solo debo entra si tengo x y quiero sumar " +   formulaResumen.getParametro(i).getValor() + ".\n" ;
                        Expression evaluador = new Expression(formulaResumen.getParametro(i).getCriterioPuntuacion(j).getCriterio());
                        //Asignar el valor introducido como variable x de esa expresion.
                        evaluador.with("x", valorIntroducido);
                        //Si esta expresion se confirma con algun criterio sumar el valor de ese criterio.
                        if (evaluador.eval().toString().equals("1"))
                            sumadorEscala = sumadorEscala + Float.parseFloat(formulaResumen.getParametro(i).getCriterioPuntuacion(j).getValor());
                        //cadena = cadena + "Quiero sumar " + Float.parseFloat(formulaResumen.getParametro(i).getCriterioPuntuacion(j).getValor()) + ".\n";
                    }
                }
            }
            //Ahora que tenemos el resultado Vamos a interpretarlo.
            formulaResumen.setResultado(getBaseContext());
            if (formulaResumen.getResultado().getIdParametro() != null) {
                for (int i = 0; i < formulaResumen.getResultado().contarCriterios(); i++) {

                    Expression evaluador = new Expression(formulaResumen.getResultado().getCriterioPuntuacion(i).getCriterio());
                    String aux="";
                    aux= aux +sumadorEscala;
                    //Convertimos el valor sumado a cadena
                    evaluador.with("x",aux);
                    //Cogemos el valor por cumplir ese criterio de puntuacion
                    if (evaluador.eval().toString().equals("1"))
                        formulaResumen.getResultado().setValor(formulaResumen.getResultado().getCriterioPuntuacion(i).getValor());
                }

            }


        }

        else {

            Expression expression = new Expression(formulaResumen.getExpresion());


            for (int i = 0; i < numeroParametros; i++) {
                if(i == 0 )
                    expression.with(formulaResumen.getParametro(i).getNombre(), formulaResumen.getParametro(i).getValor());
                else
                    expression.and(formulaResumen.getParametro(i).getNombre(), formulaResumen.getParametro(i).getValor());
            }

            String resultado =  expression.eval().toString() ;

            cadena = cadena + "Su resultado es:" + Float.parseFloat(resultado)  ;

            //Si queremos coger la medida debemos hacer una consulta a la bd donde el campo resultado tenga el id de esa formula

            usdbh = new FormulasSQLiteHelper(this, "DbEra", null, 1);

            db = usdbh.getReadableDatabase();

            c = db.rawQuery("SELECT Medida FROM Parametros  WHERE IdFormula = '" + idFormula + "' AND TipoParametro = 'resultado'  ", null);
            c.moveToFirst();

            String medida;
            medida = " " + c.getString(0);
            cadena = cadena + medida ;






        }

        //txtResumen.setText(cadena + "Y  sumaaaaa" + sumadorEscala + "\n");

        if (formulaResumen.getExpresion().equals("escala"))
            txtResumen.setText(cadena + "Suma " + sumadorEscala + "\n"  + formulaResumen.getResultado().getValor()   );
        else
            txtResumen.setText(cadena);


        //Hay que sumar la puntuacion si es una formula de Escala.

        GradientDrawable drawableEditar = new GradientDrawable();
        drawableEditar.setShape(GradientDrawable.RECTANGLE);
        drawableEditar.setStroke(5, Color.parseColor("#BDBDBD"));
        drawableEditar.setColor(Color.parseColor("#9E9E9E"));


        Button button = new Button(this);
        button.setBackgroundDrawable(drawableEditar);
        button.setText("Editar");


        LinearLayout linearAuxiliar = new LinearLayout (this);
        linearAuxiliar.setOrientation(LinearLayout.VERTICAL);

        //Se crea un parametro auxiliar para cuestiones de diseño con el TextView y el EditText
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);


        button.setLayoutParams(param);
        txtResumen.setLayoutParams(param);
        linearAuxiliar.addView(txtResumen);
        linearAuxiliar.addView(button);
        lm.addView(linearAuxiliar);


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                finish();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resumen_formula, menu);
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
                    new Intent(ResumenFormula.this, FormulasPrincipal.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
