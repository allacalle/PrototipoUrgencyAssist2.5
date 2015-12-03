package net.allacalle.android.prototipourgencyassist25;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class FormulasSQLiteHelper extends SQLiteOpenHelper {

    //Sentencia SQL para crear la tabla de Usuarios
    String sqlCreateFormula = "CREATE TABLE Formulas ( IdFormula INTEGER,NombreCompleto TEXT, Abreviatura TEXT, Expresion TEXT)";
    String sqlCreateParametro = "CREATE TABLE Parametros (IdParametro INTEGER, NombreParametro TEXT, IdFormula Integer, TipoParametro TEXT, Medida TEXT) ";
    String sqlCreateCriterioPuntuacion = "CREATE TABLE CriterioPuntuacion(IdCriterioPuntuacion INTEGER, IdParametro INTEGER, Criterio TEXT, Puntuacion TEXT)";
    String sqlCreatePrioridad = "CREATE TABLE Prioridad(IdPrioridad INTEGER, IdFormula INTEGER, Tipo TEXT)";
    String sqlCreateRecientes = "CREATE TABLE Recientes(IdRecientes INTEGER, IdFormula INTEGER, Fecha TEXT)";





    public FormulasSQLiteHelper(Context contexto, String nombre,
                                CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreateFormula);
        db.execSQL(sqlCreateParametro);
        db.execSQL(sqlCreateCriterioPuntuacion);
        db.execSQL(sqlCreatePrioridad);
        db.execSQL(sqlCreateRecientes);
        //Este comentario deberia borrarse
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Formulas");
        db.execSQL("DROP TABLE IF EXISTS Parametros");
        db.execSQL("DROP TABLE IF EXISTS CriterioPuntuacion");
        db.execSQL("DROP TABLE IF EXISTS Prioridad");
        db.execSQL("DROP TABLE IF EXISTS Recientes");

        onCreate(db);

    }
}