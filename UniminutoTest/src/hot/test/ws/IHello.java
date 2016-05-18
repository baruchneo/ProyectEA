package test.ws;

import javax.ejb.Remote;

@Remote
public interface IHello {
    public String contestar(String name);
}
