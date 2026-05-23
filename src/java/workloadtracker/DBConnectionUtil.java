package workloadtracker;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBConnectionUtil {
    private static DBConnectionUtil instance;
    private DataSource dataSource;

    private DBConnectionUtil() throws NamingException {
        Context initContext = new InitialContext();
        // Looking up the connection pool managed by Glassfish
        this.dataSource = (DataSource) initContext.lookup("jdbc/MySQLWorkDB");
    }

    public static synchronized DBConnectionUtil getInstance() throws NamingException {
        if (instance == null) {
            instance = new DBConnectionUtil();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}