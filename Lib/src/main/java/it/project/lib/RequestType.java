package it.project.lib;

import java.io.Serializable;

public class RequestType implements Serializable {
    private String email; // email of the login
    private int type;   /**
                          * 1 -> login request
                          * 2 -> send email request
                          * 3 -> receive email request
                          * 4 -> delete email request
                          */

    public RequestType(String email, int type) {
        this.email = email;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }
    public int getType(){
        return type;
    }

}
