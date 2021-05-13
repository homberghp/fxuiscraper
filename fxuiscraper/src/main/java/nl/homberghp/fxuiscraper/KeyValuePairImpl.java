package nl.homberghp.fxuiscraper;

import java.io.Serializable;
import java.util.Objects;


/**
 * Simple pair, candidate record for java [16,) 
 * 
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@fontys.nl}
 * @param <K> key type
 * @param <V> value type
 */
public class KeyValuePairImpl<K, V> implements KeyValuePair<K, V> {

    final K key;
    final V value;

    public KeyValuePairImpl( K key, V value ) {
        this.key = key;
        this.value = value;
    }
    
    
    @Override
    public K key() {
        return key;
    }

    @Override
    public V value() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode( this.key );
        hash = 67 * hash + Objects.hashCode( this.value );
        return hash;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final KeyValuePairImpl<?, ?> other = (KeyValuePairImpl<?, ?>) obj;
        if ( !Objects.equals( this.key, other.key ) ) {
            return false;
        }
        if ( !Objects.equals( this.value, other.value ) ) {
            return false;
        }
        return true;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "KeyValue{" + key + "=" + value + '}';
    }
    
}
