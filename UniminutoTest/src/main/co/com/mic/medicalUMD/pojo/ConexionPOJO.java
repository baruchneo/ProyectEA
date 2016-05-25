package co.com.mic.medicalUMD.pojo;


public class ConexionPOJO
{

    private String identificacionPaciente;
    private String usuario;
    private String password;
    private String identificacionDoctor;

    //------------------------------------------- Constructors --------------------------------------------------//

    public ConexionPOJO(String identificacionPaciente, String usuario, String password, String identificacionDoctor) {
        this.identificacionPaciente = identificacionPaciente;
        this.usuario = usuario;
        this.password = password;
        this.identificacionDoctor = identificacionDoctor;
    }

    public ConexionPOJO() {
    }

    //------------------------------------------- Getters --------------------------------------------------//


    public String getIdentificacionPaciente() {
        return identificacionPaciente;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getPassword() {
        return password;
    }

    public String getIdentificacionDoctor() {
        return identificacionDoctor;
    }

    //------------------------------------------- Setters --------------------------------------------------//

    public void setIdentificacionPaciente(String identificacionPaciente) {
        this.identificacionPaciente = identificacionPaciente;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIdentificacionDoctor(String identificacionDoctor) {
        this.identificacionDoctor = identificacionDoctor;
    }


    //------------------------------------------- Business Method  --------------------------------------------------//

    @Override
    public String toString()
    {
        return new StringBuffer("identificacionPaciente : ").append(this.identificacionPaciente)
                .append("usuario : ").append(this.usuario)
                .append("password : ").append(this.password)
                .append("identificacionDoctor : ").append(this.identificacionDoctor)
                .toString();
    }
}
