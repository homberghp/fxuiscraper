package nl.homberghp.fxuiscraper;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@fontys.nl}
 * @param <K> key type
 * @param <V> value type
 */
public interface KeyValuePair<K,V> {
    K key();
    V value();
}
