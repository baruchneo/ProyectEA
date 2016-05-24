package test.ws;

import javax.ejb.Remote;

@Remote
public interface IUser {
    public String usercontestarlogin(String Userlogin);
    public String usercontestarpassword(String Userpassword);

    public String usercontestarrol(String Userrol);

}
