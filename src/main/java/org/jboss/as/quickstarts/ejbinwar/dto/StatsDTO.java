package org.jboss.as.quickstarts.ejbinwar.dto;

public class StatsDTO {
    private int numOfTrucksTotal;
    private int numOfTrucksFree;
    private int numOfTrucksNotReady;
    private int numOfTrucksExecutingOrders;
    private int numOfDriversTotal;
    private int numOfDriversFree;
    private int numOfDriversExecutingOrders;

    public StatsDTO() {
    }

    public StatsDTO(int numOfTrucksTotal, int numOfTrucksFree, int numOfTrucksNotReady, int numOfTrucksExecutingOrders, int numOfDriversTotal, int numOfDriversFree, int numOfDriversExecutingOrders) {
        this.numOfTrucksTotal = numOfTrucksTotal;
        this.numOfTrucksFree = numOfTrucksFree;
        this.numOfTrucksNotReady = numOfTrucksNotReady;
        this.numOfTrucksExecutingOrders = numOfTrucksExecutingOrders;
        this.numOfDriversTotal = numOfDriversTotal;
        this.numOfDriversFree = numOfDriversFree;
        this.numOfDriversExecutingOrders = numOfDriversExecutingOrders;
    }

    public int getNumOfTrucksTotal() {
        return numOfTrucksTotal;
    }

    public void setNumOfTrucksTotal(int numOfTrucksTotal) {
        this.numOfTrucksTotal = numOfTrucksTotal;
    }

    public int getNumOfTrucksFree() {
        return numOfTrucksFree;
    }

    public void setNumOfTrucksFree(int numOfTrucksFree) {
        this.numOfTrucksFree = numOfTrucksFree;
    }

    public int getNumOfTrucksNotReady() {
        return numOfTrucksNotReady;
    }

    public void setNumOfTrucksNotReady(int numOfTrucksNotReady) {
        this.numOfTrucksNotReady = numOfTrucksNotReady;
    }

    public int getNumOfTrucksExecutingOrders() {
        return numOfTrucksExecutingOrders;
    }

    public void setNumOfTrucksExecutingOrders(int numOfTrucksExecutingOrders) {
        this.numOfTrucksExecutingOrders = numOfTrucksExecutingOrders;
    }

    public int getNumOfDriversTotal() {
        return numOfDriversTotal;
    }

    public void setNumOfDriversTotal(int numOfDriversTotal) {
        this.numOfDriversTotal = numOfDriversTotal;
    }

    public int getNumOfDriversFree() {
        return numOfDriversFree;
    }

    public void setNumOfDriversFree(int numOfDriversFree) {
        this.numOfDriversFree = numOfDriversFree;
    }

    public int getNumOfDriversExecutingOrders() {
        return numOfDriversExecutingOrders;
    }

    public void setNumOfDriversExecutingOrders(int numOfDriversExecutingOrders) {
        this.numOfDriversExecutingOrders = numOfDriversExecutingOrders;
    }
}
