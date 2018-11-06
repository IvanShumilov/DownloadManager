package pagesUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ConfigPage extends Application {

    private List<String> mListLink = new ArrayList<>();
    private String mRootDirSave;
    private int mCollThread;

    public static GridPane getGridPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        return grid;
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {

        GridPane startGrid = getGridPane();

        TextField saveLinkImageField = new TextField();
        startGrid.add(saveLinkImageField, 0, 0);

        Button saveLinkImageBtn = new Button("Add to list");
        startGrid.add(saveLinkImageBtn, 1, 0);

        TextField setRootDirField = new TextField();
        startGrid.add(setRootDirField, 0, 1);

        Button setRootDirBtn = new Button("Set root directory");
        startGrid.add(setRootDirBtn, 1, 1);

        Button setThreadCollBtn = new Button("Set the number of threads:");
        startGrid.add(setThreadCollBtn, 1, 2);

        TextField setThreadField = new TextField();
        startGrid.add(setThreadField, 0, 2);

        Button nextBtn = new Button("Next");
        Button showListBtn = new Button("Show list link");
        Button closeBtn = new Button("Close");
        HBox startHBBtn = new HBox(10);
        startHBBtn.setAlignment(Pos.BOTTOM_CENTER);
        startHBBtn.getChildren().addAll(nextBtn, showListBtn, closeBtn);
        startGrid.add(startHBBtn, 0, 3, 2, 1);

        Label showMessLbl = new Label();
        showMessLbl.setTextFill(Color.FIREBRICK);
        startGrid.add(showMessLbl, 0, 6, 2, 1);

        Scene scene = new Scene(startGrid, 400, 275);
        primaryStage.setTitle("Download Manager");
        primaryStage.setScene(scene);
        primaryStage.show();


        closeBtn.setOnAction(e -> primaryStage.close());

        showListBtn.setOnAction(a -> {
            ShowListPage showListPage = new ShowListPage(mListLink, primaryStage);
            showListPage.start();
        });

        //showListScene(primaryStage));

        saveLinkImageBtn.setOnAction(a -> {
            if (!saveLinkImageField.getText().isEmpty()) {
                mListLink.add(saveLinkImageField.getText());
                showMessLbl.setText("Success add to list");
            } else {
                showMessLbl.setText("Empty link text");
            }
            saveLinkImageField.clear();
        });

        setRootDirBtn.setOnAction(a -> {
            if (!setRootDirField.getText().isEmpty()) {
                mRootDirSave = setRootDirField.getText();
                setRootDirBtn.setDisable(true);
            } else {
                showMessLbl.setText("Empty dir text");
            }
        });

        setThreadCollBtn.setOnAction(a -> {
            try {
                if (!setThreadField.getText().isEmpty()) {
                    mCollThread = Integer.valueOf(setThreadField.getText());
                    setThreadCollBtn.setDisable(true);
                } else {
                    showMessLbl.setText("Empty thread coll");
                }
            } catch (NumberFormatException e) {
                showMessLbl.setText("Please enter integer format");
            }
        });

        nextBtn.setOnAction(e -> {
            WorkingPage workingPage = new WorkingPage(mListLink, mRootDirSave, mCollThread, primaryStage);
            workingPage.start();
        });

        closeBtn.setOnAction(e -> primaryStage.close());
    }
}
