import java.sql.*;

public class JdbcDatabase implements Database {

    private Connection connection;

    private void connect() {
        try {
            boolean isConnected = isConnected();

            if(!isConnected()) {
                connection = DriverManager.getConnection(DatabaseConstants.JdbcConnectionString,
                        DatabaseConstants.JdbcUserId,
                        DatabaseConstants.JdbcPassword);
            }
        }
        catch (SQLException sqle) {
            throw new RuntimeException("Error connecting to database!", sqle);
        }
    }

    private boolean isConnected() throws SQLException {
        return (connection != null) && !connection.isClosed();
    }

    public ResultSet runQuery(String sql) {
        connect();

        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            return result;
        }
        catch (SQLException sqle) {
            throw new RuntimeException("Error executing query!", sqle);
        }
    }

    public void runUpdate(String sql) {
        connect();

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }
        catch (SQLException sqle) {
            throw new RuntimeException("Error executing update!", sqle);
        }
    }

    public PreparedStatement getPreparedStatement(String sql) {
        connect();

        try {
            return connection.prepareStatement(sql);
        }
        catch (SQLException sqle) {
            throw new RuntimeException("Error creating prepared statement!", sqle);
        }
    }
}