package data.database;

import data.api.api_objects.Recipe;

import java.sql.SQLException;
import java.util.List;

public interface BaseDatabaseTool {

    void setupDatabase() throws SQLException;
    void setBuild(int version) throws SQLException;
    int getBuild() throws SQLException;
    Recipe getRecipeForId(int id) throws SQLException;
    void putRecipes(List<Recipe> recipes) throws SQLException;
}
