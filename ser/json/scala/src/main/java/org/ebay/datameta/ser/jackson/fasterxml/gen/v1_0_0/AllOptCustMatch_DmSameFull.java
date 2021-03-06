package org.ebay.datameta.ser.jackson.fasterxml.gen.v1_0_0;

import java.time.ZonedDateTime;


import org.ebay.datameta.dom.DataMetaSame;
import org.ebay.datameta.util.jdk.SemanticVersion;

/**

 * This class is generated by
 * <a href='https://github.com/eBayDataMeta/DataMeta'>DataMeta</a>.
 */
public class AllOptCustMatch_DmSameFull implements DataMetaSame<AllOptCustMatch>{
    /**
    * Convenience instance.
    */
    public final static AllOptCustMatch_DmSameFull I = new AllOptCustMatch_DmSameFull();
    @Override public boolean isSame(final AllOptCustMatch one, final AllOptCustMatch another) {
        if(one == another) return true; // same object or both are null
        //noinspection SimplifiableIfStatement
        if(one == null || another == null) return false; // whichever of them is null but the other is not
        
        return EQ.isSame(one.getCounter(), another.getCounter()) && EQ.isSame(one.getWhen(), another.getWhen()) && EQ.isSame(one.getWhat(), another.getWhat());
    }
    public static final SemanticVersion VERSION = SemanticVersion.parse("1.0.0");
}
