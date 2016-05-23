package test.ws;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/restSaludo")
public interface IRESTSaludo
{
    @POST
    @Path("/saludo")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSaludo();

    @POST
    @Path("/saluda/{nombre}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getSaludaA(@PathParam("nombre") String nombre);
}
