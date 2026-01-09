package simulations.Scripts.Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DatabaseUtil {

    private static final Logger log = Logger.getLogger(DatabaseUtil.class.getName());

    public static void updateCreatedTs(String dbUrl, String username, String password, String casId) {
        String updateSql = "UPDATE TEST";

        try (Connection connection = DriverManager.getConnection(dbUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {

            // Set the cas_id parameter
            preparedStatement.setString(1, casId);

            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();
            log.info("Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            log.severe("Failed to update created_ts: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
