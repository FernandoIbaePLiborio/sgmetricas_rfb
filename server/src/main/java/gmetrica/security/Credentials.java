package gmetrica.security;

import java.io.Serializable;

public class Credentials implements Serializable {

	private static final long serialVersionUID = 2160046808572058369L;
	
	private String login;
	
    private String password;
    
    public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Credentials{" + "username=" + login + ", password=" + password + '}';
    }

}
