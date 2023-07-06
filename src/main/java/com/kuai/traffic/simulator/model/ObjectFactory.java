//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.08.08 at 09:32:35 AM AEST 
//


package com.kuai.traffic.simulator.model;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.jci.traffic.simulator.model package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.jci.traffic.simulator.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Coordinate }
     * 
     */
    public Coordinate createCoordinate() {
        return new Coordinate();
    }

    /**
     * Create an instance of {@link Road }
     * 
     */
    public Road createRoad() {
        return new Road();
    }

    /**
     * Create an instance of {@link SideGroup }
     * 
     */
    public SideGroup createSideGroup() {
        return new SideGroup();
    }

    /**
     * Create an instance of {@link Lane }
     * 
     */
    public Lane createLane() {
        return new Lane();
    }

    /**
     * Create an instance of {@link SideSeparator }
     * 
     */
    public SideSeparator createSideSeparator() {
        return new SideSeparator();
    }

    /**
     * Create an instance of {@link CurrentPosition }
     * 
     */
    public CurrentPosition createCurrentPosition() {
        return new CurrentPosition();
    }

    /**
     * Create an instance of {@link Intersection }
     * 
     */
    public Intersection createIntersection() {
        return new Intersection();
    }

    /**
     * Create an instance of {@link Position }
     * 
     */
    public Position createPosition() {
        return new Position();
    }

    /**
     * Create an instance of {@link LastPosition }
     * 
     */
    public LastPosition createLastPosition() {
        return new LastPosition();
    }

    /**
     * Create an instance of {@link TrafficLight }
     * 
     */
    public TrafficLight createTrafficLight() {
        return new TrafficLight();
    }

    /**
     * Create an instance of {@link Traffic }
     * 
     */
    public Traffic createTraffic() {
        return new Traffic();
    }

    /**
     * Create an instance of {@link Vehicle }
     * 
     */
    public Vehicle createVehicle() {
        return new Vehicle();
    }

}