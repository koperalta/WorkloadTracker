package workloadtracker;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBConnectionUtil {
    private static DBConnectionUtil instance;
    
    // Define three separate DataSource variables for Phase 1 constraints
    private DataSource mysqlDataSource;
    private DataSource derbyDataSource;
    private DataSource postgresDataSource;

    private DBConnectionUtil() throws NamingException {
        Context initContext = new InitialContext();
        
        // Look up all three connection pools configured in your GlassFish Server
        this.mysqlDataSource = (DataSource) initContext.lookup("jdbc/MySQLWorkDB");
        this.derbyDataSource = (DataSource) initContext.lookup("jdbc/DerbyAuthDB");
        this.postgresDataSource = (DataSource) initContext.lookup("jdbc/PostgresLogDB");
    }

    public static synchronized DBConnectionUtil getInstance() throws NamingException {
        if (instance == null) {
            instance = new DBConnectionUtil();
        }
        return instance;
    }

    // Retained for backward compatibility with your existing WorkloadDAO
    public Connection getConnection() throws SQLException {
        return mysqlDataSource.getConnection();
    }

    // New method for IdentityDAO (Authentication & Roles)
    public Connection getDerbyConnection() throws SQLException {
        return derbyDataSource.getConnection();
    }

    // New method for AuditDAO (Time-Series & PDF Ledger)
    public Connection getPostgresConnection() throws SQLException {
        return postgresDataSource.getConnection();
    }
}