package nl.homberghp.fxuiscraper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import java.util.stream.Stream;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.TextInputControl;

/**
 * Simple text property scraper.
 *
 * @author "Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}"
 */
public interface FXUIScraper {

    /**
     * Provide starting point for scraping.
     *
     * @return the root of the fx node tree to scrape
     */
    Parent getRoot();

    /**
     * Start the scraping recursion.
     *
     * @param pred to apply to nodes found.
     *
     * @return The list of nodes matching the predicate.
     */
    default List<Node> scrape( Predicate<Node> pred, String... controlNames ) {
        Parent root = getRoot();
        Set<String> controls = Set.of( controlNames );
        List<Node> result = new ArrayList<>();
        Predicate<Node> cPred = pred.and( n -> controls.contains( n ) );
        scrape( root, pred, result );
        return result;
    }

    /**
     * Recurse further down the fx node tree.
     *
     * @param root   of tree
     * @param pred   to find matching nodes
     * @param result list of matching nodes in this (sub) tree
     */
    private static void scrape( Parent root, Predicate<Node> pred, List<Node> result ) {
        ObservableList<Node> childrenUnmodifiable = root
                .getChildrenUnmodifiable();
        for ( Node node : childrenUnmodifiable ) {
            if ( node.getId() != null && pred.test( node ) ) {
                result.add( node );
            }
            if ( node instanceof Parent ) { // with instance of pattern matchin (java 16) simpler
                scrape( (Parent) node, pred, result );
            }
        }
    }

    /**
     * Scrape for the properties.
     *
     * @param pred to find properties
     *
     * @return the matching named string properties
     */
    default List<KeyValuePair<String, StringProperty>> getNamedProperties(
            Predicate<Node> pred ) {
        
        return textInputControlStream( pred )
                .map( n -> new KeyValuePairImpl<String, StringProperty>(
                n.getId(),
                n.textProperty() ) )
                .collect( toList() );
    }

    /**
     * Intermediate stream of name property pairs.
     *
     * @param pred to apply
     *
     * @return Stream of name TextInputControls
     */
    default Stream<TextInputControl> textInputControlStream(
            Predicate<Node> pred ) {
        Predicate<Node> cPred = pred.and( this::isInputControl );
        return scrape( cPred )
                .stream()
                .map( n -> TextInputControl.class.cast( n ) );
    }

    /**
     * Return the scraped data as a list of tuples.
     *
     * @param pred filter to apply
     *
     * @return map of input
     */
    default List<KeyValuePair<String, String>> getNamedTextValues(
            Predicate<Node> pred ) {
        return inputControlStream( pred )
                .map( n -> new KeyValuePairImpl<String, String>(
                n.getId(), toString( n )
        ) ).collect( toList() );
        
    }
    
    default Stream<Node> inputControlStream( Predicate<Node> pred ) {
        Predicate<Node> cPred = pred.and( this::isInputControl );
        return scrape( cPred ).stream();
    }
    
    default boolean isInputControl( Node n ) {
        return ( n instanceof TextInputControl )
                || ( n instanceof ComboBoxBase );
    }

    /**
     * Return the scraped data as a map of string,string.
     *
     * @param pred filter to apply
     *
     * @return the map
     */
    default Map<String, String> getKeyValues( Predicate<Node> pred ) {
        return inputControlStream( pred )
                .collect( toMap( n -> n.getId(), n -> toString( n ) ) );
    }

    /**
     * Convenience factory method to start at any parent.
     *
     * Intended for those that find lambdas hard to grasp.
     *
     * @param root of (sub)tree to scan
     *
     * @return matching nodes in the tree
     */
    static FXUIScraper standardScraper( final Parent root ) {
        return () -> root;
    }
    
    static String toString( Node n ) {
        if ( n instanceof TextInputControl ) {
            return ( (TextInputControl) n ).getText();
        }
        if ( n instanceof ComboBoxBase ) {
            return ( (ComboBoxBase) n ).getValue().toString();
        }

        // never return null
        return "";
        
    }
    
}
