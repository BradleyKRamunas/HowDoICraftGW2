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

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", quantity=" + quantity +
                '}';
    }
}
