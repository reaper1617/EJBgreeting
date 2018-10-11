package org.jboss.as.quickstarts.ejbinwar.dto;

public class StatsDTO {
    private String trucksTotal;
    private String trucksFree;
    private String trucksNotReady;
    private String trucksExecOrders;
    private String driversTotal;
    private String driversFree;
    private String driversExecOrders;

    public StatsDTO() {
    }

    public String getTrucksTotal() {
        return trucksTotal;
    }

    public void setTrucksTotal(String trucksTotal) {
        this.trucksTotal = trucksTotal;
    }

    public String getTrucksFree() {
        return trucksFree;
    }

    public void setTrucksFree(String trucksFree) {
        this.trucksFree = trucksFree;
    }

    public String getTrucksNotReady() {
        return trucksNotReady;
    }

    public void setTrucksNotReady(String trucksNotReady) {
        this.trucksNotReady = trucksNotReady;
    }

    public String getTrucksExecOrders() {
        return trucksExecOrders;
    }

    public void setTrucksExecOrders(String trucksExecOrders) {
        this.trucksExecOrders = trucksExecOrders;
    }

    public String getDriversTotal() {
        return driversTotal;
    }

    public void setDriversTotal(String driversTotal) {
        this.driversTotal = driversTotal;
    }

    public String getDriversFree() {
        return driversFree;
    }

    public void setDriversFree(String driversFree) {
        this.driversFree = driversFree;
    }

    public String getDriversExecOrders() {
        return driversExecOrders;
    }

    public void setDriversExecOrders(String driversExecOrders) {
        this.driversExecOrders = driversExecOrders;
    }
}
