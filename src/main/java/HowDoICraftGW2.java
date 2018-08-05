import data.database.DatabaseTool;
import ui.UserInterface;

import java.sql.SQLException;

public class HowDoICraftGW2 {

    public static void main(String[] args) throws SQLException {
        DatabaseTool.init();
        UserInterface ui = new UserInterface();
    }

}
