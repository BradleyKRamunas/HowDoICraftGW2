package data.database;

import data.api.api_objects.Recipe;

public interface BaseDatabaseTool {

    Recipe getRecipeForId(int id);
}
