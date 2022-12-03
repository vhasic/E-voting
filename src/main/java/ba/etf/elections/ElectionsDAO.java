package ba.etf.elections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ElectionsDAO {
    private static ElectionsDAO instance;
    private Connection conn;


    private ElectionsDAO() {
        try {
            String url = "jdbc:sqlite:elections.sqlite";
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ElectionsDAO getInstance() {
        if (instance == null) instance = new ElectionsDAO();
        return instance;
    }

    public static void removeInstance() {
        if (instance != null) {
            try {
                instance.conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        instance = null;

    }
}
