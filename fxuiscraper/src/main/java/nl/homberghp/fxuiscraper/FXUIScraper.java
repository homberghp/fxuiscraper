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
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.TextInputControl;

/**
 * Simple text property scraper.
 *
 * Usage:
 * <pre class='brush:java'>
 * {@code
 *  Parent root=...;
 *  FXUIScraper scraper = ()-> root;
 *  Set<String> controls=Set.of("name", "dob");
 *  Map<String,String> inputs=scraper.scrape(x -> true, controls);
 *  // use inputs.
 * }
 * </pre>
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
     *
     * @param pred         to apply to nodes found.
     * @param controlNames of controls to findThis should be sufficient to
     *                     complete the puzzle.
     *
     *
     * @return The list of nodes matching the predicate.
     */
    default List<Node> scrape( Predicate<Node> pred, String... controlNames ) {
        Set<String> controls = Set.of( controlNames );
        return scrape( pred, controls );
    }

    /**
     * Scrape for nodes matching predicate and names.
     * Prefer this method of the varags variant, because the set is typically
     * constant and can be a static field in the client and you save on repeated
     * array and set allocations.
     *
     * @param pred     to match
     * @param controls to find
     *
     * @return the nodes
     */
    default List<Node> scrape( Predicate<Node> pred, Set<String> controls ) {
        Parent root = getRoot();
        List<Node> result = new ArrayList<>();
        Predicate<Node> cPred = pred.and( n -> controls.contains( n.getId() ) );
        scrape( root, cPred, result );
        return result;
    }

    /**
     * Recursively traverse the fx node tree.
     *
     * @param root   of tree
     * @param pred   to find matching nodes
     * @param result list of matching nodes in this (sub) tree
     */
    private static void scrape( Parent root, Predicate<Node> pred,
                                List<Node> result ) {
        var childrenUnmodifiable = root.getChildrenUnmodifiable();
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
     * @param pred         to find properties
     * @param controlNames names of controls to find
     *
     * @return the matching named string properties
     */
    default List<KeyValuePair<String, StringProperty>> getNamedProperties(
            Predicate<Node> pred, String... controlNames ) {

        return getNamedProperties( pred, Set.of( controlNames ) );
    }

    default List<KeyValuePair<String, StringProperty>> getNamedProperties(
            Predicate<Node> pred, Set<String> controlNames ) {

        return textInputControlStream( pred, controlNames )
                .map( n -> new KeyValuePairImpl<String, StringProperty>(
                n.getId(),
                n.textProperty() ) )
                .collect( toList() );
    }

    /**
     * Intermediate stream of name property pairs.
     *
     * @param pred         to apply
     * @param controlNames names of controls
     *
     * @return Stream of name TextInputControls
     */
    default Stream<TextInputControl> textInputControlStream(
            Predicate<Node> pred, Set<String> controlNames ) {
        Predicate<Node> cPred = pred.and( this::isInputControl );
        return scrape( cPred, controlNames )
                .stream()
                .map( n -> TextInputControl.class.cast( n ) );
    }

    /**
     * Return the scraped data as a list of tuples.
     *
     * @param pred  filter to apply
     * @param names to look for
     *
     * @return map of input
     */
    default List<KeyValuePair<String, String>> getNamedTextValues(
            Predicate<Node> pred,
            String... names ) {
        return getNamedTextValues( pred, Set.of( names ) );

    }

    default List<KeyValuePair<String, String>> getNamedTextValues(
            Predicate<Node> pred,
            Set<String> names ) {
        return inputControlStream( pred, names )
                .map( n -> new KeyValuePairImpl<String, String>(
                n.getId(), toString( n )
        ) ).collect( toList() );

    }

    /**
     * Find the input controls.
     *
     * @param pred  to maches
     * @param names of controls
     *
     * @return the nodes as stream.
     */
    default Stream<Node> inputControlStream( Predicate<Node> pred,
                                             Set<String> names ) {
        Predicate<Node> cPred = pred.and( this::isInputControl );
        return scrape( cPred, names ).stream();
    }

    /**
     * What is an input control.
     * Maybe there is something like a common ancestor or a marker interface.
     *
     * @param n node to check
     *
     * @return the nodes as stream.
     */
    default boolean isInputControl( Node n ) {
        return ( n instanceof TextInputControl )
                || ( n instanceof ComboBoxBase );
    }

    /**
     * Return the scraped data as a map of string,string.
     *
     * @param pred         filter to apply
     * @param controlNames of controls to find
     *
     * @return the map
     */
    default Map<String, String> getKeyValues( Predicate<Node> pred,
                                              String... controlNames ) {
        return getKeyValues( pred, Set.of( controlNames ) );
    }

    default Map<String, String> getKeyValues( Predicate<Node> pred,
                                              Set<String> controlNames ) {
        return inputControlStream( pred, controlNames )
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
            return Objects.toString( ( (ComboBoxBase) n ).getValue() );
        }

        // never return null
        return "";

    }

}
