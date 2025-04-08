/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodordering;

import java.io.Serializable;

/**
 *
 * @author ianwd
 */
public class AccountSerialization implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private String Id;
    private String password;
    
    AccountSerialization(String id, String password){
        this.Id=id;
        this.password=password;
    }
    
    public String getId(){
        return this.Id;
    }
    
     public String getPassword(){
        return this.password;
    }
}
