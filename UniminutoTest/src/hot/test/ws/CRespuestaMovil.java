package test.ws;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name="CRespuestaMovil", serviceName="SaludoRespuestaMovil")
@SOAPBinding(style=SOAPBinding.Style.RPC)
@Stateless
public class CRespuestaMovil implements IRespuestamovil
{
    private String usuarioWS;
    private String passwordWS;
    private String codUnificacoPaciente;

    public void CUsuario()
    {

        usuarioWS = new String ("usuarioWd");
        passwordWS = new String ("passwordWS");
        codUnificacoPaciente = new String ("codUnificacoPaciente");
    }


    @Override
    public String resmovilcontestarusuarioWS(String resmovusarioWS) {
        return null;
    }

    @Override
    public String resmovilcontestarpasswordWS(String resmovpasswordWS) {
        return null;
    }

    @Override
    public String resmovilcontestarrol(String resmovcodUnificacoPaciente) {
        return null;
    }
}
