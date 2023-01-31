package gui;

import client.ClientRMI;
import dao.ModuleDao;
import dao.RessourceDao;
import dao.UserDao;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import metier.Classe;
import metier.Module;
import metier.Ressource;
import metier.User;
import service.FileRemote;
import service.UserRMI;
import service.UserRemote;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class DashboardTeacher {
    protected UserRMI userRMI;
    private List<Module> modules;
    private Classe classe_user;
    private Module currentModule = null;
    private User profModule = null;
    protected User currentUser;
    protected UserRemote sessionUser;
    public Scene DashboardTeacher(Stage primaryStage, String email, ClientRMI clientRMI){
        /** ---------Récupération des Informations du User -------------------- **/
        this.userRMI = clientRMI.getUserRMI();
        sessionUser = clientRMI.getUserSession();
        try{
            currentUser = userRMI.getUser(email);
        }catch (Exception e) {e.printStackTrace();}

        // recuperation des informations du users .....
        //classe_user = UserDao.getClassUser(email);
        modules = ModuleDao.getAllModulesByTeacher(currentUser.getId());

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root,800,600);

        VBox leftPanel = new VBox();
        VBox rightPanel = new VBox();

        /* * ********** traitement du leftPanel ***********************/
        HBox conversationHbox = new HBox();
        HBox logoutHbox = new HBox();

        // recuperez la list des etudiants du prof ...

        // part conversation
        TitledPane titledPaneConv = new TitledPane();
        titledPaneConv.setPrefSize(180,50);
        titledPaneConv.setText("Conversation");
        titledPaneConv.setExpanded(false);
        VBox vBoxTmp = new VBox();
        vBoxTmp.getChildren().addAll(new Label("classe1"),new Label("classe2"));
        titledPaneConv.setContent(vBoxTmp);
        conversationHbox.getChildren().addAll(titledPaneConv);

        //part of logout
        Button logoutButton = new Button("logout");
        logoutHbox.getChildren().add(logoutButton);
        logoutButton.setPrefSize(180,10);

        leftPanel.getChildren().addAll(conversationHbox,logoutHbox);
        leftPanel.setSpacing(20);
        leftPanel.setPadding(new Insets(20,5,0,0));
        leftPanel.setStyle("-fx-background-color: lightgray");

        /*************************  right Panel ************************************  ***/
        ScrollPane scrollPaneRightPanel = new ScrollPane();
        for(Module m: modules){
            VBox module1Vbox = new VBox();
            Label module1Label = new Label(m.getLibelle());
            module1Label.setPadding(new Insets(3,150,0,0));
            Button module1Button = new Button("voir plus");
            module1Vbox.getChildren().addAll(module1Label,module1Button);
            module1Vbox.setStyle("-fx-background-color: rgba(255,255,255,0.87)");


            module1Vbox.setSpacing(10);
            module1Vbox.setAlignment(Pos.BASELINE_CENTER);
            module1Vbox.setPrefSize(200,80);
            rightPanel.getChildren().addAll(module1Vbox);

            module1Button.setOnAction(event -> {
                BorderPane pane = moduleComponent(root,m.getLibelle(),m.getId() ,scrollPaneRightPanel,primaryStage);
                root.setCenter(pane);
            });
        }
       // rightPanel.getChildren().addAll(module1Vbox,module2Vbox,module3Vbox);
        rightPanel.setSpacing(30);
        rightPanel.setPadding(new Insets(15,20,0,20));
        rightPanel.setPrefWidth(600);
        //ScrollPane scrollPaneRightPanel = new ScrollPane(rightPanel);
        scrollPaneRightPanel.setContent(rightPanel);
        scrollPaneRightPanel.setBorder(Border.EMPTY);
        //scrollPaneRightPanel.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);



        /** *******traitement du top *********************************/
        // label du prof
        HBox AccueilProf = new HBox();
        Label profName = new Label(""+currentUser.getNom());
        profName.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
        AccueilProf.getChildren().addAll(profName);
        AccueilProf.setPadding(new Insets(20,0,20,40));

        HBox CoursModuleHbox = new HBox();
        Label modLabel = new Label("Vos Modules");
        modLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 40));
        CoursModuleHbox.getChildren().add(modLabel);
        CoursModuleHbox.setAlignment(Pos.BASELINE_CENTER);
        CoursModuleHbox.setPadding(new Insets(20,100,20,300));

        HBox topHbox = new HBox();
        topHbox.setSpacing(20);
        topHbox.setPadding(new Insets(5,50,30,0));
        topHbox.getChildren().addAll(AccueilProf,CoursModuleHbox);
        topHbox.setStyle("-fx-background-color: rgb(234, 202, 83, 1)");

        root.setTop(topHbox);
        root.setLeft(leftPanel);
        root.setCenter(scrollPaneRightPanel);

        /**  ---------------traitement des actions des buttons ---------------------***/


        primaryStage.setTitle("Menu Teacher");
        /* primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show(); */
        return scene;
    }

    public BorderPane moduleComponent(BorderPane main, String moduleName, int module_id,ScrollPane pred,Stage stage){

        List<Ressource> ressources = RessourceDao.getRessourcesByModuleId(module_id);
        BorderPane borderPane = new BorderPane();
        /** ------------la partie top du border ----------------------------- **/
        VBox topVbox = new VBox();
        HBox topLabHbox = new HBox();

        Label moduleNameLabel = new Label(moduleName);
        moduleNameLabel.setAlignment(Pos.BASELINE_CENTER);
        moduleNameLabel.setFont(new Font(20));

        Button backButton = new Button("retour Menu");
        backButton.setAlignment(Pos.TOP_LEFT);

        Button whiteBoardButton = new Button("tableau blanc");
        whiteBoardButton.setAlignment(Pos.TOP_RIGHT);


        topLabHbox.getChildren().addAll(backButton,moduleNameLabel,whiteBoardButton );
        topLabHbox.setSpacing(125);
        topLabHbox.setPadding(new Insets(5,5,10,5));

        HBox topOpeHbox = new HBox();
        TextField descriptionTextFied = new TextField();
        descriptionTextFied.setPromptText("describe file");
        Button uploadButton = new Button("upload");
        //uploadButton.setAlignment(Pos.TOP_RIGHT);
        uploadButton.setOnAction(event -> {
            String desc = descriptionTextFied.getText();
            uploadFile(desc,module_id, currentUser.getNom(),stage);
        });

        topOpeHbox.getChildren().addAll(descriptionTextFied,uploadButton);
        topOpeHbox.setSpacing(5);
        topOpeHbox.setAlignment(Pos.BASELINE_CENTER);

        topVbox.setSpacing(10);
        topVbox.getChildren().addAll(topLabHbox,topOpeHbox);
        topVbox.setPadding(new Insets(10,10,8,10));
        Border border = new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        topVbox.setBorder(border);
        topVbox.setStyle("-fx-background-color: lightgray ");
        //Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        //topVbox.setBorder(border);

        /** -------------la partie center du borderPane --------------------- **/
        VBox centerVbox = new VBox();
        Label ressourceTitle = new Label("Ressource Uploader");
        ressourceTitle.setAlignment(Pos.BASELINE_CENTER);
        centerVbox.getChildren().add(ressourceTitle);

        for(Ressource r : ressources){
            VBox module1Vbox = new VBox();
            Label module1Label = new Label(r.getRessource_name());
            module1Label.setPadding(new Insets(3,150,0,0));
            Button module1Button = new Button("remove");
            module1Vbox.getChildren().addAll(module1Label,module1Button);
            module1Vbox.setStyle("-fx-background-color: rgba(255,255,255,0.87)");
            module1Vbox.setSpacing(10);
            module1Vbox.setAlignment(Pos.BASELINE_CENTER);
            module1Vbox.setPrefSize(200,80);
            centerVbox.getChildren().add(module1Vbox);

        }

        centerVbox.setSpacing(30);
        centerVbox.setPadding(new Insets(15,20,0,20));
        centerVbox.setPrefWidth(600);
        ScrollPane scrollPaneRightPanel = new ScrollPane(centerVbox);
        scrollPaneRightPanel.setBorder(Border.EMPTY);

        backButton.setOnAction(event -> {
            //stagePrincipale.setScene(scenePred);
            main.setCenter(pred);
        });

        borderPane.setTop(topVbox);
        borderPane.setCenter(scrollPaneRightPanel);
        return borderPane;
    }

    public void uploadFile(String desc,int module_id,String addBy ,Stage stage){
        try {
            FileRemote fileRemote = (FileRemote)  Naming.lookup("rmi://localhost:1099/FileService");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Upload File");
            //fileChooser.setInitialFileName();
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                String extension = getExtension(file.getName());
                if(extension.equals("txt")){
                   if(fileRemote.uploadTxtFile(file.getName(),desc,module_id,addBy,file)){
                       Alert alert = new Alert(Alert.AlertType.INFORMATION);
                       alert.setTitle("Retour de L'upload ");
                       alert.setHeaderText(null);
                       alert.setContentText("Votre Fichier est Uploader Avec Success");
                       alert.showAndWait();
                   }
                   else{
                       Alert alert = new Alert(Alert.AlertType.INFORMATION);
                       alert.setTitle("Retour de L'upload ");
                       alert.setHeaderText(null);
                       alert.setContentText("Echec D'upload Du Fichier ! Réessayez SVP");
                       alert.showAndWait();
                   }
                }else{
                    if(fileRemote.uploadBinaryFile(file.getName(),desc,module_id,addBy,file)){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Retour de L'upload ");
                        alert.setHeaderText(null);
                        alert.setContentText("Votre Fichier est Uploader Avec Success");
                        alert.showAndWait();
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Retour de L'upload ");
                        alert.setHeaderText(null);
                        alert.setContentText("Echec D'upload Du Fichier ! Réessayez SVP");
                        alert.showAndWait();
                    }
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public String getExtension(String fileName){
        String extension = "";
        int index = fileName.lastIndexOf(".");
        if(index > 0){
            extension = fileName.substring(index+1);
        }
        return extension;
    }

}
