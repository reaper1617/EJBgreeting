package org.jboss.as.quickstarts.ejbinwar.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/** Order Data Transfer Object
 * @author Reaper
 * @version 1.0
 */

public class OrderDTO implements Serializable {
    private String id;
    private String personalNumber;
    private String description;
    private String date;
    private String status;
    private String assignedTruckId;
    private String assignedTruckRegistrationNumber;
    private String[] cargosInOrder;
    private String[] assignedDrivers;
    private String[] route;

    public OrderDTO() {
    }

    public OrderDTO(String id, String personalNumber, String description, String date, String status) {
        this.id = id;
        this.personalNumber = personalNumber;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    public OrderDTO(String id, String personalNumber, String description, String date, String status, String assignedTruckId, String assignedTruckRegistrationNumber, String[] cargosInOrder, String[] assignedDrivers, String[] route) {
        this.id = id;
        this.personalNumber = personalNumber;
        this.description = description;
        this.date = date;
        this.status = status;
        this.assignedTruckId = assignedTruckId;
        this.assignedTruckRegistrationNumber = assignedTruckRegistrationNumber;
        this.cargosInOrder = cargosInOrder;
        this.assignedDrivers = assignedDrivers;
        this.route = route;
    }


    public String getAssignedTruckId() {
        return assignedTruckId;
    }

    public void setAssignedTruckId(String assignedTruckId) {
        this.assignedTruckId = assignedTruckId;
    }

    public String getAssignedTruckRegistrationNumber() {
        return assignedTruckRegistrationNumber;
    }

    public void setAssignedTruckRegistrationNumber(String assignedTruckRegistrationNumber) {
        this.assignedTruckRegistrationNumber = assignedTruckRegistrationNumber;
    }

    public String[] getCargosInOrder() {
        return cargosInOrder;
    }

    public void setCargosInOrder(String[] cargosInOrder) {
        this.cargosInOrder = cargosInOrder;
    }

    public String[] getAssignedDrivers() {
        return assignedDrivers;
    }

    public void setAssignedDrivers(String[] assignedDrivers) {
        this.assignedDrivers = assignedDrivers;
    }

    public String[] getRoute() {
        return route;
    }

    public void setRoute(String[] route) {
        this.route = route;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
