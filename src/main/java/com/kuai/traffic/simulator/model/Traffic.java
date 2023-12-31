//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.08.08 at 09:32:35 AM AEST 
//


package com.kuai.traffic.simulator.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.simulator.traffic.kuai.com/model}road" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.simulator.traffic.kuai.com/model}intersection" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="driveStyle" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="LEFT"/>
 *             &lt;enumeration value="RIGHT"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "road",
    "intersection"
})
@XmlRootElement(name = "traffic")
public class Traffic {

    protected List<Road> road;
    protected List<Intersection> intersection;
    @XmlAttribute(name = "driveStyle", required = true)
    protected String driveStyle;

    /**
     * Gets the value of the road property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the road property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRoad().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Road }
     * 
     * 
     */
    public List<Road> getRoad() {
        if (road == null) {
            road = new ArrayList<Road>();
        }
        return this.road;
    }

    /**
     * Gets the value of the intersection property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the intersection property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIntersection().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Intersection }
     * 
     * 
     */
    public List<Intersection> getIntersection() {
        if (intersection == null) {
            intersection = new ArrayList<Intersection>();
        }
        return this.intersection;
    }

    /**
     * Gets the value of the driveStyle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDriveStyle() {
        return driveStyle;
    }

    /**
     * Sets the value of the driveStyle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDriveStyle(String value) {
        this.driveStyle = value;
    }

}
