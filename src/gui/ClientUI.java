package gui;

import client.ClientRMI;
import dao.UserDao;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import service.UserRMI;

import java.io.FileInputStream;
import java.rmi.RemoteException;

public class ClientUI extends Application {
    private ClientRMI clientRMI;
    private String role;
    private String email;
    private String password;
    private String username;
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        clientRMI = new ClientRMI();
        primaryStage.setTitle("E-Learning");
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane,800,600);
        HBox hboxTitre = new HBox();
        Label authLabel = new Label("Authentification");
       // authLabel.setStyle();
        authLabel.setFont(Font.font(40));
        Label errorLabel = new Label("Error: Mot de pass ou email incorrecte !");
        //errorLabel.setStyle("-fx-color-label-visible: #ff0000");
         errorLabel.setTextFill(Color.RED);
        hboxTitre.getChildren().add(authLabel);
        hboxTitre.setPadding(new Insets(0,0,0,-50));
        //borderPane.setCenterShape(true);
        VBox vbox1 = new VBox();
        HBox imgHbox = new HBox();

        ImageView imageView = new ImageView(new Image(new FileInputStream("ressource/login.png")));
        imageView.setFitHeight(150);
        imageView.setFitWidth(200);
       // imageView.setStyle("-fx-background-color: #c0c0c0");
        imgHbox.getChildren().add(imageView);

        Label lblEmail = new Label("Email: ");
        lblEmail.setPadding(new Insets(10,0,0,0));
        lblEmail.setPrefHeight(20);
        TextField tfEmail = new TextField();
        tfEmail.setPrefSize(300,20);
        HBox hBoxMail = new HBox();
        hBoxMail.getChildren().addAll(lblEmail,tfEmail);
        hBoxMail.setSpacing(45);

        Label lablPassword = new Label("Password: ");
        lablPassword.setPadding(new Insets(10,0,0,0));
        lablPassword.setPrefHeight(20);
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefSize(300,20);
        HBox hboxPassword = new HBox();
        hboxPassword.getChildren().addAll(lablPassword,passwordField);
        hboxPassword.setSpacing(20);


        HBox hboxError = new HBox();
        hboxError.getChildren().add(errorLabel);
        hboxError.setPadding(new Insets(-10,0,0,100));
        errorLabel.setVisible(false);

        Button saveButton = new Button("connexion");
        saveButton.setStyle("-fx-background-color: #00ff00");
        HBox hBoxLogButton = new HBox();
        hBoxLogButton.getChildren().add(saveButton);

        //vbox1.getChildren().addAll(gp);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(hboxTitre,imgHbox);

        vBox.setPadding(new Insets(50,0,0,280));
        vBox.setSpacing(10);
        borderPane.setTop(vBox);
        vbox1.getChildren().addAll(hBoxMail,hboxPassword,hboxError,hBoxLogButton);
        hBoxLogButton.setAlignment(Pos.TOP_RIGHT);
        hBoxLogButton.setPadding(new Insets(-15,250,0,0));
        vbox1.setSpacing(20);
        vbox1.setPadding(new Insets(25,0,0,155));

        borderPane.setCenter(vbox1);


        saveButton.setOnAction(event -> {
            email = tfEmail.getText();
            password = new String(passwordField.getText());

            if(clientRMI.login(email, password)){
                try{
                    if(clientRMI.getUserRMI().getRole().equals("student")){
                        primaryStage.setScene(new MenuAccueil().accueilStudent(primaryStage,email, clientRMI));
                    }
                    else if(clientRMI.getUserRMI().getRole().equals("teacher")){
                        primaryStage.setScene(new DashboardTeacher().DashboardTeacher(primaryStage,email,clientRMI));
                    }
                }catch (Exception e){e.printStackTrace();}
            }else{
                errorLabel.setVisible(true);
            }
        });

      primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
          @Override
          public void handle(WindowEvent event) {
              try {
                  clientRMI.getUserSession().logout(UserDao.getUserByEmail(email));
                  System.out.println("deconnexion du user ");
              } catch (RemoteException e) {
                  throw new RuntimeException(e);
              }
          }
      });

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}
