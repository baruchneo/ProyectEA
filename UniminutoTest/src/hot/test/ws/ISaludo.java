package test.ws;

import javax.ejb.Remote;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.rmi.RemoteException;

@Remote
@WebService()
public interface ISaludo extends java.rmi.Remote
{
    @WebMethod()
    public String contestar(@WebParam(name = "name")String name) throws RemoteException;
}
