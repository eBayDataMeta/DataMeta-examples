package org.ebay.datameta.ser.jackson.fasterxml

import java.io.StringWriter
import java.time.ZonedDateTime.now
import java.time.{ZoneId, ZonedDateTime}
import java.util

import com.fasterxml.jackson.core.JsonToken.{END_ARRAY, END_OBJECT}
import com.fasterxml.jackson.core.{JsonFactory, JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind.ObjectMapper
import com.typesafe.scalalogging.StrictLogging
import org.apache.commons.lang3.builder.{MultilineRecursiveToStringStyle, ReflectionToStringBuilder}
import org.ebay.datameta.ser.jackson.fasterxml.gen.v1_0_0.BaseColor.Blue
import org.ebay.datameta.ser.jackson.fasterxml.gen.v1_0_0.WordedEnum.VarString
import org.ebay.datameta.ser.jackson.fasterxml.gen.v1_0_0._
import org.ebay.datameta.util.core.DateTimeUtil
import org.ebay.datameta.util.core.DateTimeUtil.CLOCK

//import scala.collection.JavaConverters._
import scala.collection.convert.WrapAsScala.collectionAsScalaIterable
import scala.language.implicitConversions // to iterate java.util._

/** Jackson Utilities for FasterXML
  *
  * @author Michael Bergens
  */
object JacksonTestApp extends App with StrictLogging {
  println(s"""Started the app, first record class: ${classOf[AllOptCustMatch].getName}""")
  val allOptCustMatch = new AllOptCustMatch
  val w = new StringWriter(8000)
  val jf = new JsonFactory
  val generator: JsonGenerator = jf.createGenerator(w)
  val ks = getKitchenSink1

  val om = new ObjectMapper()

  val v = new MultiFieldId
  val ad = new java.util.LinkedList[Double]
  ad.add(1.2); ad.add(3.4); ad.add(5.67)
  val as = new java.util.HashSet[String]()
  as.add("one"); as.add("two"); as.add("three")
  val dVal = "2017-04-02T23:49:55.176-07:00[America/Los_Angeles]" // see if we can parse our own output
  println(s"""Using Data value of $dVal""")

  v.setA(1234)
  v.setB(9876L)
  v.setC("This 'is' a Cee")
  v.setText(s"""This is "very" textual""")
//  v.setTiming(ZonedDateTime.now(CLOCK))
  v.setTiming(DateTimeUtil.parse(dVal))
  generator.setPrettyPrinter(om.getSerializationConfig.getDefaultPrettyPrinter) // don't do this in generated code
  generator.writeStartObject() // this is necessary because Jackson won't do this automatically
  generator.writeNumberField("A", v.getA)
  generator.writeNumberField("B", v.getB)
  generator.writeStringField("C", v.getC)

  generator.writeStringField("Text", v.getText)
  generator.writeStringField("Timing", DateTimeUtil.toString(v.getTiming))

  generator.writeArrayFieldStart("ad")
  for(d <- ad) generator.writeNumber(d)
  generator.writeEndArray()

  generator.writeArrayFieldStart("as")
  for(s <- as) generator.writeString(s)
  generator.writeEndArray()
  KitchenSink_JSONable.writeField("ks", generator, ks)
  generator.writeEndObject() // Jackson will do this upon close, but it's better to do it manually
  generator.close() // IMPORTANT!!

  val json = w.toString
  logger.info(
    s"""Serialized object as JSON:
       |$json
       |
     """.stripMargin)

  val parser: JsonParser = jf.createParser(json) // http://www.journaldev.com/2324/jackson-json-java-parser-api-example-tutorial
  val reV = new MultiFieldId
  val reAd = new java.util.LinkedList[Double]()
  val reAs = new java.util.HashSet[String]()
  while(parser.nextToken() != END_OBJECT) {
    val fldName = parser.getCurrentName
    if(fldName != null) {
//      println(s"""Processing field $fldName...""")
      parser.nextToken()
      fldName match {
        case "A" => reV.setA(parser.getIntValue)
        case "B" => reV.setB(parser.getLongValue)
        case "C" => reV.setC(parser.getText)
        case "Text" => reV.setText(parser.getText)
        case "Timing" => reV.setTiming(DateTimeUtil.parse(parser.getText))
        case "ad" => while (parser.nextToken() != END_ARRAY) {
          reAd.add(parser.getDoubleValue)
        }
        case "as" =>while (parser.nextToken() != END_ARRAY) {
          reAs.add(parser.getText)
        }
        case "ks" =>
          val reKs = KitchenSink_JSONable.read(parser)
          logger.info(
            s"""${classOf[KitchenSink].getName} resurrected, equality to the original: ${reKs.equals(ks)}, value:
               |${ReflectionToStringBuilder.toString(ks, new MultilineRecursiveToStringStyle)}
             """.stripMargin)
          for(ix <- ks.getBytes.indices) {
            val b: Byte = ks.getBytes()(ix)
            val reB: Byte = reKs.getBytes()(ix)
            if(b != reB) logger.error(f"""Raw Bytes: at the index $ix, bytes are not equal, original is $b%02X, deserialized $reB%02X """)
            else logger.info(f"""Raw Bytes[$ix]=$b%02X""")
          }
        case _ => throw new IllegalArgumentException(s"""Unhandled field "$fldName" """)
        //println(s"Unhandled field: $fldName")
      }
    }
  }
  logger.info(
    s"""Resurrected object equals original == "${reV.equals(v)}": a=${reV.getA}; b=${reV.getB}; c="${reV.getC}";
       |  Text= <${reV.getText}>; Timing=${reV.getTiming}
       |
       |reAd: $reAd; equals == ${ad.equals(reAd)}
       |reAs: $reAs; equals == ${as.equals(reAs)}
       |
       |Done.
     """.stripMargin)

  def getKitchenSink1: KitchenSink = {
    val r = new KitchenSink
    r.setAltered(now(CLOCK))
    r.setAmplitude(9876543210L)
    r.setBearing(1234)
    r.setChoices(new MultiSetString(Array(0xDL)))
    r.setCode("""coded-recoded""")
    r.setColor(Blue)
    val bytes = Array[Byte](1, 0x7F, 0x80.asInstanceOf[Byte], 0xFF.asInstanceOf[Byte])
    r.setBytes(bytes)

    r.setComments(
      """
        |So called "comments" are provided for:
        | * 'clarity'
        | * and improved communications.""".stripMargin)

    r.setContext("Within limits")
    r.setCreated(now())
    r.setDiameter(1234.567F)
    val embo1 = new Embodiment
    embo1.setId(10)
    embo1.setInclusivement("Number One")

    val embo2 = new Embodiment
    embo2.setId(20)
    embo2.setInclusivement("Number Two")

    val embo3 = new Embodiment
    embo3.setId(30)
    embo3.setInclusivement("#Three")

    val emb1 = new EmbeddedType
    emb1.setIntCode(100)
    emb1.setTxtCode("One Hundred")
    emb1.setEmbo(embo1)

    val emb2 = new EmbeddedType
    emb2.setIntCode(200)
    emb2.setTxtCode("Two Hundred")

    val emb3 = new EmbeddedType
    emb3.setIntCode(300)
    emb3.setTxtCode("Three Hundred")
    emb3.setEmbo(embo3)

    r.setEmb(emb1)

    val embeds = new util.ArrayList[EmbeddedType](3)
    embeds.add(emb2)
    embeds.add(emb3)

    r.setEmbeds(embeds)
    r.setEmbo(embo1)
    r.setFieldName(42)
    r.setFrequency(1234567890123L)
    r.setHomeEmail("me@home.com")
    r.setHomePage(new java.net.URL("http://ebay.com"))
    r.setHomeZip("12345-6789")
    r.setId(24L)
    val idless1 = new IdLess
    idless1.setCount(1)
    idless1.setName("Uno")
    val time1 = ZonedDateTime.of(1998, 9, 30, 23, 34, 50, 0, CLOCK.getZone)
    idless1.setWhen(time1)
    val idless2 = new IdLess
    idless2.setCount(2)
    idless2.setName("Dos")
    val time2 = ZonedDateTime.of(2004, 11, 28, 15, 43, 51, 0, ZoneId.of("America/Los_Angeles"))
    idless2.setWhen(time2)
    val idless3 = new IdLess
    idless3.setCount(3)
    idless3.setName("Tres")
    val time3 = ZonedDateTime.of(2015, 1, 2, 1, 3, 1, 0, CLOCK.getZone)
    idless3.setWhen(time3)
    val idLesses = new util.HashSet[IdLess]()
    idLesses.add(idless1)
    idLesses.add(idless2)
    idLesses.add(idless3)
    r.setIdLessNess(idLesses)
    val ints = new util.LinkedList[Integer]()
    ints.add(1)
    ints.add(22)
    ints.add(333)
    r.setInts(ints)

    r.setIsMeasurable(true)
    r.setIsRequired(false)
    r.setLastFirstName("Doe, John") // this value is verified against a pattern, see the model for details
    r.setLength(987929)
    r.setMobilePhone("425-555-1234")
    r.setName("What's in the name?")
    r.setRadius(987.654)
    r.setSalary(new java.math.BigDecimal("350456.95"))
    val strings = new util.HashSet[String]()
    strings.add("ერთი")
    strings.add("ორი")
    strings.add("სამი")
    r.setStrings(strings)
    r.setTemperature(36.7)
    val times = new java.util.HashSet[ZonedDateTime]
    times.add(time1)
    times.add(time2)
    times.add(time3)
    r.setTimes(times)
    r.setType(VarString)
    r.setWeight(45.67F)
    r.setWorkPage(new java.net.URL("http://workable.com"))
    r.setWorkZip("98765")
    r.verify()
    r
  }
}
