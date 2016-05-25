package test.ws;

import java.text.MessageFormat;

public class RESTSaludoImpl implements IRESTSaludo
{

    @Override
    public String getSaludo() {
        return "Hola Mundo !";
    }

    @Override
    public String getSaludaA(String nombre) {
        return MessageFormat.format("Hola {0}!", nombre);
    }
}
