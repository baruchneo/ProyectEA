package co.com.mic.medicalUMD.pojo;


public class EncuestaPOJO
{

    private String fechaRadicado;
    private String preguntas;
    private Integer encuestaVista;

    //------------------------------------------- Constructors --------------------------------------------------//

    public EncuestaPOJO(String fechaRadicado, String preguntas, Integer encuestaVista) {
        this.fechaRadicado = fechaRadicado;
        this.preguntas = preguntas;
        this.encuestaVista = encuestaVista;
    }
    
    //------------------------------------------- Getters --------------------------------------------------//

    public String getFechaRadicado() {
        return fechaRadicado;
    }

    public String getPreguntas() {
        return preguntas;
    }

    public Integer getEncuestaVista() {
        return encuestaVista;
    }

    //------------------------------------------- Setters --------------------------------------------------//

    public void setFechaRadicado(String fechaRadicado) {
        this.fechaRadicado = fechaRadicado;
    }

    public void setPreguntas(String preguntas) {
        this.preguntas = preguntas;
    }

    public void setEncuestaVista(Integer encuestaVista) {
        this.encuestaVista = encuestaVista;
    }

    //------------------------------------------- Business Method  --------------------------------------------------//

    @Override
    public String toString()
    {
        return new StringBuffer("Fecha Radicacion : ").append(this.fechaRadicado)
            .append("Preguntas : ").append(this.preguntas)
            .append("Encuestavista : ").append(this.encuestaVista)
            .toString();
    }
}