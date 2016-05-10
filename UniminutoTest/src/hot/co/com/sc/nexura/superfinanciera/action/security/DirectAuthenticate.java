package co.com.sc.nexura.superfinanciera.action.security;


import org.jboss.seam.annotations.Name;

@Name("directAuthenticate")
public class DirectAuthenticate
{
    /**
     * Temporal user valores Conecction Request
     */
    private String userFromRequest;

    /**
     * this request login has from supervalores
     */
    private Boolean requestFromValores;

    public String getUserFromRequest() {
        return userFromRequest;
    }

    public void setUserFromRequest(String userFromRequest) {
        this.userFromRequest = userFromRequest;
    }

    public Boolean getRequestFromValores() {
        return requestFromValores;
    }

    public void setRequestFromValores(Boolean requestFromValores) {
        this.requestFromValores = requestFromValores;
    }
}
