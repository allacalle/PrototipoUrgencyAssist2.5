package net.allacalle.android.prototipourgencyassist25;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DetallesFormula extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_formula);
        setTitle("ERA");
        //Recuperamos la información pasada en el intent
        Bundle bundle = this.getIntent().getExtras();
        //Construimos el mensaje a mostrar
        final String valorRecibido = bundle.getString("NOMBRE");
        //creamos el layout dinamico como pros!
        final LinearLayout lm = (LinearLayout) findViewById(R.id.LytContenedor);

        //Creamos un objeto drawable para dar formato a los elementos auxiliares.
        GradientDrawable drawableExtra = new GradientDrawable();
        drawableExtra.setShape(GradientDrawable.RECTANGLE);
        drawableExtra.setStroke(5, Color.parseColor("#BDBDBD"));
        drawableExtra.setColor(Color.parseColor("#9E9E9E"));



        Button botonAtras = (Button) findViewById(R.id.BtnAtras);
        botonAtras.setText(valorRecibido);

        /*
        //Creamos un linear layout para poner el nombre de la prioridad
        LinearLayout lFormula = new LinearLayout(this);
        TextView tituloFormula = new TextView(this);
        tituloFormula.setBackgroundDrawable(drawableExtra);
        tituloFormula.setText(valorRecibido);
        lFormula.addView(tituloFormula);
        lm.addView(lFormula);
        */


        // Para obtener el tipo de cada formula deberiamos movernos al cursor de cada formula y obtener el getString(1);
        //Abro la base de datos.
        FormulasSQLiteHelper usdbh =
                new FormulasSQLiteHelper(this, "DbEra", null, 1);

        SQLiteDatabase db = usdbh.getReadableDatabase();

        //creamos un cursos, en el string(0) tenemos el parametro, en el string(1) tenemos el tipo de formula
        Cursor c = db.rawQuery("SELECT IdFormula,NombreCompleto,Expresion FROM Formulas  WHERE Abreviatura = '" + valorRecibido + "'  ", null);
        c.moveToFirst();
        //Cogemos el identificador de la formula
        String idFormula = c.getString(0);
        //Cogemos el nombre completo de la formula
        String nombreFormula = c.getString(1);

        //Obtenemos la expresion de esa formula si la tiene sino simplemente obtendra null
        final String expresion = c.getString(2);
        c.close();
        db.close();
        //Creamos la formula con la que trabajaremos
        final Formula formulaActual = new Formula(idFormula,getBaseContext());

        //Evaluamos si el tipo de la formula es escala
        if ( formulaActual.getExpresion().equals("escala") )
        {
            //Lista para los editText
            final List<EditText> allEds = new ArrayList<EditText>();
            //Lista para los checkbox
            final List<CheckBox> allChs = new ArrayList<CheckBox>();
            //Lista para los radial group
            final List<RadioGroup> allRgs = new ArrayList<RadioGroup>();

            //Drawable para los editText del formulario


            //Creamos un contador para almacenar el resultado de la escala
            int contadorEscala = 0;

            //Dentro de cada formula hay que examinar cada parametro para ver de que tipo es.

            for (int i = 0; i < formulaActual.contarParametros(); i++   )
            {
                //Creamos un parametro copiando el existente para trabajar con el por comodidad.
                Parametro parametroActual = formulaActual.getParametro(i);
                String tipoParametro = parametroActual.getTipo();

                if(tipoParametro.equals("escalaA") )
                {
                    //Contamos el numero de valores que puede tomar.
                    int contadorCriterio = parametroActual.contarCriterios();

                    if (contadorCriterio == 1)
                    {
                        //Se crea un chebox con el criterio.
                        CheckBox ch = new CheckBox(this);
                        ch.setText(parametroActual.getNombre());
                        allChs.add(ch);
                        lm.addView(ch);
                    }
                    else
                    {
                        //Se creo un radial group con el criterio. Luego se crearan radial button con los posibles valores.t
                        RadioGroup rg;
                        rg = new RadioGroup(this);

                        for(int j=0;j< parametroActual.contarCriterios(); j++)
                        {
                            RadioButton radial = new RadioButton(this);
                            radial.setText(parametroActual.getCriterioPuntuacion(j).getCriterio());
                            radial.setId(parametroActual.getCriterioPuntuacion(j).getIdCriterioPuntuacion());
                            //
                            //meter valor en una matriz con el identificador.
                            rg.addView(radial);
                        }
                        allRgs.add(rg);
                        TextView label = new TextView(this);
                        label.setText(parametroActual.getNombre());
                        lm.addView(label);
                        lm.addView(rg);
                    }
                }
                else if (tipoParametro.equals("escalaB") )
                {

                    GradientDrawable drawableEdit = new GradientDrawable();
                    drawableEdit.setShape(GradientDrawable.RECTANGLE);
                    drawableEdit.setStroke(5, Color.parseColor("#E0E0E0"));
                    drawableEdit.setColor(Color.parseColor("#9E9E9E"));


                    //Se crea un linear layout auxiliar para meter el nombre del campo y su espacio para insertarlo
                    LinearLayout linearAuxiliar = new LinearLayout (this);
                    linearAuxiliar.setOrientation(LinearLayout.HORIZONTAL);

                    //Se crea un parametro auxiliar para cuestiones de diseño con el TextView y el EditText
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);

                    //Se crea una caja de texto para introducir un valor numerico
                    //Creamos un campo para el nombre del parametro
                    TextView label = new TextView(this);
                    //
                    label.setLayoutParams(param);

                    if (parametroActual.getMedida() != null)
                        label.setText("" +parametroActual.getNombre()+ "(" +parametroActual.getMedida()+ ")." );
                    else
                        label.setText(parametroActual.getNombre());

                    //creamos un campo de texto para introducir el valor del parametro
                    EditText ed;
                    ed = new EditText(this);

                    ed.setLayoutParams(param);
                    ed.setBackgroundDrawable(drawableEdit);
                    allEds.add(ed);
                    //ed.setId( parametroActual.getIdParametro() );
                    //Mostrar solo teclado numerico
                    ed.setInputType(InputType.TYPE_CLASS_PHONE);
                    linearAuxiliar.addView(label);
                    linearAuxiliar.addView(ed);
                    lm.addView(linearAuxiliar);
                }
            }


            Button botonGenerico = new Button(this);
            botonGenerico.setBackgroundDrawable(drawableExtra);
            botonGenerico.setText("Calcular");
            lm.addView(botonGenerico);
            final TextView mensaje = new TextView(this);
            lm.addView(mensaje);


            botonGenerico.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Primero vamos a comprobar que no se han dejado valores en blanco en el formulario
                    boolean valoresEnBlanco = false;
                    String cadenaEvaluar="";
                    //Como los valores pueden estar en EditText, ChecBox o Radial Button habra que comprobar cada tipo de lista.
                    //Evaluamos la lista de EditText hasta el tamaño de EditText introducidos.
                    for (int i = 0; i < allEds.size(); i++) {
                        cadenaEvaluar = allEds.get(i).getText().toString();
                        //Ponemos el valor del campo en cada parametro
                        if (cadenaEvaluar.equals(""))
                            valoresEnBlanco = true;
                    }

                    //Evaluamos la lista de radial group para que al menos uno este elegido
                    for (int i = 0; i <  allRgs.size(); i++) {
                        int opcion;
                        opcion = allRgs.get(i).getCheckedRadioButtonId();
                        //Ningun boton marcado.
                        if (opcion == -1)
                            valoresEnBlanco = true;
                    }

                    //Si el formulario tiene valores sin rellenar.
                    if (valoresEnBlanco) {
                        mensaje.setText("No es posible calcular la formula con valores en blanco");
                    } else
                    {
                        //mensaje.setText("Calculando...");
                        //Aqui es donde habra que hacer lo gordo, asignar valores a parametros etc etc.
                        //Se debe asignar el valor actual del campo al valor actual de la formula.

                        int numeroParametros = formulaActual.contarParametros();
                        //Se recorren todos los parametros

                        //Se necesitaran 3 cursores.
                        //Para recorrer la lista de chexbox
                        int cursorChs =0;
                        //Para recorrer la lista de Radio Group
                        int cursorRgs =0;
                        //Para reocrrer la lista de EditText
                        int cursorEds =0;

                        for (int i = 0; i < numeroParametros; i++)
                        {
                            //Se crea un objeto Parametro para trabajar con mas comodidad.
                            //Parametro parametroActual = formulaActual.getParametro(i);


                            //Si el parametro es escalaA

                            if (formulaActual.getParametro(i).getTipo().equals("escalaA"))
                            {
                                //Si el parametro es escalaA  con un solo criterio se extrae de allChs
                                if (formulaActual.getParametro(i).contarCriterios() == 1)
                                {
                                    if(allChs.get(cursorChs).isChecked() )
                                        formulaActual.getParametro(i).setValor("Marcado");
                                    else
                                        formulaActual.getParametro(i).setValor("No marcado");

                                    //Incrementamos el cursor cursorChs
                                    cursorChs ++;
                                }
                                //Si el parametro es escalaA con varios criterios se extrae de allRgs
                                else
                                {
                                    //Obtenemos la id del boton de radio que coincide con la id del CriterioPuntuacion
                                    int idRadio = allRgs.get(cursorRgs).getCheckedRadioButtonId();
                                    //Obtenemos la posicion donde se almacena ese criterio
                                    int posicionCriterio = formulaActual.getParametro(i).buscarPosicionDeCriterio(idRadio);
                                    //Asignamos al valor el nombre del criterio que es una String con la opcion seleccionada.
                                    formulaActual.getParametro(i).setValor(formulaActual.getParametro(i).getCriterioPuntuacion(posicionCriterio).getCriterio());

                                    cursorRgs++;
                                }
                            }

                            //Si el parametro es escalaB se estrae de allEds
                            else
                            {
                                //Obtenemos el valor
                                formulaActual.getParametro(i).setValor(allEds.get(cursorEds).getText().toString() );

                                //Incrementamos el cursor.
                                cursorEds++;
                            }


                        }

                        //Creamos una cadena donde capturamos los valores de los parametros para mandarlos a la pantalla de resumen.
                        String valores = "";
                        valores = valores + formulaActual.getIdFormula() + ";" ;
                        for (int i = 0; i < numeroParametros; i++)
                        {
                            valores = valores + formulaActual.getParametro(i).getValor() + ";" ;
                        }

                        //mensaje.setText(cadena);
                        Intent intent =
                                new Intent(DetallesFormula.this, ResumenFormula.class);

                        Bundle b = new Bundle();
                        b.putString("RESUMEN", (String) valores);


                        //Añadimos la información al intent
                        intent.putExtras(b);

                        //Iniciamos la nueva actividad
                        startActivity(intent);
                    }
                }
            });



        }

        //Sino será de tipo generico
        else
        {
            final List<EditText> allEds = new ArrayList<EditText>();
            final int numeroParametros = formulaActual.contarParametros();
            //final int valor1 = allEds.size();
            //Creamos un objeto drawable para dibujar el boton Edit
            GradientDrawable drawableEdit = new GradientDrawable();
            drawableEdit.setShape(GradientDrawable.RECTANGLE);
            drawableEdit.setStroke(5, Color.parseColor("#E0E0E0"));
            drawableEdit.setColor(Color.parseColor("#9E9E9E"));

            for(int i=0;i< numeroParametros ; i++)
            {

                LinearLayout linearAuxiliar = new LinearLayout (this);
                linearAuxiliar.setOrientation(LinearLayout.HORIZONTAL);

                //Se crea un parametro auxiliar para cuestiones de diseño con el TextView y el EditText
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);


                //Creamos un campo para el nombre del parametro
                TextView label = new TextView(this);
                label.setLayoutParams(param);
                //Si tiene una unidad de medida habra que mostrarla
                if (formulaActual.getParametro(i).getMedida() != null)
                    label.setText("" +formulaActual.getParametro(i).getNombre()+ "(" +formulaActual.getParametro(i).getMedida()+ ")." );
                else
                    label.setText(formulaActual.getParametro(i).getNombre());

                //creamos un campo de texto para introducir el valor del parametro
                EditText ed;
                ed = new EditText(this);
                ed.setLayoutParams(param);
                ed.setBackgroundDrawable(drawableEdit);
                allEds.add(ed);
                //ed.setId( formulaActual.getParametro(i).getIdParametro() );
                //Mostrar solo teclado numerico
                ed.setInputType(InputType.TYPE_CLASS_PHONE);
                linearAuxiliar.addView(label);
                linearAuxiliar.addView(ed);
                lm.addView(linearAuxiliar);
            }
            //final int valor2 = allEds.size() ;

            Button botonEcuacion = new Button(this);
            botonEcuacion.setBackgroundDrawable(drawableExtra);
            botonEcuacion.setText("Calcular formula");
            lm.addView(botonEcuacion);
            final TextView mensaje = new TextView(this);
            lm.addView(mensaje);


            botonEcuacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Primero vamos a comprobar que no se han dejado valores en blanco en el formulario
                    boolean valoresEnBlanco = false;
                    String cadenaEvaluar;
                    for (int i = 0; i < numeroParametros; i++) {
                        cadenaEvaluar = allEds.get(i).getText().toString();
                        //Ponemos el valor del campo en cada parametro
                        formulaActual.getParametro(i).setValor(cadenaEvaluar);
                        if (cadenaEvaluar.equals(""))
                            valoresEnBlanco = true;
                    }


                    //Creamos una cadena donde capturamos los valores de los parametros para mandarlos a la pantalla de resumen.
                    String valores = "";
                    valores = valores + formulaActual.getIdFormula() + ";" ;

                    //Si el formulario tiene valores sin rellenar.
                    if (valoresEnBlanco) {
                        mensaje.setText("No es posible calcular la formula con valores en blanco");
                    }

                    //Tanto como para esta opcion como la anterior los calculos realizaran en la pantalla resumen
                    else {
                        //Expression expression = new Expression(formulaActual.getExpresion());


                        for (int i = 0; i < numeroParametros; i++) {

                            //if(i == 0 )
                            //expression.with(formulaActual.getParametro(i).getNombre(), formulaActual.getParametro(i).getValor() );
                            //else
                            //expression.and(formulaActual.getParametro(i).getNombre(), formulaActual.getParametro(i).getValor());
                            valores = valores + formulaActual.getParametro(i).getValor() + ";" ;
                        }

                        //mensaje.setText(cadena);
                        Intent intent =
                                new Intent(DetallesFormula.this, ResumenFormula.class);

                        Bundle b = new Bundle();
                        b.putString("RESUMEN", (String) valores);


                        //Añadimos la información al intent
                        intent.putExtras(b);

                        //Iniciamos la nueva actividad
                        startActivity(intent);

                        //mensaje.setText("Primer parametro" + formulaActual.getParametro(0).getNombre() +  " su valor" +formulaActual.getParametro(0).getValor()+ ". Segundo parametro" +formulaActual.getParametro(1).getNombre()+ " su valor" +formulaActual.getParametro(1).getValor()+ ""  );
                        //BigDecimal resultadoEcuacion = expression.eval();
                        //Por ahora mostramos el resultado por pantalla.
                        //mensaje.setText( expression.eval().toString() );

                        //mensaje.setText("Valor antes de meterle nada" + valor1 +   "Valor despues de meterle todo" +valor2+  "");
                    }
                }
            });
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalles_formula, menu);
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
                    new Intent(DetallesFormula.this, FormulasPrincipal.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
