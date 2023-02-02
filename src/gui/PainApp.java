package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.text.html.ImageView;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

//import java.awt.*;

public class PainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root,800,600);

        Canvas canvas = new Canvas();
        canvas.setHeight(400);
        canvas.setWidth(500);

        root.setCenter(canvas);

        root.setTop(getHeaderElement(canvas));





        primaryStage.setScene(scene);
        primaryStage.show();

    }
    public VBox getHeaderElement(Canvas canvas){
        VBox vBox = new VBox();
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Files");
        MenuItem menuItem1 = new MenuItem("save");
        menuItem1.setOnAction(event -> {
            onSave(canvas);
        });
        MenuItem menuItem2 = new MenuItem("exit");
        menuItem2.setOnAction(event -> {
            onExit();
        });
        menu.getItems().addAll(menuItem1,menuItem2);
        menuBar.getMenus().add(menu);


        ToolBar toolBar = new ToolBar();
        HBox hBox = new HBox();
        TextField brushSize = new TextField("20");
        ColorPicker colorPicker = new ColorPicker();
        CheckBox eraser = new CheckBox("eraser");
        CheckBox shareWhiteboard = new CheckBox("share whiteboard");
        shareWhiteboard.setAlignment(Pos.TOP_RIGHT);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(5);
        hBox.getChildren().addAll(brushSize,colorPicker,eraser);
        toolBar.getItems().addAll(hBox,shareWhiteboard);

        vBox.getChildren().addAll(menuBar,toolBar);

        //Using Timer and TimerTask
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                onSave(canvas);
            }
        };

        //Canvas events
        GraphicsContext g = canvas.getGraphicsContext2D();
        canvas.setOnMouseDragged(event -> {
            double size = Double.parseDouble(brushSize.getText());
            double x = event.getX() -size/2;
            double y = event.getY() -size/2;
            if (eraser.isSelected()){
                g.clearRect(x,y,size,size);
            }else {
                g.setFill(colorPicker.getValue());
                g.fillRect(x,y,size,size);
            }
        });
        canvas.setOnMouseDragReleased(event -> {
            /*Timer timer = new Timer();
            timer.schedule(timerTask,2*1000);

             */
            onSave(canvas);
        });


        return vBox;
    }

    public void onSave(Canvas canvas){
        try{
            Image snapshot = canvas.snapshot(null,null);
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot,null),"png",
                    new File("paint.png"));
        }catch (Exception e){
            System.out.println("Failed to save !!!");
        }
    }
    public void onExit(){
        Platform.exit();
    }
    public static void main(String[] args) {
        launch(args);
    }
}