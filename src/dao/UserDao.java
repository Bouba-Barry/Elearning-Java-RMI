package dao;

import connectionDB.ConnectDB;
import metier.Classe;
import metier.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private static Connection connection = ConnectDB.getConnection();

    public static void addUser(User user) {
        String sql = "INSERT INTO User (nom, email,password, type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getNom());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getType());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            ConnectDB.closeConnection();
        }
    }

    public static int readId(String username) {
        int id = -1;
        String sql = "SELECT id FROM User WHERE nom = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public static List<User> getAllUsers(){
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM User";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                String username = resultSet.getString("nom");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String type = resultSet.getString("type");
                users.add(new User(username,email, password,type));
            }
        }catch (SQLException e) {
            e.printStackTrace();
            // ConnectDB.closeConnection();
        }
        return users;
    }

    public static List<User> getAllAdmin(){
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM User Where type = ? ";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,"Admin");
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                String username = resultSet.getString("nom");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String type = resultSet.getString("type");
                users.add(new User(username,email, password,type));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static List<User> getAllTeachers(){
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM User Where type = ? ";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,"teacher");
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                String username = resultSet.getString("nom");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String type = resultSet.getString("type");
                users.add(new User(username,email, password,type));
            }
        }catch (SQLException e) {
            e.printStackTrace();
            // ConnectDB.closeConnection();
        }
        return users;
    }
    public static List<User> getAllStudents(){
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM User Where type = ? ";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,"student");
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                String username = resultSet.getString("nom");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String type = resultSet.getString("type");
                users.add(new User(username,email, password,type));
            }
        }catch (SQLException e) {
            e.printStackTrace();
            // ConnectDB.closeConnection();
        }
        return users;
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
    public static User getUserById(int id){
        String sql = "SELECT * FROM User Where id = ? ";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                int idUser = resultSet.getInt("id");
                String username = resultSet.getString("nom");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String type = resultSet.getString("type");
                int classe_id = resultSet.getInt("classe_id");
                return new User(idUser, username, email, password, type, classe_id);

            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<User> getAllUserByClasse(int classe_id){
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM User Where classe_id = ? ";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,classe_id);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                int idUser = resultSet.getInt("id");
                String username = resultSet.getString("nom");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String type = resultSet.getString("type");
                int classeUser = resultSet.getInt("classe_id");
                users.add(new User(idUser, username, email, password, type, classeUser));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static User getUserByName(String name){
        String sql = "SELECT * FROM User Where nom = ? ";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,name);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                int idUser = resultSet.getInt("id");
                String username = resultSet.getString("nom");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String type = resultSet.getString("type");
                int classe_id = resultSet.getInt("classe_id");
                return new User(idUser, username, email, password, type, classe_id);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User getUserByEmail(String email){
        String sql = "SELECT * FROM User Where email = ? ";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,email);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                int idUser = resultSet.getInt("id");
                String username = resultSet.getString("nom");
                String mail = resultSet.getString("email");
                String password = resultSet.getString("password");
                String type = resultSet.getString("type");
                int classe_id = resultSet.getInt("classe_id");
                return new User(idUser, username, mail, password, type, classe_id);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}