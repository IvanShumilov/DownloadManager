package pagesUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;

public class ShowListPage {

    private List<String> mListLink;
    private Stage mPrimaryStage;

    public ShowListPage(List<String> list, Stage primaryStage) {
        this.mListLink = list;
        this.mPrimaryStage = primaryStage;
    }

    public void start() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        TextArea listLinkLbl = new TextArea(getPrettyList(mListLink));
        listLinkLbl.setScrollTop(5.0);
        grid.add(listLinkLbl, 0, 1);

        Button closeBtn = new Button("Back");
        closeBtn.setAlignment(Pos.BOTTOM_LEFT);
        grid.add(closeBtn, 0, 0);



        Scene scene = new Scene(grid, 400, 275);
        Stage secondStage = new Stage();
        secondStage.setScene(scene); // set the scene
        secondStage.setTitle("Working ...");
        secondStage.show();
        mPrimaryStage.close(); // close the first stage (Window)

        closeBtn.setOnAction(e->{
            secondStage.close();
            mPrimaryStage.show();
        } );
    }
    private String getPrettyList(List<String> list){
        StringBuilder result = new StringBuilder();

        for(String i : list){
            result.append(i).append("\n");
        }
        return result.toString();
    }
}
