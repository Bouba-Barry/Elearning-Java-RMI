package dao;

import connectionDB.ConnectDB;
import metier.Classe;
import metier.Ressource;
import metier.WhiteBoard;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WhiteBoardDao {
    private static Connection connection = ConnectDB.getConnection();
    public static void add(WhiteBoard classe) {
        String sql = "INSERT INTO WhiteBoard (module_id,chemin,content) VALUES (?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
           statement.setInt(1,classe.getModule_id());
           statement.setString(2,classe.getChemin());
            statement.setBinaryStream(3, new ByteArrayInputStream(classe.getContent()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            ConnectDB.closeConnection();
        }
    }

    public static boolean Update(int id, byte[] content) {
        boolean tmp = false;
        String sql = "UPDATE WhiteBoard SET content = ? WHERE module_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(2,id);
            statement.setBinaryStream(1, new ByteArrayInputStream(content));
            statement.executeUpdate();
            tmp = true;
        } catch (SQLException e) {
            e.printStackTrace();
            ConnectDB.closeConnection();
        }
        return tmp;
    }

    public static WhiteBoard getWhiteBoard(int module_id) {
        String sql = "SELECT * FROM WhiteBoard WHERE module_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, module_id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                int id = result.getInt("id");
                String chemin = result.getString("chemin");
                InputStream input = result.getBinaryStream("content");
                byte[] data = new byte[input.available()];
                input.read(data);
                return new WhiteBoard(id,module_id,data,chemin);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
