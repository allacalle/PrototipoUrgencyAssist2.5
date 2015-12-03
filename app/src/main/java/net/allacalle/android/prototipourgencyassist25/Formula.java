package net.allacalle.android.prototipourgencyassist25;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Alfonso on 20/10/2015.
 */




public class Formula
{
    private Integer IdFormula;
    private String tipoFormula;
    private String nombreCompleto;
    private String abreviatura;
    private String expresion;
    private  Parametro [] parametros;
    private Parametro resultado;

    /* Para evaluar luego el resultado sera necesario

        La id de la formula que se calculo para encontrarla.
        El resultado de la formula.
        Los criterios para evaluar ese resultado
        Los posibles valores que puede tener ese resultado
        El valor final que tiene el resultado despues de haber sido evaluado.
     */


    public Formula(String idFormula, Context context)
    {

        FormulasSQLiteHelper usdbh =
                new FormulasSQLiteHelper(context ,"DbEra", null, 1);
        SQLiteDatabase db = usdbh.getReadableDatabase();

        //Inicializo el curso para recorrer las formulas
        Cursor cursorFormulas = db.rawQuery("SELECT NombreCompleto,Abreviatura,Expresion FROM Formulas  WHERE IdFormula = '" + idFormula + "'  ", null);
        cursorFormulas.moveToFirst();

        //Aignamos el id
        setIdFormula(Integer.parseInt(idFormula));

        //Asignamos el nombre completo
        setNombreCompleto(cursorFormulas.getString(0));
        //Asignamos la abreviatura
        setAbreviatura(cursorFormulas.getString(1));

        //Asignamo la expresion
        setExpresion(cursorFormulas.getString(2));

        //Cerramos el cursor de Formulas
        cursorFormulas.close();

        Cursor cursorParametros ;
        Cursor cursorCriterioPuntuacion;

        //Contamos todos los parametros de interes, omitimos el resultado ya que este no debe mostrarse como dato a introducir mas tarde
        cursorParametros = db.rawQuery("SELECT  COUNT(*) FROM Parametros WHERE IdFormula="+idFormula+" AND TipoParametro<>'resultado'", null);
        cursorParametros.moveToFirst();

        int contadorParametros = Integer.parseInt(cursorParametros.getString(0));
        cursorParametros.close();

        //Usamos el cursor de Parametros para sacar los parametros de las formulas, contamos uno menos para excluir los parametros resultados
        cursorParametros = db.rawQuery(" SELECT  IdParametro,NombreParametro,TipoParametro,Medida  FROM Parametros WHERE IdFormula = "+ getIdFormula()+" ", null);

        //Nos movemos al primer campo del cursor.
        cursorParametros.moveToFirst();


        //Array de parametros donde iran los parametros de la formula
        Parametro parametroArray[] = new Parametro[contadorParametros] ;


        for (int i =0; i < contadorParametros; i++ )
        {
            Parametro parametroFormula = new Parametro();
            //Asignamos el identificador al parametro
            parametroFormula.setIdParametro(Integer.parseInt(cursorParametros.getString(0)));
            //Asignamos el nombre al parametro
            parametroFormula.setNombre(cursorParametros.getString(1));
            //Asignamos el tipo
            parametroFormula.setTipo(cursorParametros.getString(2));
            //Asignamos la medida
            parametroFormula.setMedida(cursorParametros.getString(3));

            //Obtenemos el numero de Criterios de Puntuacion para ese parametro
            cursorCriterioPuntuacion = db.rawQuery("SELECT  COUNT(*)  FROM CriterioPuntuacion WHERE IdParametro = "+ parametroFormula.getIdParametro() +"", null);
            cursorCriterioPuntuacion.moveToFirst();
            int contadorCriterioPuntuacion = Integer.parseInt(cursorCriterioPuntuacion.getString(0));
            cursorCriterioPuntuacion.close();
            cursorCriterioPuntuacion = db.rawQuery(" SELECT  IdCriterioPuntuacion,Criterio,Puntuacion  FROM CriterioPuntuacion WHERE IdParametro = "+ parametroFormula.getIdParametro() +" ", null);
            //Nos colocamos en el primer valor del criterio
            cursorCriterioPuntuacion.moveToFirst();
            CriterioPuntuacion criterioPuntuacionArray[] = new CriterioPuntuacion[contadorCriterioPuntuacion];

            for (int j =0; j < contadorCriterioPuntuacion; j++ )
            {
                CriterioPuntuacion criterioPuntuacionParametro = new CriterioPuntuacion();
                //Asignamos el identificador al criterio
                criterioPuntuacionParametro.setIdCriterioPuntuacion(cursorCriterioPuntuacion.getInt(0));
                //Asignamos el criterio
                criterioPuntuacionParametro.setCriterio(cursorCriterioPuntuacion.getString(1));
                //Asignamos el valor
                criterioPuntuacionParametro.setValor(cursorCriterioPuntuacion.getString(2));
                criterioPuntuacionArray[j] = criterioPuntuacionParametro;
                cursorCriterioPuntuacion.moveToNext();
            }

            //Metemos el array de criterios dentro del parametro
            parametroFormula.setCriterio(criterioPuntuacionArray);

            //Agregamos el parametro a un array de Parametros
            parametroArray[i] = parametroFormula;
            cursorParametros.moveToNext();
        }

        cursorParametros.close();
        //Asignamos a nuestra Formula el array de parametros creado anteriormente.
        setParametros(parametroArray);


    }
    //Aqui empiezan los getter y setters sencillos de la clase.

    public String getTipoFormula()
    {
        return tipoFormula;
    }

    public String getNombreCompleto()
    {
        return nombreCompleto;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public String getExpresion() {
        return expresion;
    }


    public void setTipoFormula(String tipoFormula) {
        this.tipoFormula = tipoFormula;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public void setExpresion(String expresion) {
        this.expresion = expresion;
    }


    public Parametro[] getParametros() {
        return parametros;
    }

    public void setParametros(Parametro[] parametros) {
        this.parametros = parametros;
    }

    public Parametro getResultado() {
        return resultado;
    }


    public void setResultado(Context context)
    {
        FormulasSQLiteHelper usdbh =
                new FormulasSQLiteHelper(context ,"DbEra", null, 1);
        SQLiteDatabase db = usdbh.getReadableDatabase();
        Parametro parametroResultado = new Parametro();
        //this.resultado = resultado;
        Cursor cursorResultado;
        String idResultado;
        //Encontramos el parametro resultado de esta formula
        cursorResultado = db.rawQuery(" SELECT  IdParametro  FROM Parametros WHERE IdFormula = "+ getIdFormula()+" AND TipoParametro='resultado'  ", null);
        cursorResultado.moveToFirst() ;
        idResultado = cursorResultado.getString(0);
        parametroResultado.setIdParametro(Integer.parseInt(idResultado));
        parametroResultado.setTipo("resultado");
        cursorResultado.close();
        cursorResultado = db.rawQuery("SELECT  COUNT(*)  FROM CriterioPuntuacion WHERE IdParametro = " + idResultado + "", null);
        cursorResultado.moveToFirst();
        int contadorCriterioPuntuacion = Integer.parseInt(cursorResultado.getString(0));
        cursorResultado.close();
        cursorResultado = db.rawQuery(" SELECT  IdCriterioPuntuacion,Criterio,Puntuacion  FROM CriterioPuntuacion WHERE IdParametro = "+ idResultado +" ", null);
        CriterioPuntuacion criterioPuntuacionArray[] = new CriterioPuntuacion[contadorCriterioPuntuacion];
        cursorResultado.moveToFirst();
        for (int j =0; j < contadorCriterioPuntuacion; j++ )
        {
            CriterioPuntuacion criterioPuntuacionParametro = new CriterioPuntuacion();
            //Asignamos el identificador al criterio
            criterioPuntuacionParametro.setIdCriterioPuntuacion(cursorResultado.getInt(0));
            //Asignamos el criterio
            criterioPuntuacionParametro.setCriterio(cursorResultado.getString(1));
            //Asignamos el valor
            criterioPuntuacionParametro.setValor(cursorResultado.getString(2));
            criterioPuntuacionArray[j] = criterioPuntuacionParametro;
            cursorResultado.moveToNext();
        }

        //Metemos el array de criterios dentro del parametro
        parametroResultado.setCriterio(criterioPuntuacionArray);
        resultado = parametroResultado;
    }

    public void setIdFormula(Integer idFormula) {
        IdFormula = idFormula;
    }

    public Integer getIdFormula() {
        return IdFormula;
    }

    public int buscarPosicionDeParametro (Integer idParametro)
    {
        int posicion =-1;

        for(int i =0; i < contarParametros();i++ )
        {
            if (parametros[i].getIdParametro().equals(idParametro) )
                posicion = i;
        }

        return posicion ;

    }

    public Parametro getParametro (Integer posicion)
    {
        return parametros[posicion];
    }

    //Aqui acaban los getter y setter sencillos de la clase


    /*

    PROC contarPametros() DEV int
    REQUIERE:
    MODIFICA:
    EFECTOS: Muestra el numero de parametros de una formula

     */

    public int contarParametros()
    {
        return parametros.length ;
    }




}

