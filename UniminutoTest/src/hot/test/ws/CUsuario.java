package test.ws;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name="CUsuario", serviceName="SaludoUsuario")
@SOAPBinding(style=SOAPBinding.Style.RPC)
@Stateless
public class CUsuario implements IUser
{
    private String login;
    private String password;
    private String rol;

    public void CUsuario()
    {

        login = new String ("login ");
        password = new String ("password ");
        rol = new String ("rol");
    }

    @WebMethod
    public String usercontestarlogin(String Userlogin){
        return login + Userlogin;
    }

    @Override
    public String usercontestarpassword(String Userpassword) {
        return null;
    }

    @Override
    public String usercontestarrol(String Userrol) {
        return null;
    }

}
