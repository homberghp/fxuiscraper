package app;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import nl.homberghp.fxuiscraper.FXUIScraper;

/**
 *
 * @author Pieter van den Hombergh {@code Pieter.van.den.Hombergh@gmail.com}
 */
public class CustomerController implements FXUIScraper {
 
    
    @FXML
    AnchorPane customerView= new CustomerView();

    @Override
    public Parent getRoot() {
        return customerView;
    } 
    
}
