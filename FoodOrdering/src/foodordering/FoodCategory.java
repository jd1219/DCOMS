/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodordering;

/**
 *
 * @author cleme
 */
public class FoodCategory {

    private String catID;
    private String catDesc;

    public FoodCategory(String catID, String catDesc) {
        this.catID = catID;
        this.catDesc = catDesc;

    }

    //getters
    public String getCatID() {
        return catID;
    }

    
    public String getCatDesc() {
        return catDesc;
    }
    
    public String toString(){
        return this.catDesc;
    }

}
