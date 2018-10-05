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
    private String status;

    public OrderDTO() {
    }

    public OrderDTO(String id, String personalNumber, String description, String status) {
        this.id = id;
        this.personalNumber = personalNumber;
        this.description = description;
        this.status = status;
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
