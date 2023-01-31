package dao;

import connectionDB.ConnectDB;
import metier.Classe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClasseDao {
    private static Connection connection = ConnectDB.getConnection();

    public static void add(Classe classe) {
        String sql = "INSERT INTO Classe (subject) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, classe.getSubject());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            ConnectDB.closeConnection();
        }
    }
    public static Classe getClassUser(String email){
        String sql = "SELECT c.* FROM User u, Classe c Where u.classe_id = c.id and u.email = ? ";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,email);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                int id = resultSet.getInt("id");
                String subject = resultSet.getString("subject");
                return new Classe(id, subject);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Classe getClassById(int id){
        String sql = "SELECT * FROM Classe Where id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                int idC = resultSet.getInt("id");
                String subject = resultSet.getString("subject");
                return new Classe(idC, subject);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
