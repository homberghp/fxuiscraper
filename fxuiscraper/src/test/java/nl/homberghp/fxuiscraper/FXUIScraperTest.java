package nl.homberghp.fxuiscraper;

import app.CustomerController;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

/**
 *
 * @author "Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}"
 */
@ExtendWith( ApplicationExtension.class )
public class FXUIScraperTest {

    static {
        if ( Boolean.getBoolean( "SERVER" ) ) {
            System.setProperty( "java.awt.headless", "true" );
            System.setProperty( "testfx.robot", "glass" );
            System.setProperty( "testfx.headless", "true" );
            System.setProperty( "prism.order", "sw" );
            System.setProperty( "prism.text", "t2k" );
            System.setProperty( "glass.platform", "Monocle" );
            System.setProperty( "monocle.platform", "Headless" );
        }
    }
    CustomerController cc;
    Parent pane;
    @Start
    void start( Stage stage ) throws IOException {
        cc= new CustomerController();
        pane = cc.getRoot();

    }

    //@Disabled("Think TDD")
    @org.junit.jupiter.api.Test
    public void tScrape() {
        List<Node> scrape = cc.scrape( n -> n instanceof TextInputControl );
        List<String> collect = scrape
                .stream()
                .map( n -> TextInputControl.class.cast( n ) )
                .map( tc -> tc.getText() )
                .collect( toList() );

        assertThat( collect ).containsExactlyInAnyOrder( "Piet Puk", "Venlo",
                                                         "2021-05-12" );

//        fail( "tScrape tScrape reached end. You know what to do." );
    }

    //@Disabled("Think TDD")
    @Test
    public void tNamedProps() {
        Map<String, StringProperty> collect = cc
                .getNamedProperties( x -> true )
                .stream()
                .collect( Collectors.toMap( KeyValuePair::key,
                                            KeyValuePair::value ) );

        // massage to simple string string pairs
        Map<String, String> collect1 = collect.entrySet().stream().collect(
                toMap( Map.Entry::getKey, me -> me.getValue().getValue() ) );

        assertThat( collect1 ).containsExactlyInAnyOrderEntriesOf(
                Map.of( "name", "Piet Puk",
                        "dob", "2021-05-12",
                        "city", "Venlo" )
        );

//        fail( "method tNamedProps reached end. You know what to do." );
    }

    @Test
    public void tNamedStringPairs() {
        Map<String, String> collect = cc.getNamedTextValues( x -> true )
                .stream().collect(
                        Collectors
                                .toMap( KeyValuePair::key, KeyValuePair::value ) );
        assertThat( collect ).containsExactlyInAnyOrderEntriesOf(
                Map.of( "name", "Piet Puk",
                        "dob", "2021-05-12",
                        "city", "Venlo" )
        );

//        fail( "method tNamedProps reached end. You know what to do." );
    }

    //@Disabled("Think TDD")
    @Test
    void tSimpleScraping() {
        FXUIScraper scraper = () -> pane; // <1>
        Predicate<Node> pred = n -> "dob".equals( n.getId() ); //<2>

        List<Node> scraped = scraper.scrape( pred ); //<3>

        assertThat( scraped ).hasSize( 1 ); //<4>  FXUIScraper scraper = ()-> pane;

//        fail( "method SimpleScraping completed succesfully; you know what to do" );
    }

    //@Disabled("Think TDD")
    @Test
    void tShorterCode() {
        FXUIScraper scraper = () -> pane; // <1>
        // all named nodes
        List<Node> allTextControls = scraper.scrape( n -> (null != n.getId())
                && (n instanceof TextInputControl) );
        assertThat( allTextControls ).hasSize( 3 ); //<4>  FXUIScraper scraper = ()-> pane;

//        fail( "method ShorterCode completed succesfully; you know what to do" );
    }
}
