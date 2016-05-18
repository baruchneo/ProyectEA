package test.ws;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebServiceContext;

@WebService(name="Saludo", serviceName="SaludoServicio")
@SOAPBinding(style=SOAPBinding.Style.RPC)
@Stateless
public class Saludo implements IHello
{
    private String message;

    public void Saludo()
    {
        message = new String ("Hola ");
    }

    @WebMethod
    public String contestar(String name){
        return message + name;
    }

}
