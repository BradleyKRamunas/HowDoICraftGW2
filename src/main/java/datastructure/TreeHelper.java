package datastructure;

import data.api.api_objects.Ingredient;
import data.api.api_objects.Recipe;
import data.database.BaseDatabaseTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TreeHelper {

    /**
     * Create a tree structure representing the recipe to craft the passed in itemId in the passed in quantity.
     * @param itemId id representing the item to be crafted
     * @param quantity number of items to be crafted
     * @param databaseTool tool to access database for recipe information
     * @return root node of the tree structure
     */
    public static ItemNode createTree(int itemId, int quantity, BaseDatabaseTool databaseTool) {
        ItemNode root = new ItemNode(itemId, quantity);
        populateIngredients(root, databaseTool);
        return root;
    }

    /**
     * Recursively populate ingredient lists for each ItemNode in the tree structure
     * @param node current node whose list of ingredients is being populated
     * @param databaseTool tool to access database for recipe information
     */
    private static void populateIngredients(ItemNode node, BaseDatabaseTool databaseTool) {
        Recipe recipeForNode = databaseTool.getRecipeForId(node.getItemId());
        if(recipeForNode != Recipe.EMPTY_RECIPE) {
            List<Ingredient> ingredients = recipeForNode.ingredients;
            List<ItemNode> ingredientNodes = new ArrayList<>();
            for(Ingredient ingredient : ingredients) {
                ItemNode child = new ItemNode(ingredient.id, ingredient.quantity);
                ingredientNodes.add(child);
            }
            node.setIngredients(ingredientNodes);
            for(ItemNode itemNode : node.getIngredients()) {
                populateIngredients(itemNode, databaseTool);
            }
        }
        //if recipeForNode is EMPTY_RECIPE, this node is a leaf node with no ingredients.
    }

    /**
     * Calculate ingredient counts needed to craft the specified item
     * @param root root node of the tree structure, or the ItemNode that represents the item being crafted
     * @param ingredientMapping current mapping of ingredients, passed by reference
     */
    public static void populateIngredientMap(ItemNode root, Map<Integer, Integer> ingredientMapping) {
        treeTraversal(root, ingredientMapping, 1);
    }

    private static void treeTraversal(ItemNode node, Map<Integer, Integer> ingredientMapping, int multiplier) {
        int newMultiplier = node.getQuantity() * multiplier;
        if(isLeaf(node)) {
            if(ingredientMapping.containsKey(node.getItemId())){
                ingredientMapping.put(node.getItemId(), ingredientMapping.get(node.getItemId()) + newMultiplier);
            }else{
                ingredientMapping.put(node.getItemId(), newMultiplier);
            }
        }else{
            for(ItemNode child : node.getIngredients()){
                treeTraversal(child, ingredientMapping, newMultiplier);
            }
        }
    }

    private static boolean isLeaf(ItemNode node) {
        return node.getIngredients().isEmpty();
    }

    private static boolean isParentOfOnlyLeaf(ItemNode node) {
        if(isLeaf(node)) return false;
        for(ItemNode child : node.getIngredients()) {
            if(!isLeaf(child)){
                return false;
            }
        }
        return true;
    }

    public static List<Integer> getCraftingOrder(ItemNode root) {
        List<Integer> order = new ArrayList<>();
        populateCraftingOrder(root, order);
        return order;
    }

    private static void populateCraftingOrder(ItemNode node, List<Integer> order) {
        if(!isLeaf(node)) {
            if(isParentOfOnlyLeaf(node)) {
                order.add(node.getItemId());
            }else{
                for(ItemNode child : node.getIngredients()) {
                    populateCraftingOrder(child, order);
                }
                order.add(node.getItemId());
            }
        }
    }

}
