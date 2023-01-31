package gui;

import client.ClientRMI;
import dao.ModuleDao;
import dao.RessourceDao;
import dao.UserDao;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import metier.*;
import metier.Module;
import service.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
public class MenuAccueil {

    protected UserRMI userRMI;
    private List<Module> modules;
    private Classe classe_user;
    private Module currentModule = null;
    private User profModule = null;
    protected User currentUser;
    protected UserRemote sessionUser;

    public Scene accueilStudent(Stage primaryStage, String email, ClientRMI clientRMI) {
        this.userRMI = clientRMI.getUserRMI();
        sessionUser = clientRMI.getUserSession();
        try{
            currentUser = userRMI.getUser(email);
        }catch (Exception e) {e.printStackTrace();}

        // recuperation des informations du users .....
        classe_user = UserDao.getClassUser(email);
        modules = ModuleDao.getAllModulesByClasse(classe_user.getId());

        primaryStage.setTitle("Accueil-Etudiant");
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane, 800, 600);

        VBox accueilVbox = new VBox();

        HBox accueilHbox = new HBox();
        Label textAccueilLabel = new Label("Accueil");
        textAccueilLabel.setPadding(new Insets(0, 15, 0, 15));
        Label classNameLabel = new Label("Classes: IRISI");
        if(classe_user != null){
            classNameLabel.setText("Classe: "+classe_user.getSubject());
        }

        Button logoutButton = new Button("Deconnexion");
        logoutButton.setStyle("-fx-background-color: #ff0000");
        logoutButton.setAlignment(Pos.TOP_RIGHT);
        logoutButton.setOnAction(event -> {
            try {
                if (clientRMI.getUserSession().logout(currentUser)){
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.close();
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });

        accueilHbox.setSpacing(250);
        accueilHbox.setPadding(new Insets(15, 15, 5, 0));
        accueilHbox.getChildren().addAll(textAccueilLabel, classNameLabel, logoutButton);

        HBox moduleHbox = new HBox();
        Label moduleListLabel = new Label("Choix Module: ");
        moduleListLabel.setAlignment(Pos.BOTTOM_CENTER);
        ComboBox listModulBox = new ComboBox<>();
        listModulBox.setPrefSize(200, 20);

        for (Module m : modules) {
            listModulBox.getItems().add(m.getLibelle());
        }
        moduleHbox.getChildren().addAll(moduleListLabel, listModulBox);
        moduleHbox.setPadding(new Insets(-15, 70, 0, 250));
        // moduleHbox.getChildren().addAll(moduleListLabel,listModulBox);

        accueilVbox.setSpacing(20);
        accueilVbox.getChildren().addAll(accueilHbox, moduleHbox);
        accueilVbox.setStyle("-fx-background-color: gray");

        listModulBox.setOnAction(ev -> {
            // Code à exécuter lorsque la valeur sélectionnée dans le ComboBox change
            String selectedItem = (String) listModulBox.getValue();
            // faire quelque chose avec selectedItem
            currentModule = ModuleDao.getModuleByName(selectedItem);
            profModule = UserDao.getUserById(currentModule.getTeacher_id());
        });

        VBox ModuleRessourceVbox = new VBox();

        Button coursButton = new Button();
        coursButton.setPrefSize(150, 20);
        //courHbox.getChildren().add(coursButton);
        Button whiteBoardButton = new Button("Tableau Blanc");
        whiteBoardButton.setPrefSize(150, 20);
        Button messagerieButton = new Button("Messagerie");
        messagerieButton.setPrefSize(150, 20);
        Button profButton = new Button();
        if(profModule != null){
            coursButton.setText("Ressource Module "+currentModule.getLibelle());
            profButton.setText("Prof: "+profModule.getNom());
        }
        coursButton.setText("Ressource Cours");
        profButton.setText("Prof Module ");

        profButton.setPrefSize(150, 20);

        VBox mainModuleVbox = new VBox();
        //borderPane.setCenter(mainModuleVbox);

        coursButton.setOnAction(event -> {

            // traitement de la partie borderPane.setCenter
            VBox centerVbox = new VBox();
           // centerVbox.getChildren().addAll(displayRessource(currentModule.getId(),primaryStage));
            if(currentModule != null){
                centerVbox.getChildren().removeAll();
                try{
                    centerVbox.getChildren().addAll(displayRessource(currentModule.getId(),primaryStage));
                }catch (Exception e){e.printStackTrace();}
                centerVbox.setPadding(new Insets(0));
                borderPane.setCenter(centerVbox);
            }
        });

        TitledPane messagerie = new TitledPane();
        messagerie.setPrefSize(150,20);
        HBox HboxLabelUser = new HBox();
        HboxLabelUser.getChildren().add(new Label("Choisir Une conversation"));


        List<User> usersClasse = UserDao.getAllUserByClasse(classe_user.getId());
        ComboBox listUserBox = new ComboBox<>();
        listUserBox.setPrefSize(150, 20);
        for (User u : usersClasse) {
            listUserBox.getItems().add(u.getNom());
        }
        VBox vbox = new VBox();
        vbox.getChildren().addAll(HboxLabelUser,listUserBox);
        messagerie.setContent(vbox);
        messagerie.setText("Messagerie ");
        messagerie.setExpanded(false);

        listUserBox.setOnAction(event -> {
            String userReceiv = (String) listUserBox.getValue();
            User userReceiver = UserDao.getUserByName(userReceiv);

           BorderPane pane =  createConversationBorderPane(userRMI,userReceiver);
           pane.setPadding(new Insets(0,15,0,0));
           borderPane.setCenter(pane);
        });

        profButton.setOnAction(event -> {
            // code de la méssagerie du professeur
        });

        whiteBoardButton.setOnAction(event -> {
            // code pour afficher le tableau blanc
        });

        logoutButton.setOnAction(event -> {

        });

        ModuleRessourceVbox.getChildren().addAll(coursButton, whiteBoardButton, messagerie, profButton);
        ModuleRessourceVbox.setSpacing(20);
        ModuleRessourceVbox.setPadding(new Insets(50, 15, 0, 15));
        ModuleRessourceVbox.setStyle("-fx-background-color: lightgray");
        borderPane.setLeft(ModuleRessourceVbox);
        borderPane.setTop(accueilVbox);
        return scene;
    }

    public TableView<Ressource> displayRessource(int module_id, Stage stage) throws MalformedURLException, NotBoundException, RemoteException {
        // get ressources by module id
        FileRemote fileRemote = (FileRemote)  Naming.lookup("rmi://localhost:1099/FileService");

        List<Ressource> ressources =   fileRemote.getAllFileFromModule(module_id);
        // create a table view to display the ressources
        TableView<Ressource> table = new TableView<>();
        table.setItems(FXCollections.observableArrayList(ressources));
        // create table columns for the ressource name and download button
        //TableColumn<Ressource, Image> iconCol = new TableColumn<>("icon");
        //iconCol.setCellValueFactory(new);

        TableColumn<Ressource, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("ressource_name"));
        TableColumn<Ressource, Button> downloadCol = new TableColumn<>("Download");
        downloadCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ressource, Button>, ObservableValue<Button>>() {
            @Override
            public ObservableValue<Button> call(TableColumn.CellDataFeatures<Ressource, Button> features) {
                Ressource ressource = features.getValue();
                Button downloadBtn = new Button("Download");
                downloadBtn.setOnAction(event -> {
                    /*Ressource r = RessourceDao.getRessourceByName(ressource.getRessource_name());
                    byte[] data = r.getData(); */
                    try{
                        String fileName = ressource.getRessource_name();
                        if(fileName.contains(".txt")){
                            FileChooser fileChooser = new FileChooser();
                            fileChooser.setTitle("Save File");
                            fileChooser.setInitialFileName(fileName);
                            File file = fileChooser.showSaveDialog(stage);
                            if (file != null) {
                                fileRemote.receiveTxtFile(fileName,file);
                            }
                        }else{
                            FileChooser fileChooser = new FileChooser();
                            fileChooser.setTitle("Save File");
                            fileChooser.setInitialFileName(fileName);
                            File file = fileChooser.showSaveDialog(stage);
                            if (file != null) {
                               fileRemote.receiveFileBinary(fileName,file);
                            }
                        }
                    }
                    catch (Exception e){e.printStackTrace();}

                });
                return new SimpleObjectProperty<>(downloadBtn);
            }
        });
        table.getColumns().addAll(nameCol, downloadCol);
        return table;
    }

    public BorderPane createCenterPane(){
        BorderPane borderPane = new BorderPane();

        return borderPane;
    }

    public void addMessageInConversationBox(String message,VBox leftMessageDisplay,VBox rightMessageDisplay,BorderPane conversationBox)
    {
        Label leftLabel = new Label(message);
        leftLabel.setFont(Font.font("bold",15));
        Label rightLabel = new Label(message);
        rightLabel.setFont(Font.font("bold",15));
        HBox leftHBox = new HBox();
        HBox rightHBox = new HBox();
        rightHBox.getChildren().add(rightLabel);
        rightLabel.setStyle("-fx-background-color: #C0C0C0");
        rightMessageDisplay.getChildren().add(rightHBox);
        leftLabel.setVisible(false);
        leftHBox.getChildren().add(leftLabel);
        leftMessageDisplay.getChildren().add(leftHBox);
    }

    public BorderPane createConversationBorderPane(UserRMI user, User receiverInfo){
        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox();
        String receiverName = receiverInfo.getNom();
        Label receiverNameLabel = new Label(receiverName);
        receiverNameLabel.setFont(Font.font("bold",30));


        hBox.getChildren().addAll(receiverNameLabel);
        hBox.setSpacing(30);
      // ce qui est ajouté de suite ....
        hBox.setPadding(new Insets(10,0,0,150));
        hBox.setStyle("-fx-background-color: white");

        borderPane.setTop(hBox);
        //Creating method that generate conversation scrolling
        List<Messagerie> messageList = new ArrayList<>();
        try{
            ChatRemote chatRemote = (ChatRemote) Naming.lookup("rmi://localhost:1099/ChatService");
            messageList = chatRemote.getMessages(currentUser.getId(),receiverInfo.getId());
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Erreur de recupération de conversation");
        }
        System.out.println(messageList);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefHeight(500);
        scrollPane.setVvalue(300);
        VBox leftMessageDisplay = new VBox();
        VBox rightMessageDisplay = new VBox();
        int i=0;
        for (Messagerie message:messageList) {
            HBox leftHBox = new HBox();
            HBox rightHBox = new HBox();
            Label leftLabel = new Label(message.getContent());
            leftLabel.setFont(Font.font("bold",15));
            Label rightLabel = new Label(message.getContent());
            rightLabel.setFont(Font.font("bold",15));
            if (message.getSender_id()==receiverInfo.getId()){
                //Message have to be displayed at left
                leftHBox.getChildren().add(leftLabel);
                leftHBox.setStyle("-fx-background-color: lightgray");
                leftMessageDisplay.getChildren().add(leftHBox);
                rightLabel.setVisible(false);
                rightHBox.getChildren().add(rightLabel);
                rightMessageDisplay.getChildren().add(rightHBox);
            }else {
                //Message have to be displayed at right
                rightHBox.getChildren().add(rightLabel);
                rightLabel.setStyle("-fx-background-color: #C0C0C0");
                rightMessageDisplay.getChildren().add(rightHBox);
                leftLabel.setVisible(false);
                leftHBox.getChildren().add(leftLabel);
                leftMessageDisplay.getChildren().add(leftHBox);
            }

        }
        BorderPane conversationBox = new BorderPane();
        leftMessageDisplay.setSpacing(20);
        rightMessageDisplay.setSpacing(20);
        conversationBox.setLeft(leftMessageDisplay);
        conversationBox.setRight(rightMessageDisplay);
        conversationBox.setPrefWidth(450);
        scrollPane.setContent(conversationBox);

        borderPane.setCenter(scrollPane);

        HBox writingHbox = new HBox();
        TextArea messageField = new TextArea();
        messageField.setPrefWidth(700);
        messageField.setPrefHeight(30);
        messageField.setBorder(Border.EMPTY);

        Button sendButton = new Button("envoye");
        sendButton.setPrefSize(100,40);
        sendButton.setFont(Font.font("bold",20));
        sendButton.setTextFill(Color.WHITE);
       sendButton.setStyle("-fx-background-color: green");
        sendButton.setOnAction(event -> {
            String messageToSend = messageField.getText();
            if (!messageToSend.equals("")){
                /*DateFormat dateFormat = new SimpleDateFormat();
                String dateToString = dateFormat.format(new Date());*/

                try {
                    ChatRemote messageRemote = (ChatRemote) Naming.lookup("rmi://localhost:1099/ChatService");
                    if (messageRemote.sendMessage(messageToSend,currentUser.getId(), receiverInfo.getId())){
                        addMessageInConversationBox(messageToSend,leftMessageDisplay,rightMessageDisplay,conversationBox);
                        for(User u : sessionUser.getAllLoggedUser()){
                            if(u.getId() == receiverInfo.getId()){
                                u.setNotifyUser(false);
                                SharedMessageRemote sharedMes = (SharedMessageRemote) Naming.lookup("rmi://localhost:1099/SharedService");
                                sharedMes.setContent(new Messagerie(currentUser.getId(),receiverInfo.getId(),messageToSend,"envoyé"));
                                sharedMes.setReceiver(receiverInfo.getId());
                                sharedMes.setSender(currentUser.getId());
                                if(sharedMes.getContent() != null && sharedMes.getReceiver() == currentUser.getId()){
                                    addMessageInConversationBox(sharedMes.getContent().getContent(),rightMessageDisplay,leftMessageDisplay,conversationBox);
                                }
                                u.setNewMessage(messageToSend);
                            }
                        }
                        messageField.setText("");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Failed to send new message !!!");
                }
            }

        });
        writingHbox.getChildren().addAll(messageField,sendButton);
        writingHbox.setPadding(new Insets(10));
        writingHbox.setStyle("-fx-background-color: white");
        borderPane.setBottom(writingHbox);

        return borderPane;
    }
}