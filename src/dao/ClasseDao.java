package dao;

import connectionDB.ConnectDB;
import metier.Classe;
import metier.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClasseDao {
    private static Connection connection = ConnectDB.getConnection();

    public static void add(Classe classe) {
        String sql = "INSERT INTO Classe (subject,description) VALUES (?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, classe.getSubject());
            statement.setString(2,classe.getDesc());
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

    public static Classe getClassByName(String email){
        String sql = "SELECT * From Classe WHERE subject = ? ";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,email);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                int id = resultSet.getInt("id");
                String subject = resultSet.getString("subject");
                String descr = resultSet.getString("description");
                String date = resultSet.getTimestamp("date_creation").toString();
                return new Classe(id, subject,descr,date);
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

    public static List<Classe> getAllClasse(){
        List<Classe> classes = new ArrayList<>();
        String sql = "SELECT * FROM Classe";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                int idC = resultSet.getInt("id");
                String subject = resultSet.getString("subject");
                String desc = resultSet.getString("description");
                Timestamp dateTime = resultSet.getTimestamp("date_creation");
                classes.add(new Classe(idC, subject,desc,dateTime.toString()));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }

}
