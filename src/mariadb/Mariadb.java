package mariadb;

import java.sql.*;
import com.rivescript.macro.Subroutine;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Jan Julius de Lang
 */
public class Mariadb implements Subroutine {

    @Override
    public String call(com.rivescript.RiveScript rs, String[] args) {
        String host = args[0];
        String port = args[1];
        String db = args[2];
        String username = args[3];
        String password = args[4];
        String sql = "";
        String result = "";
        for (int i=5; i<args.length; i++)
            sql = sql + " " + args[i];
        sql = sql.trim();

        String address = "jdbc:mariadb://" + host + ":" + port + "/" + db + "?autoReconnect=true&useSSL=false";

        System.out.println(address);

        System.out.println(sql);

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection=(Connection) DriverManager.getConnection(
                    address,
                    username, password);
            statement=(Statement) connection.createStatement();
            resultSet=statement.executeQuery(sql);
            while(resultSet.next()) {
                int i = resultSet.getMetaData().getColumnCount();
                for (int j = 1; j <= i; j++) {
                    if (result.equals("")) {
                        result = resultSet.getString(j);
                    } else {
                        result += resultSet.getString(j) + " ";
                    }
                }
                if (!result.equals(""))
                    result += "\n";
            }
        } catch (SQLException ex) {
        } finally{
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException ex) {
            }
        }

        return result;
    }

}
