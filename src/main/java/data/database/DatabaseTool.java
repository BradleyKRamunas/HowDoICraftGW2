package data.database;

import com.google.gson.Gson;
import data.api.api_objects.Recipe;

import java.io.File;
import java.sql.*;
import java.util.List;

public class DatabaseTool implements BaseDatabaseTool {

    private static BaseDatabaseTool instance;
    private static Gson gson;

    public static void init() throws SQLException {
        instance = new DatabaseTool();
        gson = new Gson();
        Connection connection = DriverManager.getConnection("jdbc:sqlite:gw2Recipe.db");
        connection.close();
    }

    public static BaseDatabaseTool getInstance() {
        return instance;
    }

    public void setupDatabase() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:gw2Recipe.db");
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS recipes (" +
                "item_id integer, quantity integer, chatlink string, recipe string)");
        statement.close();
        connection.close();
    }

    @Override
    public void setBuild(int version) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:gw2Recipe.db");
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM recipes WHERE item_id = -1");
        statement.executeUpdate("INSERT INTO recipes VALUES(-1, " + version + ", '', '')");
        statement.close();
        connection.close();
    }

    @Override
    public int getBuild() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:gw2Recipe.db");
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM recipes WHERE item_id = -1 LIMIT 1");
        int version = -1;
        if(rs.isBeforeFirst()) {
            rs.next();
            version = rs.getInt("quantity");
        }
        rs.close();
        statement.close();
        connection.close();
        return version;
    }

    @Override
    public Recipe getRecipeForId(int id) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:gw2Recipe.db");
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM recipes WHERE item_id = " + id + " LIMIT 1");
        rs.next();
        Recipe recipe = Recipe.EMPTY_RECIPE;
        if(!rs.isAfterLast()) {
            String json = rs.getString("recipe");
            recipe = gson.fromJson(json, Recipe.class);
        }
        rs.close();
        statement.close();
        connection.close();
        return recipe;
    }

    @Override
    public void putRecipes(List<Recipe> recipes) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:gw2Recipe.db");
        Statement statement = connection.createStatement();
        for(Recipe recipe : recipes) {
            String json = gson.toJson(recipe);
            statement.executeUpdate("INSERT INTO recipes VALUES(" +
                    recipe.output + ", " + recipe.quantity + ", '" + recipe.chatLink + "', '" + json + "')");
        }
        statement.close();
        connection.close();
    }

}
