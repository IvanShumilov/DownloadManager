package pagesUI;

import back.DownloadFile;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

class WorkingPage {

    private List<String> mListLink;
    private String mRootDirSave;
    private int mCollThread;
    private Stage mPrimaryStage;

    WorkingPage(List<String> mListLink, String mRootDirSave, int mCollThread, Stage primaryStage) {
        this.mListLink = mListLink;
        this.mRootDirSave = mRootDirSave;
        this.mCollThread = mCollThread;
        this.mPrimaryStage = primaryStage;
    }

    void start() {
        GridPane grid = ConfigPage.getGridPane();
        Task task;

        Label labelShowMess = new Label();
        labelShowMess.setTextFill(Color.FIREBRICK);
        grid.add(labelShowMess,0,0,2,1);

        Label showContLink = new Label("Coll of enter links: " + String.valueOf(mListLink.size()));
        Label showContThread = new Label("Coll of thread: " + String.valueOf(mCollThread));
        Label showRootDir = new Label("Root dir: " + mRootDirSave);

        grid.add(showContLink, 0, 1, 2, 1);
        grid.add(showContThread, 0, 2, 2, 1);
        grid.add(showRootDir, 0, 3, 2, 1);

        Button startBtn = new Button("Start");
        Button pauseBtn = new Button("Pause");
        Button resetBtn = new Button("Continue");
        Button closeBtn = new Button("Close");
        Button backBtn = new Button("Back");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_CENTER);
        hbBtn.getChildren().addAll(startBtn, pauseBtn, resetBtn, closeBtn, backBtn);
        grid.add(hbBtn, 0, 5, 2, 1);

        Scene scene = new Scene(grid, 400, 275);
        Stage secondStage = new Stage();
        secondStage.setScene(scene); // set the scene
        secondStage.setTitle("Show lift of link");

        pauseBtn.setDisable(true);
        resetBtn.setDisable(true);

        if(mRootDirSave == null || mCollThread == 0 || mListLink.size() == 0){
            startBtn.setDisable(true);
        }


        DownloadFile df = new DownloadFile(mListLink, mCollThread, mRootDirSave);

        task = new Task() {
            @Override
            protected Object call() {
                int max = 101;
                while(df.getPercent() != 100.0){
                    updateProgress(df.getPercent(), max);
                }
                pauseBtn.setDisable(true);
                resetBtn.setDisable(true);
                return null;
            }
        };


        ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress(0);
        progressBar.progressProperty().bind(task.progressProperty());
        grid.add(progressBar,0,4,2,1);

        secondStage.show();
        mPrimaryStage.close(); // close the first stage (Window)


        startBtn.setOnAction(e-> {
            new Thread(task).start();
            df.startDownload();
            startBtn.setDisable(true);
            resetBtn.setDisable(false);
            pauseBtn.setDisable(false);
            labelShowMess.setText("Start download");
        });


        resetBtn.setOnAction(e-> {
            df.setPause(false);
            pauseBtn.setDisable(false);
            labelShowMess.setText("Start download");
                }
        );

        pauseBtn.setOnAction(e->{
            resetBtn.setDisable(false);
            df.setPause(true);
            labelShowMess.setText("Pause download");
        });

        closeBtn.setOnAction(e-> secondStage.close());

        backBtn.setOnAction(event -> {
            secondStage.close();
            mPrimaryStage.show();
        });
    }
}
