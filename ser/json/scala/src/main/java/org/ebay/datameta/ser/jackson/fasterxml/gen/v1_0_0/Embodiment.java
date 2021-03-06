package org.ebay.datameta.ser.jackson.fasterxml.gen.v1_0_0;


import org.ebay.datameta.dom.Verifiable;
import java.util.Objects;
import java.util.StringJoiner;
import org.ebay.datameta.dom.VerificationException;
import org.ebay.datameta.util.jdk.SemanticVersion;
import static org.ebay.datameta.dom.CannedRegexUtil.getCannedRegEx;

/**

 * This class is generated by
 * <a href='https://github.com/eBayDataMeta/DataMeta'>DataMeta</a>.
 */
public class Embodiment implements Verifiable {

    public static final SemanticVersion VERSION = SemanticVersion.parse("1.0.0");



    private Integer id;
    private String inclusivement;

    public void setId(final Integer newValue) {
        if(newValue == null) throw new IllegalArgumentException(
                "NULL passed to the setter of the required field 'id' on the class org.ebay.datameta.ser.jackson.fasterxml.gen.v1_0_0.Embodiment.");
        this.id = newValue;
    }

    public Integer getId() {return this.id; }
    public void setInclusivement(final String newValue) {
        if(newValue == null) throw new IllegalArgumentException(
                "NULL passed to the setter of the required field 'inclusivement' on the class org.ebay.datameta.ser.jackson.fasterxml.gen.v1_0_0.Embodiment.");
        this.inclusivement = newValue;
    }

    public String getInclusivement() {return this.inclusivement; }

    /**
    * If there is class type mismatch, somehow we are comparing apples to oranges, this is an error, not
    * a not-equal condition.
    */
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass") @Override public boolean equals(Object other) {
        return Objects.deepEquals(new Object[]{this.id},
          new Object[]{((Embodiment) other).id});
    }

    @Override public int hashCode() {// null - safe: result = 31 * result + (element == null ? 0 : element.hashCode());
        return Objects.hash(this.id);
    }

    public void verify() {

        StringJoiner missingFields = new StringJoiner(", ");
        if(id == null) missingFields.add("id");
        if(inclusivement == null) missingFields.add("inclusivement");
        if(missingFields.length() != 0) throw new VerificationException(getClass().getSimpleName() + ": required fields not set: " + missingFields);




    }

    public final SemanticVersion getVersion() { return VERSION; }
}
