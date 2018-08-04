package data.api.api_objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ingredient {
    @Expose
    @SerializedName("item_id")
    public int id;

    @Expose
    @SerializedName("count")
    public int quantity;

    public Ingredient() {
    }

    public Ingredient(int id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", quantity=" + quantity +
                '}';
    }

}
