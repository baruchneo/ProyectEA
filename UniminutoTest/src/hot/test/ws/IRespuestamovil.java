package test.ws;

import javax.ejb.Remote;

@Remote
public interface IRespuestamovil {
    public String resmovilcontestarusuarioWS(String resmovusarioWS);
    public String resmovilcontestarpasswordWS(String resmovpasswordWS);

    public String resmovilcontestarrol(String resmovcodUnificacoPaciente);

}
