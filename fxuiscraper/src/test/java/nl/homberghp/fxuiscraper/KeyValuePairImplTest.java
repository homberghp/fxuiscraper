package nl.homberghp.fxuiscraper;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;
import org.assertj.core.api.SoftAssertions;

/**
 *
 * @author Pieter van den Hombergh {@code Pieter.van.den.Hombergh@gmail.com}
 */
public class KeyValuePairImplTest {

    //@Disabled("Think TDD")
    @Test
    void tmethod() {
        KeyValuePairImpl<String, Integer> ref = new KeyValuePairImpl<>( 
                "the answer", 42 );
        KeyValuePairImpl<String, Integer> equ = new KeyValuePairImpl<>( 
                "the answer", 42 );
        KeyValuePairImpl<String, Double> kv1 = new KeyValuePairImpl<>( 
                "the answer", 42.0 );
        KeyValuePairImpl<String, Integer> kv2 = new KeyValuePairImpl<>( 
                "not the answer", 42 );
        Object nul = null;
        String hello = "Hello";
        SoftAssertions.assertSoftly( softly -> {
            softly.assertThat( ref.equals( ref ) ).isTrue();
            softly.assertThat( ref.equals( equ ) ).isTrue();
            softly.assertThat( ref.equals( kv1 ) ).isFalse();
            softly.assertThat( ref.equals( kv2 ) ).isFalse();
            softly.assertThat( ref.equals( nul ) ).isFalse();
            softly.assertThat( ref.equals( hello ) ).isFalse();
            assertThat(ref.hashCode()).isNotEqualTo( kv1.hashCode());
        } );

//        fail( "method method completed succesfully; you know what to do" );
    }
    
    //@Disabled("Think TDD")
    @Test
    void tToString() {
        KeyValuePairImpl<String, Integer> ref = new KeyValuePairImpl<>( 
                "the answer", 42 );
        assertThat(ref.toString()).contains( ref.key(),""+ref.value());
//        fail( "method ToString completed succesfully; you know what to do" );
    }

}
