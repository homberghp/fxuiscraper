package app;

import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author "Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}"
 */
public class CustomerView extends AnchorPane  {

    public CustomerView() {
        init();
    }

    final void init() {
        BorderPane level1 = new BorderPane();
        getChildren().add( level1 );

        level1.setTop( tf( "name", "Piet Puk" ) );
        TextField dob = tf( "dob", "2021-05-12" );
        TextField city = tf( "city", "Venlo" );
        GridPane level2 = new GridPane();
        Label l = new Label( "Text" );
        Label l2 = new Label( "Text" );
        level2.add( dob, 0, 0 );
        level2.add( city, 1, 0 );
        level2.add( l, 3, 0 );
        level1.getChildren().addAll( level2 );
        level1.setBottom( l2 );
        var canvas = new Canvas();
        canvas.setId( "canvas" );
        level1.setLeft( canvas );
    }

    TextField tf( String nameId, String pp ) {
        TextField nameF = new TextField();
        nameF.setId( nameId );
        nameF.setText( pp );
        return nameF;
    }

}
