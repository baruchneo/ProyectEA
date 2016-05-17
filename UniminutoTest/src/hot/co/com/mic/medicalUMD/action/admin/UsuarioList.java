package co.com.mic.medicalUMD.action.admin;

import co.com.mic.medicalUMD.modelo.Usuario;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.util.Arrays;
import java.util.List;


@Name("usuarioList")
public class UsuarioList extends EntityQuery<Usuario>
{
    //---------------------------------------------------------------//
    // Class constants
    //---------------------------------------------------------------//

    /**
     * Default serial version Id
     */
    private static final long serialVersionUID = 1L;

    private static final String EJBQL = "select usuario from Usuario usuario";

    private static final String[] RESTRICTIONS =
            {
                    "lower(usuario.usuario) like lower(concat('%', concat(#{usuarioList.usuario.usuario},'%')))",
                    "usuario.id = #{usuarioList.usuario.id} ",
                    "usuario.fechaCreacion = #{usuarioList.usuario.fechaCreacion}",
                    "lower(usuario.rol.nombreRol) like lower(concat('%', concat(#{usuarioList.usuario.rol.nombreRol},'%')))",
                    "lower(usuario.estadoUsuario) like lower(concat('%', concat(#{usuarioList.usuario.estadoUsuario},'%')))",
            };


    //---------------------------------------------------------------//
    // Class attributes
    //---------------------------------------------------------------//

    private Usuario usuario = new Usuario();

    //---------------------------------------------------------------//
    // Class constructors methods
    //---------------------------------------------------------------//

    public UsuarioList()
    {
        setOrderColumn("usuario.id");
        setOrderDirection("asc");
        setEjbql(EJBQL);
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        //setMaxResults(25);
    }

    //---------------------------------------------------------------//
    // Class getters methods
    //---------------------------------------------------------------//

    public Usuario getUsuario()
    {
        return usuario;
    }

    //---------------------------------------------------------------//
    // Class setters methods
    //---------------------------------------------------------------//

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario)
    {
        this.usuario = usuario;
    }

    //---------------------------------------------------------------//
    // Class business methods
    //---------------------------------------------------------------//

    @Override
    public List<Usuario> getResultList()
    {
        return super.getResultList();
    }
}
