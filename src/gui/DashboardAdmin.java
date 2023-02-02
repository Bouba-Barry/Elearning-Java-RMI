package gui;

import client.ClientRMI;
import dao.ClasseDao;
import dao.ModuleDao;
import dao.UserDao;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import metier.Classe;
import metier.Module;
import metier.User;
import service.UserRMI;
import service.UserRemote;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DashboardAdmin {
    protected User currentUser;
    protected UserRemote sessionUser;
    protected UserRMI userRMI;
    private Classe current_classe;
    private List<Classe> classeList;
    public DashboardAdmin(){}
    public Scene startMethode(Stage primaryStage, String email, ClientRMI clientRMI) throws Exception {

        this.userRMI = clientRMI.getUserRMI();
        sessionUser = clientRMI.getUserSession();
        try{
            currentUser = userRMI.getUser(email);
        }catch (Exception e) {e.printStackTrace();}
        classeList = ClasseDao.getAllClasse();

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root,800,600);

        /**  header **/
        BorderPane headerPane = new BorderPane();
        HBox accueilAdmin = new HBox();
        Label welcomeLabel = new Label("Dashboard Admin");
        welcomeLabel.setFont(Font.font("bold",20));
        welcomeLabel.setTextFill(Color.WHITE);
        //welcomeLabel.setPadding(new Insets(10));
        accueilAdmin.getChildren().addAll(welcomeLabel);
        accueilAdmin.setPadding(new Insets(20,100,20,10));
        accueilAdmin.setAlignment(Pos.TOP_LEFT);

        HBox profNameHbox = new HBox();
        Label label = new Label("Mr "+currentUser.getNom());
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("bold",20));
        Image image = null;
        try {
            image = new Image(new FileInputStream("ressource/image/profile.jpg"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        profNameHbox.getChildren().addAll(imageView,label);
        profNameHbox.setPadding(new Insets(20,100,20,5));
        profNameHbox.setAlignment(Pos.TOP_CENTER);

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
        //logoutButton.setStyle("-fx-background-color: #FF0F0F");
        leftTopHbox.getChildren().add(logoutButton);
        leftTopHbox.setAlignment(Pos.TOP_RIGHT);
        leftTopHbox.setPadding(new Insets(20,0,20,100));

        HBox topHbox = new HBox();
        topHbox.setSpacing(20);
        topHbox.setPadding(new Insets(5,50,30,0));
        topHbox.getChildren().addAll(accueilAdmin,profNameHbox,leftTopHbox);
        topHbox.setStyle("-fx-background-color: rgb(72, 113, 23, 1)");

        /************* Traitement de La partie panel left de l'admin ************************/
        ScrollPane scrollPaneRightPanel = new ScrollPane();
        /* * ********** traitement du leftPanel ***********************/
        VBox leftPanel = new VBox();
        HBox conversationHbox = new HBox();

        // part ajout de User;
        TitledPane titledPaneConv = new TitledPane();
        titledPaneConv.setPrefSize(200,50);
        titledPaneConv.setText("Ajouter User");
        titledPaneConv.setExpanded(false);
        VBox vBoxTmp = new VBox();
        Button studentBut = new Button("student");

        studentBut.setOnAction(event -> {
            BorderPane pane = createUserComponent(root,"student",scrollPaneRightPanel);
            root.setCenter(pane);
        });
        Button teacherBut = new Button("teacher");
        teacherBut.setOnAction(event -> {
            BorderPane pane = createUserComponent(root,"teacher",scrollPaneRightPanel);
            root.setCenter(pane);
        });
        Button adminBut = new Button("admin");
        adminBut.setOnAction(event -> {
            BorderPane pane = createUserComponent(root,"admin",scrollPaneRightPanel);
            root.setCenter(pane);
        });

        studentBut.setPrefSize(100,20);
        teacherBut.setPrefSize(100,20);
        adminBut.setPrefSize(100,20);

        vBoxTmp.setSpacing(20);
        vBoxTmp.getChildren().addAll(studentBut,teacherBut,adminBut);
        titledPaneConv.setContent(vBoxTmp);
        conversationHbox.getChildren().addAll(titledPaneConv);
        //part of logout

        HBox moduleHbox = new HBox();

        Button addClasse = new Button("New Classe");
        addClasse.setOnAction(event -> {
            BorderPane pane = createClasseComponent(root,"Nouvelle Classe",scrollPaneRightPanel);
            root.setCenter(pane);
        });
        addClasse.setPrefSize(200,20);
        Button addModule = new Button("New Module");
        addModule.setOnAction(event -> {
            BorderPane pane = createModuleComponent(root,"Nouveau Module",scrollPaneRightPanel);
            root.setCenter(pane);
        });
        addModule.setPrefSize(200,20);

        leftPanel.getChildren().addAll(conversationHbox,addModule, addClasse);
        leftPanel.setSpacing(20);
        leftPanel.setPadding(new Insets(20,5,0,0));
        leftPanel.setStyle("-fx-background-color: lightgray");


        /** *********** Traitement de La partie panel right de l'admin ********************** **/
        /*************************  right Panel ************************************  ***/
        VBox rightPanel = new VBox();

        // classe No 1: -------------------------------------------------------------

        // classe No 2: -------------------------------------------------------------
        for(Classe c :classeList){
            VBox module2Vbox = new VBox();
            HBox module2Hbox = new HBox();
            // Button classeButton2 = new Button();
            Image Imgclasse2 = null;
            try {
                Imgclasse2 = new Image(new FileInputStream("ressource/image/imgClasse1.jpg"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            ImageView classeImgView2 = new ImageView(Imgclasse2);
            classeImgView2.setFitHeight(60);
            classeImgView2.setFitWidth(70);
            //classeButton2.setGraphic(classeImgView2);
            //classeButton2.setPadding(new Insets(20,5,0,5));

            Label module2Label = new Label(c.getSubject()+"\n"+c.getDesc()+"\n Depuis le "+c.getDate_creation());
            //Label create_atLabel = new Label("2022/12/01/ 1h:34:00 ");
            module2Label.setPadding(new Insets(5,10,20,5));
            module2Hbox.getChildren().addAll(classeImgView2,module2Label);
            module2Hbox.setAlignment(Pos.TOP_LEFT);

            HBox gerezHbox2 = new HBox();
            Button module2Button = new Button("Gerez");
            gerezHbox2.getChildren().add(module2Button);
            gerezHbox2.setAlignment(Pos.BOTTOM_RIGHT);
            gerezHbox2.setPadding(new Insets(5,20,5,100));

            module2Button.setOnAction(event -> {
                BorderPane pane = createClasseGestionComponent(root,c,scrollPaneRightPanel);
                root.setCenter(pane);
            });
            module2Vbox.getChildren().addAll(module2Hbox,gerezHbox2);
            module2Vbox.setStyle("-fx-background-color: rgba(255,255,255,0.87)");
            module2Vbox.setSpacing(10);
            module2Vbox.setAlignment(Pos.BASELINE_CENTER);
            module2Vbox.setPrefSize(200,80);
            rightPanel.getChildren().add(module2Vbox);
        }
        //*-------------------------classe 3 ----------------------------//

        //***************************sfqsflmsfljmlfmqsfdlm///:::::::::::::::::://
        rightPanel.setSpacing(30);
        rightPanel.setPadding(new Insets(15,20,0,20));
        rightPanel.setPrefWidth(600);
        scrollPaneRightPanel.setBorder(Border.EMPTY);
        scrollPaneRightPanel.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPaneRightPanel.setContent(rightPanel);


        /** ------Border pane root ajout des éléments dans le component **/
        root.setTop(topHbox);
        root.setCenter(scrollPaneRightPanel);
        root.setLeft(leftPanel);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Menu Admin");
        primaryStage.setResizable(false);
        primaryStage.show();

        return scene;
    }

    public BorderPane createClasseGestionComponent(BorderPane main,Classe classe, ScrollPane pred){
        BorderPane borderPane = new BorderPane();
        /** ------------la partie top du border ----------------------------- **/
        VBox topVbox = new VBox();
        HBox topLabHbox = new HBox();

        Label moduleNameLabel = new Label(classe.getSubject());
        moduleNameLabel.setAlignment(Pos.BASELINE_CENTER);
        moduleNameLabel.setFont(new Font(20));

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
        backButton.setPadding(new Insets(20,5,0,5));

        /** button create a new classe *** /
         *
         */


        Button removeButton = new Button();
        Image removeImg = null;
        try {
            removeImg = new Image(new FileInputStream("ressource/image/delete.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView removeImgView = new ImageView(removeImg);
        removeImgView.setFitHeight(30);
        removeImgView.setFitWidth(30);
        removeButton.setGraphic(removeImgView);
        removeButton.setPadding(new Insets(20,5,0,5));
        removeButton.setAlignment(Pos.TOP_RIGHT);


        topLabHbox.getChildren().addAll(backButton,moduleNameLabel,removeButton);
        topLabHbox.setSpacing(125);
        topLabHbox.setPadding(new Insets(5,5,10,5));
        // fin de traitement du top .............



        topVbox.setSpacing(10);
        topVbox.getChildren().addAll(topLabHbox);
        topVbox.setPadding(new Insets(10,10,8,10));
        Border border = new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        topVbox.setBorder(border);
        topVbox.setStyle("-fx-background-color: lightgray ");
        //Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        //topVbox.setBorder(border);

        /** -------------la partie center du borderPane --------------------- **/
        HBox centerVbox = new HBox();

        HBox studentHbox = new HBox();
        HBox moduleClasseHbox = new HBox();

        TitledPane modulePane = new TitledPane();
        List<Module> modules = ModuleDao.getAllModuleByClasse(classe.getId());
        modulePane.setText("Liste des Modules enseignés");
        VBox moduleVbox = new VBox();
        for(Module m : modules){
            HBox buttonHbox = new HBox();
            buttonHbox.setSpacing(10);
            Button mdButton = new Button(m.getLibelle());
            Button remove = new Button("X");
            remove.setStyle("-fx-background: #ff00");
            buttonHbox.getChildren().addAll(mdButton,remove);
            remove.setOnAction(event -> {
                if(ModuleDao.updateModuleClasseToNull(m.getId())){
                    System.out.println(" ModuleSupprimer");
                }else{
                    System.out.println("error ");
                }
            });
            mdButton.setPrefSize(200,20);
            remove.setPrefSize(50,20);
            moduleVbox.getChildren().add(buttonHbox);
        }
        TitledPane newModPane = new TitledPane();
        newModPane.setExpanded(false);
        newModPane.setText("Ajouter Module");
        VBox addVboxMd = new VBox();
        ComboBox<String> ModuleCombo = new ComboBox<>();
        ModuleCombo.setPrefSize(200,20);
        List<Module> modulesNew = ModuleDao.getModulesWithNullClasseId();
        for(Module m: modulesNew){
            ModuleCombo.getItems().addAll(m.getLibelle());
        }
        Button addNewModule = new Button("ajouter");
        addNewModule.setOnAction(event -> {
            // ajout d'un module dans une classe ....
        });
        addVboxMd.getChildren().addAll(ModuleCombo,addNewModule);
        addVboxMd.setSpacing(10);

        newModPane.setContent(addVboxMd);

        moduleVbox.getChildren().add(newModPane);
        moduleVbox.setSpacing(10);
        modulePane.setContent(moduleVbox);
        modulePane.setExpanded(false);

        TitledPane studentPane = new TitledPane();

        studentPane.setText("Liste des Etudiants de La classe");
        VBox studentVbox = new VBox();
        studentVbox.setSpacing(10);
        List<User> studentClasse = UserDao.getAllStudentByClasse(classe.getId());
        for(User u : studentClasse){
            HBox buttonHbox = new HBox();
            buttonHbox.setSpacing(10);
            Button stdButton = new Button(u.getNom());
            Button remove = new Button("X");
            remove.setStyle("-fx-background: #ff00");
            buttonHbox.getChildren().addAll(stdButton,remove);
            remove.setOnAction(event -> {
                if(UserDao.updateUserClasseToNull(u.getId())){
                    System.out.println("user enlevé de la classe ");
                }else{
                    System.out.println("error ");
                }
            });
            stdButton.setPrefSize(200,20);
            remove.setPrefSize(50,20);
            studentVbox.getChildren().add(buttonHbox);
        }
        TitledPane newStudPane = new TitledPane();
        newStudPane.setExpanded(false);
        newStudPane.setText("Ajouter Etudiant");
        VBox addVboxStd = new VBox();
        List<User> studentNew = UserDao.getStudentsWithNullClasseId();
        ComboBox<String> studentCombo = new ComboBox<>();
        studentCombo.setPrefSize(200,20);
        for(User u: studentNew){
            studentCombo.getItems().addAll(u.getEmail());
        }
        User newUser ;
        AtomicReference<String> name = null;
        studentCombo.setOnAction(event -> {
            String studentSelected = studentCombo.getValue();
            if(! studentSelected.isEmpty()){
                name.set(studentSelected);
            }
        });
        newUser = UserDao.getUserByName(name.get().toString());
        Button addNewStudent = new Button("ajouter");
        addNewStudent.setOnAction(event -> {
            try {
                if(userRMI.addUserToClasse(newUser.getId(),classe.getId())){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Retour de L'ajout ");
                    alert.setHeaderText(null);
                    alert.setContentText("L'etudiant est ajouté avec Success ! ");
                    alert.showAndWait();
                }else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Retour de L'ajout ");
                    alert.setHeaderText(null);
                    alert.setContentText("Error lors de l'ajout ! Veuillez Réessayer");
                    alert.showAndWait();
                }

            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        addVboxStd.getChildren().addAll(studentCombo,addNewStudent);
        addVboxStd.setSpacing(10);

        newStudPane.setContent(addVboxStd);
        studentVbox.getChildren().add(newStudPane);
        studentVbox.setSpacing(10);

        studentPane.setContent(studentVbox);
        studentPane.setExpanded(false);

        // ce VBox quand il clique sur l'ajout de new student
       /* VBox newVboxStud = new VBox();
        newVboxStud.getChildren().add(addNewStudent());

        // ce VBox quand il clique sur l'ajout de new Module
        VBox newVboxMod = new VBox();
        newVboxMod.getChildren().add(addNewModule());*/

        moduleClasseHbox.getChildren().add(modulePane);
        studentHbox.getChildren().add(studentPane);
        centerVbox.setStyle("-fx-background-color: rgba(255,255,255,0.87)");
        centerVbox.getChildren().addAll(studentHbox,moduleClasseHbox);
        centerVbox.setSpacing(30);
        centerVbox.setPadding(new Insets(15,20,0,20));
        centerVbox.setPrefWidth(600);
        ScrollPane scrollPaneRightPanel = new ScrollPane(centerVbox);
        scrollPaneRightPanel.setBorder(Border.EMPTY);
        scrollPaneRightPanel.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        backButton.setOnAction(event -> {
            //stagePrincipale.setScene(scenePred);
            main.setCenter(pred);
        });

        borderPane.setTop(topVbox);
        borderPane.setCenter(scrollPaneRightPanel);
        //borderPane.setBottom(newVbox);
        return borderPane;
    }

    public VBox addNewStudent(UserRMI userRMI){
        VBox addStudentVBox = new VBox();
        GridPane grid = new GridPane();

        Label lblTitle = new Label("Entrez Les informations du User");

        Label lblEmail = new Label("Email: ");
        lblEmail.setPrefHeight(20);
        TextField tfEmail = new TextField();
        tfEmail.setPrefSize(300,20);

        Label lablPassword = new Label("Default Password: ");
        lablPassword.setPrefHeight(20);
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefSize(300,20);

        Label lablClasse = new Label("Default Classe: ");
        lablClasse.setPrefHeight(20);
        ComboBox<String> classeCombo = new ComboBox<>();
        List<Classe> listClasse = ClasseDao.getAllClasse();
        for(Classe c : listClasse){
            classeCombo.getItems().addAll(c.getSubject());
        }
        classeCombo.setPrefSize(200,20);


        Button saveButton = new Button("ajouter");
        saveButton.setStyle("-fx-background-color: #ffBBCC");

        saveButton.setOnAction(event -> {
            boolean ret = false;
            String classe = (String) classeCombo.getValue();
            String email = tfEmail.getText();
            String password = new String(passwordField.getText());
          if(! email.isEmpty() && !password.isEmpty() ){
              if(classe == null){
                  try {
                     ret = userRMI.addSimpleUser(new User("",email,password,"student"));
                  } catch (RemoteException e) {
                      throw new RuntimeException(e);
                  }
              }else{
                  Classe classe1 = ClasseDao.getClassByName(classe);
                  try {
                     ret =  userRMI.addNewUser(new User(email,password,"student",classe1.getId()));
                  } catch (RemoteException e) {
                      throw new RuntimeException(e);
                  }
              }
          }
            if(ret == true){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Retour de L'ajout ");
                alert.setHeaderText(null);
                alert.setContentText("Votre User est créer avec Success ! ");
                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Retour de L'ajout ");
                alert.setHeaderText(null);
                alert.setContentText("Erreur Lors de L'ajout ! Veuillez Réessayer !");
                alert.showAndWait();
            }
        });

        Label lablError = new Label("Error: ");
        grid.setPadding( new Insets(10,10,5,10) );
        grid.setHgap( 8 );
        grid.setVgap( 8 );

        grid.add( lblTitle,       1, 1);  // empty item at 0,0
        grid.add( lblEmail,       0, 2); grid.add(tfEmail,        1, 2);
        grid.add( lablPassword,    0, 3); grid.add( passwordField,    1, 3);
        grid.add( lablClasse,     0, 4); grid.add( classeCombo,     1, 4);
        grid.add( lablError,     1, 5);
        lablError.setVisible(false);
        HBox hBoxLogButton = new HBox();
        hBoxLogButton.getChildren().add(saveButton);
        hBoxLogButton.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxLogButton.setPadding(new Insets(0,113,70,170));

        addStudentVBox.getChildren().addAll(grid,hBoxLogButton);

        return addStudentVBox;
    }

    public VBox addNewModule(){
        VBox addStudentVBox = new VBox();
        GridPane grid = new GridPane();

        Label lblTitle = new Label("Entrez Les informations Pour le Module");

        Label lblEmail = new Label("Libellé: ");
        lblEmail.setPrefHeight(20);
        TextField tfEmail = new TextField();
        tfEmail.setPrefSize(300,20);

        Label lablPassword = new Label("Description: ");
        lablPassword.setPrefHeight(20);
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefSize(300,20);

        Label lablClasse = new Label("Default Classe: ");
        lablClasse.setPrefHeight(20);
        ComboBox<String> classeCombo = new ComboBox<>();
        classeCombo.getItems().addAll("Irisi", "IFA", "MIPC", "GMP", "EGV");
        classeCombo.setPrefSize(200,20);

        Label lablTeacher = new Label("Default Teacher: ");
        lablClasse.setPrefHeight(20);
        ComboBox<String> teacherCombo = new ComboBox<>();
        teacherCombo.getItems().addAll("Mr A", "Mr B", "Mr C", "Mr E", "Mr D");
        teacherCombo.setPrefSize(200,20);


        Button saveButton = new Button("ajouter");
        saveButton.setStyle("-fx-background-color: #ffBBCC");

        Label lablError = new Label("Error: ");
        grid.setPadding( new Insets(10,10,5,10) );
        grid.setHgap( 8 );
        grid.setVgap( 8 );

        grid.add( lblTitle,       1, 1);  // empty item at 0,0
        grid.add( lblEmail,       0, 2); grid.add(tfEmail,        1, 2);
        grid.add( lablPassword,    0, 3); grid.add( passwordField,    1, 3);
        grid.add( lablClasse,     0, 4); grid.add( classeCombo,     1, 4);
        grid.add(lablTeacher, 0,5); grid.add(teacherCombo,1,5);
        grid.add( lablError,     1, 6);

        lablError.setVisible(false);
        HBox hBoxLogButton = new HBox();
        hBoxLogButton.getChildren().add(saveButton);
        hBoxLogButton.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxLogButton.setPadding(new Insets(0,113,70,170));

        addStudentVBox.getChildren().addAll(grid,hBoxLogButton);

        return addStudentVBox;
    }

    public BorderPane createUserComponent(BorderPane main,String typeUser, ScrollPane pred){
        BorderPane borderPane = new BorderPane();
        /** ------------la partie top du border ----------------------------- **/
        VBox topVbox = new VBox();
        HBox topLabHbox = new HBox();

        Label moduleNameLabel = new Label(typeUser);
        moduleNameLabel.setAlignment(Pos.BASELINE_CENTER);
        moduleNameLabel.setFont(new Font(20));

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
        backButton.setPrefSize(30,30);
        backButton.setGraphic(backImgView);
        backButton.setPadding(new Insets(20,5,0,5));
        //backButton.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        Border border2 = new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.NONE, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        backButton.setBorder(border2);
        /** button create a new classe *** /
         *
         */


        Button removeButton = new Button();
        Image removeImg = null;
        try {
            removeImg = new Image(new FileInputStream("ressource/image/delete.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView removeImgView = new ImageView(removeImg);
        removeImgView.setFitHeight(30);
        removeImgView.setFitWidth(30);
        removeButton.setGraphic(removeImgView);
        removeButton.setPadding(new Insets(20,5,0,5));
        removeButton.setAlignment(Pos.TOP_RIGHT);

        // on enlève aussi le removeButton dans le top
        topLabHbox.getChildren().addAll(backButton,moduleNameLabel);
        topLabHbox.setSpacing(125);
        topLabHbox.setPadding(new Insets(5,5,10,5));
        // fin de traitement du top .............


        topVbox.setSpacing(10);
        topVbox.getChildren().addAll(topLabHbox);
        topVbox.setPadding(new Insets(10,10,8,10));
        Border border = new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        topVbox.setBorder(border);
        topVbox.setStyle("-fx-background-color: lightgray ");
        //Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        //topVbox.setBorder(border);

        /** -------------la partie center du borderPane --------------------- **/
       VBox centerVbox = addNewStudent(userRMI);

        centerVbox.setStyle("-fx-background-color: rgba(255,255,255,0.87)");
       // centerVbox.getChildren().addAll();
        centerVbox.setAlignment(Pos.BOTTOM_CENTER);
        centerVbox.setSpacing(30);
        centerVbox.setPadding(new Insets(80,20,0,20));
        centerVbox.setPrefWidth(600);
        //centerVbox.setPrefHeight(400);

        ScrollPane scrollPaneRightPanel = new ScrollPane(centerVbox);
        scrollPaneRightPanel.setBorder(Border.EMPTY);
        scrollPaneRightPanel.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        backButton.setOnAction(event -> {
            //stagePrincipale.setScene(scenePred);
            main.setCenter(pred);
        });

        borderPane.setTop(topVbox);
        borderPane.setCenter(scrollPaneRightPanel);
        //borderPane.setBottom(newVbox);
        return borderPane;
    }
    public BorderPane createModuleComponent(BorderPane main,String typeUser, ScrollPane pred){
        BorderPane borderPane = new BorderPane();
        /** ------------la partie top du border ----------------------------- **/
        VBox topVbox = new VBox();
        HBox topLabHbox = new HBox();

        Label moduleNameLabel = new Label(typeUser);
        moduleNameLabel.setAlignment(Pos.BASELINE_CENTER);
        moduleNameLabel.setFont(new Font(20));

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
        backButton.setPadding(new Insets(20,5,0,5));

        /** button create a new classe *** /
         *
         */


        Button removeButton = new Button();
        Image removeImg = null;
        try {
            removeImg = new Image(new FileInputStream("ressource/image/delete.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView removeImgView = new ImageView(removeImg);
        removeImgView.setFitHeight(30);
        removeImgView.setFitWidth(30);
        removeButton.setGraphic(removeImgView);
        removeButton.setPadding(new Insets(20,5,0,5));
        removeButton.setAlignment(Pos.TOP_RIGHT);


        topLabHbox.getChildren().addAll(backButton,moduleNameLabel,removeButton);
        topLabHbox.setSpacing(125);
        topLabHbox.setPadding(new Insets(5,5,10,5));
        // fin de traitement du top .............


        topVbox.setSpacing(10);
        topVbox.getChildren().addAll(topLabHbox);
        topVbox.setPadding(new Insets(10,10,8,10));
        Border border = new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        topVbox.setBorder(border);
        topVbox.setStyle("-fx-background-color: lightgray ");
        //Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        //topVbox.setBorder(border);

        /** -------------la partie center du borderPane --------------------- **/
        VBox centerVbox = addNewModule();

        centerVbox.setStyle("-fx-background-color: rgba(255,255,255,0.87)");
        // centerVbox.getChildren().addAll();
        // centerVbox.setSpacing(30);
        centerVbox.setPadding(new Insets(15,20,0,20));
        centerVbox.setPrefWidth(600);
        ScrollPane scrollPaneRightPanel = new ScrollPane(centerVbox);
        scrollPaneRightPanel.setBorder(Border.EMPTY);
        scrollPaneRightPanel.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        backButton.setOnAction(event -> {
            //stagePrincipale.setScene(scenePred);
            main.setCenter(pred);
        });

        borderPane.setTop(topVbox);
        borderPane.setCenter(scrollPaneRightPanel);
        //borderPane.setBottom(newVbox);
        return borderPane;
    }
    public BorderPane createClasseComponent(BorderPane main,String typeUser, ScrollPane pred){
        BorderPane borderPane = new BorderPane();
        /** ------------la partie top du border ----------------------------- **/
        VBox topVbox = new VBox();
        HBox topLabHbox = new HBox();

        Label moduleNameLabel = new Label(typeUser);
        moduleNameLabel.setAlignment(Pos.BASELINE_CENTER);
        moduleNameLabel.setFont(new Font(20));

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
        backButton.setPrefSize(30,30);
        backButton.setGraphic(backImgView);
        backButton.setPadding(new Insets(20,5,0,5));
        //backButton.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        Border border2 = new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.NONE, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        backButton.setBorder(border2);
        /** button create a new classe *** /
         *
         */


        Button removeButton = new Button();
        Image removeImg = null;
        try {
            removeImg = new Image(new FileInputStream("ressource/image/delete.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView removeImgView = new ImageView(removeImg);
        removeImgView.setFitHeight(30);
        removeImgView.setFitWidth(30);
        removeButton.setGraphic(removeImgView);
        removeButton.setPadding(new Insets(20,5,0,5));
        removeButton.setAlignment(Pos.TOP_RIGHT);

        // on enlève aussi le removeButton dans le top
        topLabHbox.getChildren().addAll(backButton,moduleNameLabel);
        topLabHbox.setSpacing(125);
        topLabHbox.setPadding(new Insets(5,5,10,5));
        // fin de traitement du top .............


        topVbox.setSpacing(10);
        topVbox.getChildren().addAll(topLabHbox);
        topVbox.setPadding(new Insets(10,10,8,10));
        Border border = new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        topVbox.setBorder(border);
        topVbox.setStyle("-fx-background-color: lightgray ");
        //Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        //topVbox.setBorder(border);

        /** -------------la partie center du borderPane --------------------- **/
        VBox centerVbox = addNewClasse();

        centerVbox.setStyle("-fx-background-color: rgba(255,255,255,0.87)");
        // centerVbox.getChildren().addAll();
        centerVbox.setAlignment(Pos.BOTTOM_CENTER);
        centerVbox.setSpacing(30);
        centerVbox.setPadding(new Insets(80,20,0,20));
        centerVbox.setPrefWidth(600);
        //centerVbox.setPrefHeight(400);

        ScrollPane scrollPaneRightPanel = new ScrollPane(centerVbox);
        scrollPaneRightPanel.setBorder(Border.EMPTY);
        scrollPaneRightPanel.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        backButton.setOnAction(event -> {
            //stagePrincipale.setScene(scenePred);
            main.setCenter(pred);
        });

        borderPane.setTop(topVbox);
        borderPane.setCenter(scrollPaneRightPanel);
        //borderPane.setBottom(newVbox);
        return borderPane;
    }

    public VBox addNewClasse(){
        VBox addStudentVBox = new VBox();
        GridPane grid = new GridPane();
        Label mainTitle = new Label("Création D'une Nouvelle Classe");
        Label lblTitle = new Label("Entrez Les informations de la classe");

        Label lblEmail = new Label("Nom: ");
        lblEmail.setPrefHeight(20);
        TextField tfEmail = new TextField();
        tfEmail.setPromptText("Ex: Filière A");
        tfEmail.setPrefSize(300,20);

        Label lablPassword = new Label("Description de la classe: ");
        lablPassword.setPrefHeight(20);
        TextField passwordField = new TextField();
        passwordField.setPrefSize(300,20);

        Button saveButton = new Button("ajouter");
        saveButton.setStyle("-fx-background-color: #ffBBCC");

        Label lablError = new Label("Error: ");
        grid.setPadding( new Insets(10,10,5,10) );
        grid.setHgap( 8 );
        grid.setVgap( 8 );

        grid.add(mainTitle,1,1);
        grid.add( lblTitle,       1, 2);  // empty item at 0,0
        grid.add( lblEmail,       0, 3); grid.add(tfEmail,        1, 3);
        grid.add( lablPassword,    0, 4); grid.add( passwordField,    1, 4);
        grid.add( lablError,     1, 5);
        lablError.setVisible(false);
        HBox hBoxLogButton = new HBox();
        hBoxLogButton.getChildren().add(saveButton);
        hBoxLogButton.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxLogButton.setPadding(new Insets(0,113,70,170));

        addStudentVBox.getChildren().addAll(grid,hBoxLogButton);

        // traitement du button
        saveButton.setOnAction(event -> {
            // lui rediriger vers la gestion de la classe
            String classeName = tfEmail.getText();
            String description = passwordField.getText();
            if(! classeName.isEmpty()){
                ClasseDao.add(new Classe(classeName,description));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Retour de L'ajout ");
                alert.setHeaderText(null);
                alert.setContentText("Votre Classe est créer avec Success ! ");
                alert.showAndWait();
                //alert.getDialogPane().setContent();
            }else{
                lablError.setVisible(true);
                //lablError.setStyle("red");
            }
        });

        return addStudentVBox;
    }


}
