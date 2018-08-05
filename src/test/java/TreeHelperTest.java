import data.api.api_objects.Ingredient;
import data.api.api_objects.Recipe;
import data.database.BaseDatabaseTool;
import datastructure.ItemNode;
import datastructure.TreeHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.*;

public class TreeHelperTest {

    /**
     * The tree structure is as follows:
     *                 (A,2)
     *                /  |  \
     *        (B,2)   (C, 4)  (D, 1)
     *          |      /  \
     *       (D, 2) (E, 8) (F, 2)
     *                 |
     *              (D, 1)
     *
     * Resulting mapping should be:
     * D: 74
     * F: 16
     */
    public ItemNode root;
    public BaseDatabaseTool mockDatabase;

    @Before
    public void setUp() throws Exception {
        mockDatabase = new BaseDatabaseTool() {

            @Override
            public void setupDatabase() throws SQLException {

            }

            @Override
            public void setBuild(int version) {

            }

            @Override
            public int getBuild() {
                return 0;
            }

            @Override
            public Recipe getRecipeForId(int id) {
                if(id == 1) { // A
                    Ingredient abIngredient = new Ingredient(2, 2);
                    Ingredient acIngredient = new Ingredient(3, 4);
                    Ingredient adIngredient = new Ingredient(4, 1);
                    List<Ingredient> ingredientList = new ArrayList<>();
                    ingredientList.add(abIngredient);
                    ingredientList.add(acIngredient);
                    ingredientList.add(adIngredient);
                    Recipe aRecipe = new Recipe();
                    aRecipe.ingredients = ingredientList;
                    return aRecipe;
                }
                if(id == 2) { // B
                    Ingredient bdIngredient = new Ingredient(4, 2);
                    List<Ingredient> ingredientList = new ArrayList<>();
                    ingredientList.add(bdIngredient);
                    Recipe bRecipe = new Recipe();
                    bRecipe.ingredients = ingredientList;
                    return bRecipe;
                }
                if(id == 3) { // C
                    Ingredient ceIngredient = new Ingredient(5, 8);
                    Ingredient cfIngredient = new Ingredient(6, 2);
                    List<Ingredient> ingredientList = new ArrayList<>();
                    ingredientList.add(ceIngredient);
                    ingredientList.add(cfIngredient);
                    Recipe cRecipe = new Recipe();
                    cRecipe.ingredients = ingredientList;
                    return cRecipe;
                }
                if(id == 5) { // E
                    Ingredient edIngredient = new Ingredient(4, 1);
                    List<Ingredient> ingredientList = new ArrayList<>();
                    ingredientList.add(edIngredient);
                    Recipe eRecipe = new Recipe();
                    eRecipe.ingredients = ingredientList;
                    return eRecipe;
                }
                return Recipe.EMPTY_RECIPE;
            }

            @Override
            public void putRecipes(List<Recipe> recipes) throws SQLException {

            }
        };
        root = TreeHelper.createTree(1, 2, mockDatabase);
    }

    @Test
    public void testGetItemIds() {
        Set<Integer> ids = TreeHelper.getAllItemIds(root);
        Set<Integer> expectedIds = new TreeSet<>();
        expectedIds.add(1);
        expectedIds.add(2);
        expectedIds.add(3);
        expectedIds.add(4);
        expectedIds.add(5);
        expectedIds.add(6);
        Assert.assertEquals(expectedIds, ids);
    }

    @Test
    public void testIngredientMapping() {
        Map<Integer, Integer> ingredientMapping = new TreeMap<>();
        TreeHelper.populateIngredientMap(root, ingredientMapping);
        Assert.assertEquals(74, (int) ingredientMapping.get(4));
        Assert.assertEquals(16, (int) ingredientMapping.get(6));
    }

    @Test
    public void testCraftingOrder() {
        List<Ingredient> order = TreeHelper.getCraftingOrder(root);
        List<Ingredient> expectedOrder = new ArrayList<>();
        expectedOrder.add(new Ingredient(2,4));
        expectedOrder.add(new Ingredient(5, 64));
        expectedOrder.add(new Ingredient(3, 8));
        expectedOrder.add(new Ingredient(1, 2));
        Assert.assertEquals(expectedOrder, order);
    }
}
