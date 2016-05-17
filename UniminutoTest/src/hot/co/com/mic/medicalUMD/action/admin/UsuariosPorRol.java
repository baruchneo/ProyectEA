package co.com.mic.medicalUMD.action.admin;

import co.com.mic.medicalUMD.modelo.Rol;
import co.com.mic.medicalUMD.modelo.Usuario;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.util.Arrays;
import java.util.List;


@Name("usuariosPorRol")
public class UsuariosPorRol extends EntityQuery<Usuario>
{
    //---------------------------------------------------------------//
    // Class constants
    //---------------------------------------------------------------//

    /**
     * Default serial version Id
     */
    private static final long serialVersionUID = 1L;

    private static final String EJBQL = "select distinct usuarios from Rol rol INNER JOIN rol.usuarios usuarios";

    private static final String[] RESTRICTIONS =
    {
        "rol.id = #{rolHome.instance.id}",
    };


    //---------------------------------------------------------------//
    // Class attributes
    //---------------------------------------------------------------//

    private Rol rol = new Rol();

    //---------------------------------------------------------------//
    // Class constructors methods
    //---------------------------------------------------------------//

    public UsuariosPorRol()
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

    public Rol getRol()
    {
        return rol;
    }

    //---------------------------------------------------------------//
    // Class setters methods
    //---------------------------------------------------------------//

    /**
     * @param rol the rol to set
     */
    public void setRol(Rol rol)
    {
        this.rol = rol;
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
