package data.api.api_objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class Recipe {

    @Expose
    @SerializedName("type")
    public String type;

    @Expose
    @SerializedName("output_item_id")
    public int output;

    @Expose
    @SerializedName("output_item_count")
    public int quantity;

    @Expose
    @SerializedName("ingredients")
    public List<Ingredient> ingredients;

    @Expose
    @SerializedName("chat_link")
    public String chatLink;


    @Override
    public String toString() {
        return "Recipe{" +
                "type='" + type + '\'' +
                ", output=" + output +
                ", quantity=" + quantity +
                ", ingredients=" + ingredients +
                ", chatLink='" + chatLink + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return output == recipe.output &&
                quantity == recipe.quantity &&
                Objects.equals(type, recipe.type) &&
                Objects.equals(ingredients, recipe.ingredients) &&
                Objects.equals(chatLink, recipe.chatLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, output, quantity, ingredients, chatLink);
    }

}
