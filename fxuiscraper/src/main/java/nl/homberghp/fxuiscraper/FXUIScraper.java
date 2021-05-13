package nl.homberghp.fxuiscraper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
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
    default List<Node> scrape( Predicate<Node> pred ) {
        Parent root = getRoot();

        List<Node> result = new ArrayList<>();
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
    static void scrape( Parent root, Predicate<Node> pred, List<Node> result ) {
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
     * @param filter to find properties
     *
     * @return the matching named string properties
     */
    default List<KeyValuePair<String, StringProperty>> getNamedProperties( 
            Predicate<Node> filter ) {

        return textInputControlStream( filter )
                .map( n -> new KeyValuePairImpl<String, StringProperty>(
                n.getId(),
                n.textProperty() ) )
                .collect( toList() );
    }

    default Stream<TextInputControl> textInputControlStream( 
            Predicate<Node> filter ) {
        Stream<TextInputControl> map = scrape( filter )
                .stream().filter( n -> n instanceof TextInputControl )
                .map( n -> TextInputControl.class.cast( n ) );
        return map;
    }

    default List<KeyValuePair<String, String>> getNamedTextValues( 
            Predicate<Node> filter ) {
        return textInputControlStream( filter )
                .map( n -> new KeyValuePairImpl<String, String>(
                n.getId(), n.getText()
        ) ).collect( toList() );

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
        return  () -> root;
    }
}
