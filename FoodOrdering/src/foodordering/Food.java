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
public class Food {

    private String foodID;
    private String foodName;
    private String foodDesc;
    private String foodCatID;
    private String foodPrice;
    private String foodPicURL;

 

    public Food(String foodID, String foodName, String foodDesc, String foodCatID, String foodPrice, String foodPicURL) {
        this.foodID = foodID;
        this.foodName = foodName;
        this.foodDesc = foodDesc;
        this.foodCatID = foodCatID;
        this.foodPrice = foodPrice;
        this.foodPicURL = foodPicURL;
    }

    //getters
    public String getFoodID() {
        return foodID;
    }

    public String getFoodName() {
        return foodName;
    }
    
    public String getFoodPicURL() {
        return foodPicURL;
    }

    public String getFoodDesc() {
        return foodDesc;
    }
    public String getFoodCatID() {
        return foodCatID;
    }

    public String getFoodPrice() {
        return foodPrice;
    }
}
