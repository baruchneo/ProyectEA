package test.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/restSaludo")
public interface IRESTSaludo
{
    @GET
    @Path("/saludo")
    public String getSaludo();

    @GET
    @Path("/saluda/{nombre}")
    public String getSaludaA(@PathParam("nombre") String nombre);
}
