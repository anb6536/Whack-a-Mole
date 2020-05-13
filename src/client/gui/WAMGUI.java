package client.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.List;


public class WAMGUI extends Application implements Observer<WAMBoard>{

//    public static final int ROWS = 5;
//    public static final int COLS = 5;
    private int ROWS;
    private int COLS;
    private int clickCount = 0;
    private WAMBoard board;
    private WAMClient client;
    private GridPane gridPane;
    Image mole = new Image(getClass().getResourceAsStream("mole.png"));
    Image empty = new Image(getClass().getResourceAsStream("empty.png"));
    private Button[][] buttons;
    private Label playerScore;
    private Label gameStatus;
    private VBox score;
    private VBox status;
    private HBox hbox;

    public void init()
    {
        List<String> args = getParameters().getRaw();

        // get host info and port from command line
        String hostName = args.get(0);
        int port = Integer.parseInt(args.get(1));
        this.gridPane = new GridPane();
        this.board = new WAMBoard();
        try
        {
            Socket socket = new Socket(hostName, port);
            this.client = new WAMClient(socket, this.board);
            this.ROWS = client.getROWS();
            this.COLS = client.getCOLS();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        this.board.addObserver(this);
        buttons = new Button[ROWS][COLS];
        this.playerScore = new Label();
        this.gameStatus = new Label();
        this.playerScore.setText(Integer.toString(client.getScore()));
        this.gameStatus.setText(board.getStatus().toString());
        this.hbox = new HBox();
        this.score = new VBox();
        this.status = new VBox();
        this.gameStatus.setFont(new Font("Ariel", 25));
        this.playerScore.setFont(new Font("Ariel", 25));
    }

    public Button moleDown( int mole )
    {
        Button button = new Button();
        button.setGraphic(new ImageView(empty));
        button.setOnAction(E ->
                        client.sendWHACK(this.client.getPlayerNumber(), mole*this.COLS)
                );
        return button;
    }

    public Button moleUp ( int mole )
    {
        Button button = new Button();
        button.setGraphic(new ImageView(this.mole));
        button.setOnAction(E ->
                client.sendWHACK(this.client.getPlayerNumber(), mole*this.COLS)
        );
        return button;
    }

    public void start(Stage stage){
        //add in ability to drag window around and keep ratio

        client.startListener();
        BorderPane bane = new BorderPane();
        System.out.println(this.ROWS + " " + this.COLS);
        for (int row = 0; row < this.ROWS; row++) {
            for (int col = 0; col < this.COLS; col++) {
                gridPane.add(moleDown(row*this.COLS), col, row);
            }
        }

        this.playerScore.setText(Integer.toString(client.getScore()));
        this.score.getChildren().add(playerScore);
        this.status.getChildren().add(gameStatus);
        hbox.setSpacing(70);
        hbox.getChildren().add(this.status);
        hbox.getChildren().add(this.score);
        bane.setBottom(hbox);
        bane.setCenter(gridPane);

        stage.setScene(new Scene(bane));
        stage.setMaximized(true);
        stage.show();
    }

    @Override
    public void update(WAMBoard board) {
        if (Platform.isFxApplicationThread()) {
            this.refresh();
        } else {
            Platform.runLater(() -> this.refresh());
        }
    }

    public void refresh()
    {
        this.playerScore.setText(Integer.toString(client.getScore()));
        Status status = board.getStatus();
        switch (status) {
            case ERROR:
                this.gameStatus.setText(status.toString());
                break;
            case WON:
                this.gameStatus.setText("You Win!");
                break;
            case LOST:
                this.gameStatus.setText("You Lost!");
                break;
            case TIE:
                this.gameStatus.setText("Game Tie!");
                break;
            case IN_PROGRESS:
                this.gameStatus.setText("In Progress");
                break;

        }

        for (int i=0; i<client.getROWS(); i++)
        {
            for (int j=0; j<client.getCOLS(); j++)
            {
                if( this.board.getMole(i, j).isUp() )
                {
                    gridPane.add(moleUp(i*this.COLS), j, i);
                }
                else if( this.board.getMole(i, j).isUp() == false)
                {
                    gridPane.add(moleDown(i*this.COLS), j, i);
                }
            }
        }
    }

    public static void main(String[] args){
        Application.launch(args);
    }


}
