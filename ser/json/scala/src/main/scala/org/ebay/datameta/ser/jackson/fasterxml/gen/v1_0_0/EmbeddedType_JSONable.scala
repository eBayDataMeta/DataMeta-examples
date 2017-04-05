package org.ebay.datameta.ser.jackson.fasterxml.gen.v1_0_0

import org.ebay.datameta.ser.jackson.fasterxml.JacksonUtil._
import org.ebay.datameta.ser.jackson.fasterxml.Jsonable
import com.fasterxml.jackson.core.{JsonFactory, JsonGenerator, JsonParser, JsonToken}
import com.fasterxml.jackson.core.JsonToken.{END_ARRAY, END_OBJECT}

/**

 * This class is generated by
 * <a href='https://github.com/eBayDataMeta/DataMeta'>DataMeta</a>.
 */
object EmbeddedType_JSONable extends Jsonable[EmbeddedType] {

  override def write(out: JsonGenerator, value: EmbeddedType) {
    value.verify()

    if(value.getEmbo != null) Embodiment_JSONable.writeField("embo", out, value.getEmbo)
    out.writeNumberField("intCode", value.getIntCode)
    out.writeStringField("txtCode", value.getTxtCode)
  }

  override def read(in: JsonParser, value: EmbeddedType): EmbeddedType = {
    while(in.nextToken() != END_OBJECT) {
      val fldName = in.getCurrentName
      if(fldName != null) {
        in.nextToken()
        fldName match {

          case "embo" =>
            value.setEmbo(Embodiment_JSONable.read(in))

          case "intCode" =>
            value.setIntCode(in.getIntValue)

          case "txtCode" =>
            value.setTxtCode(readText(in))

          case _ => throw new IllegalArgumentException(s"""Unhandled field "$fldName" """)
        }
      }
    }
    value
  }

  override def read(in: JsonParser): EmbeddedType = {
    read(in, new EmbeddedType())
  }
}