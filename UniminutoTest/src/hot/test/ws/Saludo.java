package test.ws;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.rmi.RemoteException;


@WebService(endpointInterface = "test.ws.ISaludo", serviceName = "SaludoServicio")
@Stateless
public class Saludo implements ISaludo
{
    private String message;

    public void Saludo()
    {
        message = new String ("Hola ");
    }

    @Override
    @WebMethod
    public String contestar(@WebParam(name = "name")String name)
    {
        return message + name;
    }

}
