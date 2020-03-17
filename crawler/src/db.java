import java.sql.*;

public class db {

    // private static final String URL="";

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/languageCorrection?serverTimezone=UTC";
        String user = "testuser";
        String password = "testuser";
        try {
            // Get a connection to database
            Connection myConn = DriverManager.getConnection(url,user,password);
            // Create a statement
            Statement myStmt = myConn.createStatement();

            select("demo",myStmt);
            if (isFull(myStmt)) {
                System.out.println("already have 1 GB data");
            } else {
                System.out.println("still crawling");
            }

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public static void insert(String tablename,
                              Statement myStmt,
                              String phrase,
                              String sentence,
                              String url) throws SQLException {
        String insertSql = "insert into" + tablename
                + " (phrase, sentence, url)"
                + " values (" + phrase + "," +sentence+"," + url+")";
        myStmt.executeUpdate(insertSql);
    }

    public static void select(String tablename,
                              Statement myStmt) throws SQLException {
        ResultSet myResult = myStmt.executeQuery("select * from demo");
        while (myResult.next()) {
            System.out.println(myResult.getString("phrase") + "    " + myResult.getString("sentence"));
        }
    }

    public static boolean isFull(Statement myStmt) throws SQLException {
        String checkSql = "SELECT"
                +" table_schema AS 'DB Name'"
                +","
                +"ROUND(SUM(data_length + index_length) / 1024 / 1024, 1) AS 'DB Size in MB'"
                +" FROM " +"information_schema.tables "
                +" WHERE "+ "table_schema = 'languageCorrection' "
                +" GROUP BY table_schema";
        ResultSet size  = myStmt.executeQuery(checkSql);
        while (size.next()) {
            System.out.println(size.getString("DB name") + "   "
                    + size.getString("DB Size in MB"));
            if (Double.valueOf(size.getString("DB Size in MB")) > 1024) {
                return true;
            }
        }
        return false;
    }
}
