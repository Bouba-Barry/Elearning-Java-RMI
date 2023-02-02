package gui;

import client.ClientRMI;
import dao.ModuleDao;
import dao.RessourceDao;
import dao.UserDao;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

import java.io.*;
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
        ScrollPane centerScrollPane = new ScrollPane();
        /*** traitement du centre *****************************************/
        VBox centerImgVbox = new VBox();
        ImageView imageViewCenter = null;
        try{
            imageViewCenter = new ImageView(new Image(new FileInputStream("ressource/image/E-LEARNING.png")));
        }catch (Exception e){}
        imageViewCenter.setFitHeight(350);
        imageViewCenter.setFitWidth(550);
        centerImgVbox.getChildren().add(imageViewCenter);
        centerImgVbox.setPadding(new Insets(20));
        centerScrollPane.setContent(centerImgVbox);
        borderPane.setCenter(centerScrollPane);

        /** *******traitement du top *********************************/
        VBox topVBox = new VBox();
        HBox accueilStudent = new HBox();
        Label welcomeLabel = new Label("Dashboard Student");
        welcomeLabel.setFont(Font.font("bold",20));
        welcomeLabel.setTextFill(Color.WHITE);
        //welcomeLabel.setPadding(new Insets(10));
        accueilStudent.getChildren().addAll(welcomeLabel);
        accueilStudent.setPadding(new Insets(20,100,20,10));
        accueilStudent.setAlignment(Pos.TOP_LEFT);

        HBox stdNameHbox = new HBox();
        Label label = new Label("Mr "+currentUser.getNom());
        label.setFont(Font.font("bold",15));
        Image image = null;
        try {
            image = new Image(new FileInputStream("ressource/image/profile.jpg"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        stdNameHbox.getChildren().addAll(imageView,label);
        stdNameHbox.setPadding(new Insets(20,100,20,5));
        stdNameHbox.setAlignment(Pos.TOP_CENTER);

        HBox leftTopHbox = new HBox();
        Button logoutButton = new Button();
        Image image2 = null;
        try {
            image2 = new Image(new FileInputStream("ressource/image/logout.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView logoutImg = new ImageView(image2);
        logoutImg.setFitHeight(20);
        logoutImg.setFitWidth(20);
        logoutButton.setGraphic(logoutImg);
        //logoutButton.setTextFill(Color.WHITE);
        logoutButton.setOnAction(event -> {
            logoutMethod(userRMI,event);
        });
        logoutButton.setStyle("-fx-background-color: #FF0F0F");
        leftTopHbox.getChildren().add(logoutButton);
        leftTopHbox.setAlignment(Pos.TOP_RIGHT);
        leftTopHbox.setPadding(new Insets(20,0,20,100));

        HBox topHbox = new HBox();
        topHbox.setSpacing(20);
        topHbox.setPadding(new Insets(5,50,30,0));
        topHbox.getChildren().addAll(accueilStudent,stdNameHbox,leftTopHbox);
        //topHbox.setStyle("-fx-background-color: rgb(234, 202, 83, 1)");
        // label du prof

        HBox moduleHbox = new HBox();
        Label moduleListLabel = new Label("Choix Module: ");
        moduleListLabel.setAlignment(Pos.BOTTOM_CENTER);
        ComboBox listModulBox = new ComboBox<>();
        listModulBox.setPrefSize(200, 20);
        for (Module m : modules) {
            listModulBox.getItems().add(m.getLibelle());
        }
        moduleHbox.getChildren().addAll(moduleListLabel, listModulBox);
        moduleHbox.setPadding(new Insets(-15, 70, 10, 250));

        listModulBox.setOnAction(event -> {
            String value =(String)listModulBox.getValue();
            currentModule = ModuleDao.getModuleByName(value);
            profModule = UserDao.getUserById(currentModule.getTeacher_id());
        });
        topVBox.setSpacing(20);
        topVBox.getChildren().addAll(topHbox, moduleHbox);
        topVBox.setStyle("-fx-background-color: rgb(234, 202, 83, 1)");

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
        //**************************************************Forum ****************************/
        Button forumButton = new Button("Forum");
        forumButton.setPrefSize(150,20);

        forumButton.setOnAction(event -> {
            BorderPane pane = new ForumUI().createConversationBorderPane(currentUser, classe_user.getId(),classe_user.getSubject(),borderPane,centerScrollPane);
            borderPane.setCenter(pane);
        });

        VBox mainModuleVbox = new VBox();
        //borderPane.setCenter(mainModuleVbox);

        ScrollPane pred = new ScrollPane();

        coursButton.setOnAction(event -> {

            // traitement de la partie borderPane.setCenter
            VBox centerVbox = new VBox();
           // centerVbox.getChildren().addAll(displayRessource(currentModule.getId(),primaryStage));

            if(currentModule != null){
                centerVbox.getChildren().removeAll();
                try{
                    pred.setContent(displayRessource(currentModule.getId(),primaryStage));
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

           BorderPane pane =  createConversationBorderPane(userRMI,userReceiver,centerScrollPane,borderPane);
           pane.setPadding(new Insets(0,15,0,0));
           borderPane.setCenter(pane);
        });

        profButton.setOnAction(event -> {
            User u = UserDao.getUserById(currentModule.getTeacher_id());
            if(u != null){
                BorderPane pane = createConversationBorderPane(userRMI,u,centerScrollPane,borderPane);
                borderPane.setCenter(pane);
            }
            // code de la méssagerie du professeur
        });

        whiteBoardButton.setOnAction(event -> {
            // code pour afficher le tableau blanc
            try {
                if(currentModule != null){
                    borderPane.setCenter(new WhiteBoard(currentModule.getId()).start(primaryStage,borderPane,pred));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        logoutButton.setOnAction(event -> {
        });

        ModuleRessourceVbox.getChildren().addAll(coursButton, whiteBoardButton, messagerie,forumButton ,profButton);
        ModuleRessourceVbox.setSpacing(20);
        ModuleRessourceVbox.setPadding(new Insets(50, 15, 0, 15));
        ModuleRessourceVbox.setStyle("-fx-background-color: lightgray");
        borderPane.setLeft(ModuleRessourceVbox);
        borderPane.setTop(topVBox);
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
        leftLabel.setFont(Font.font("bold",17));
        Label rightLabel = new Label(message);
        rightLabel.setTextFill(Color.WHITE);
        rightLabel.setFont(Font.font("bold",15));
        HBox leftHBox = new HBox();
        HBox rightHBox = new HBox();
        rightHBox.getChildren().add(rightLabel);
        rightLabel.setStyle("-fx-background-color: #2caf19");
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

    public void getWhiteBoardUpdate(){

    }
    public BorderPane createConversationBorderPane(UserRMI user, User receiverInfo,ScrollPane pred,BorderPane main){
        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox();
        String receiverName = receiverInfo.getNom();
        Label receiverNameLabel = new Label(receiverName);
        receiverNameLabel.setFont(Font.font("bold",30));
        receiverNameLabel.setAlignment(Pos.TOP_CENTER);


        Button backButton = new Button();
        backButton.setAlignment(Pos.TOP_LEFT);
        Image backImg = null;
        try {
            backImg = new Image(new FileInputStream("ressource/image/iconRetour.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView backImgView = new ImageView(backImg);
        backImgView.setFitHeight(30);
        backImgView.setFitWidth(30);
        backButton.setGraphic(backImgView);
        //backButton.setPadding(new Insets(10,150,10,5));
        backButton.setAlignment(Pos.TOP_LEFT);
        backButton.setOnAction(event -> {
            main.setCenter(pred);
        });
        HBox buttonBack = new HBox();
        buttonBack.getChildren().add(backButton);
        buttonBack.setAlignment(Pos.TOP_LEFT);
        buttonBack.setPadding(new Insets(0,80,0,5));


        hBox.getChildren().addAll(backButton,receiverNameLabel);
        hBox.setSpacing(80);
        // ce qui est ajouté de suite ....
        hBox.setPadding(new Insets(10,0,0,10));
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
            // leftHBox.setPadding(new Insets());
            Label leftLabel = new Label(message.getContent() + "\n" + message.getDate());
            leftLabel.setFont(Font.font("bold",15));
            Label rightLabel = new Label(message.getContent() + "\n" + message.getDate());
            rightLabel.setFont(Font.font("bold",15));
            if (message.getSender_id()==receiverInfo.getId()){
                //Message have to be displayed at left
                leftHBox.getChildren().add(leftLabel);
                leftHBox.setStyle("-fx-background-color: #89849C");
                leftMessageDisplay.getChildren().add(leftHBox);
                rightLabel.setVisible(false);
                rightHBox.getChildren().add(rightLabel);
                rightMessageDisplay.getChildren().add(rightHBox);
            }else {
                //Message have to be displayed at right
                rightHBox.getChildren().add(rightLabel);
                rightLabel.setStyle("-fx-background-color: #2caf19");
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
        messageField.setPrefWidth(550);
        messageField.setPrefHeight(30);
        messageField.setBorder(Border.EMPTY);

        Button sendButton = new Button();
        Image sendImg = null;
        try {
            sendImg = new Image(new FileInputStream("ressource/image/send.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView sendImgView = new ImageView(sendImg);
        sendImgView.setFitHeight(30);
        sendImgView.setFitWidth(30);
        sendButton.setGraphic(sendImgView);
        sendButton.setPrefSize(30,30);
        //sendButton.setTextFill(Color.WHITE);
        //sendButton.setStyle("-fx-background-color: green");
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
    public void logoutMethod(UserRMI u, Event event){
        try{
            if (sessionUser.logout(u.getUser(u.getEmail()))){
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            }
        }catch (Exception e){
            System.out.println("Failed to delete user session !!!");
        }
    }
}