//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.08.08 at 09:32:35 AM AEST 
//


package com.kuai.traffic.simulator.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element ref="{http://www.simulator.traffic.kuai.com/model}position"/>
 *       &lt;/sequence>
 *       &lt;attribute name="backgroundImage" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="length" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="width" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="orientation" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="forwardTime" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="leftTurnTime" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="rightTurnTime" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "position"
})
@XmlRootElement(name = "trafficLight")
public class TrafficLight {

    @XmlElement(required = true)
    protected Position position;
    @XmlAttribute(name = "backgroundImage", required = true)
    protected String backgroundImage;
    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "length", required = true)
    protected int length;
    @XmlAttribute(name = "width", required = true)
    protected int width;
    @XmlAttribute(name = "orientation", required = true)
    protected String orientation;
    @XmlAttribute(name = "forwardTime", required = true)
    protected int forwardTime;
    @XmlAttribute(name = "leftTurnTime", required = true)
    protected int leftTurnTime;
    @XmlAttribute(name = "rightTurnTime", required = true)
    protected int rightTurnTime;

    /**
     * Gets the value of the position property.
     * 
     * @return
     *     possible object is
     *     {@link Position }
     *     
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the value of the position property.
     * 
     * @param value
     *     allowed object is
     *     {@link Position }
     *     
     */
    public void setPosition(Position value) {
        this.position = value;
    }

    /**
     * Gets the value of the backgroundImage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBackgroundImage() {
        return backgroundImage;
    }

    /**
     * Sets the value of the backgroundImage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBackgroundImage(String value) {
        this.backgroundImage = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the length property.
     * 
     */
    public int getLength() {
        return length;
    }

    /**
     * Sets the value of the length property.
     * 
     */
    public void setLength(int value) {
        this.length = value;
    }

    /**
     * Gets the value of the width property.
     * 
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the value of the width property.
     * 
     */
    public void setWidth(int value) {
        this.width = value;
    }

    /**
     * Gets the value of the orientation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrientation() {
        return orientation;
    }

    /**
     * Sets the value of the orientation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrientation(String value) {
        this.orientation = value;
    }

    /**
     * Gets the value of the forwardTime property.
     * 
     */
    public int getForwardTime() {
        return forwardTime;
    }

    /**
     * Sets the value of the forwardTime property.
     * 
     */
    public void setForwardTime(int value) {
        this.forwardTime = value;
    }

    /**
     * Gets the value of the leftTurnTime property.
     * 
     */
    public int getLeftTurnTime() {
        return leftTurnTime;
    }

    /**
     * Sets the value of the leftTurnTime property.
     * 
     */
    public void setLeftTurnTime(int value) {
        this.leftTurnTime = value;
    }

    /**
     * Gets the value of the rightTurnTime property.
     * 
     */
    public int getRightTurnTime() {
        return rightTurnTime;
    }

    /**
     * Sets the value of the rightTurnTime property.
     * 
     */
    public void setRightTurnTime(int value) {
        this.rightTurnTime = value;
    }

}