package se.kth.alialaa.labb1db;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import se.kth.alialaa.labb1db.model.BooksDbMockImpl;
import se.kth.alialaa.labb1db.view.BooksPane;

/**
 * Application start up.
 *
 * @author anderslm@kth.se
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        BooksDbMockImpl booksDb = new BooksDbMockImpl(); // model

        String dbUrl = "jdbc:mysql://localhost:3306/labb1DB"; // Replace with your database URL
        try {
            booksDb.connect(dbUrl);

        } catch (Exception e) {
            e.printStackTrace();
            // Handle connection error (e.g., show an error message)
        }
        BooksPane root = new BooksPane(booksDb);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Books Database Client");
        // add an exit handler to the stage (X) ?
        primaryStage.setOnCloseRequest(event -> {
            try {
                booksDb.disconnect();
            } catch (Exception e) {}
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
