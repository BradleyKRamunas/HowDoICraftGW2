package datastructure;

import java.util.ArrayList;
import java.util.List;

public class ItemNode {

    private int itemId;
    private int quantity;
    private List<ItemNode> ingredients;

    public ItemNode(int itemId, int quantity, List<ItemNode> ingredients) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.ingredients = ingredients;
    }

    public ItemNode(int itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.ingredients = new ArrayList<>();
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<ItemNode> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<ItemNode> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "ItemNode{" +
                "itemId=" + itemId +
                ", quantity=" + quantity +
                ", ingredients=" + ingredients +
                '}';
    }
}
