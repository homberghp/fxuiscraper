package nl.homberghp.fxuiscraper;

import javafx.beans.property.StringProperty;

/**
 * Interface to allow adaptation to different tuple styles.
 * @author "Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}"
 */
public interface NamedStringProperty {
    String name();
    StringProperty property();
}
