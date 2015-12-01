package net.allacalle.android.prototipourgencyassist25;


import android.content.Context;
import android.content.pm.InstrumentationInfo;

/**
 * Created by Alfonso on 20/10/2015.
 */
public class Parametro
{
    private Integer IdParametro;
    private String tipo;
    private String nombre;
    private CriterioPuntuacion [] criterio;
    private String medida;
    private String valor;

    //setters y getters sencillos de la clase



    public String getMedida() {
        return medida;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setIdParametro(Integer idParametro) {
        IdParametro = idParametro;
    }

    public Integer getIdParametro() {
        return IdParametro;
    }

    public void setCriterio(CriterioPuntuacion[] criterio) {
        this.criterio = criterio;
    }

    public CriterioPuntuacion[] getCriterio() {
        return criterio;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public CriterioPuntuacion getCriterioPuntuacion (Integer posicion)
    {
        return criterio[posicion];
    }

    public int buscarPosicionDeCriterio (Integer idCriTerioPuntuacion) {
        //int posicion = -1;
        //Esto no es correcto!!!!!!!!!!!!
        int posicion=0;

        for (int i = 0; i < contarCriterios(); i++) {
            if (criterio[i].getIdCriterioPuntuacion().equals(idCriTerioPuntuacion))
                posicion = i;
        }

        return posicion;

    }


    public int contarCriterios()
    {
        return criterio.length ;

    }



}
