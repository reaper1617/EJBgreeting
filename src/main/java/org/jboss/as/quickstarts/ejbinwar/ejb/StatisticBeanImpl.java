//package org.jboss.as.quickstarts.ejbinwar.ejb;
//
//
//import javax.ejb.Stateful;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Stateful
//public class StatisticBeanImpl implements StatisticBean{
//
//    private int numOfTrucksTotal;
//    private int numOfTrucksFree;
//    private int numOfTrucksNotReady;
//    private int numOfTrucksExecutingOrders;
//    private int numOfDriversTotal;
//    private int numOfDriversFree;
//    private int numOfDriversExecutingOrders;
//
//    public StatisticBeanImpl() {
//    }
//
//    public int getNumOfTrucksTotal() {
//        return numOfTrucksTotal;
//    }
//
//    public void setNumOfTrucksTotal(int numOfTrucksTotal) {
//        this.numOfTrucksTotal = numOfTrucksTotal;
//    }
//
//    public int getNumOfTrucksFree() {
//        return numOfTrucksFree;
//    }
//
//    public void setNumOfTrucksFree(int numOfTrucksFree) {
//        this.numOfTrucksFree = numOfTrucksFree;
//    }
//
//    public int getNumOfTrucksNotReady() {
//        return numOfTrucksNotReady;
//    }
//
//    public void setNumOfTrucksNotReady(int numOfTrucksNotReady) {
//        this.numOfTrucksNotReady = numOfTrucksNotReady;
//    }
//
//    public int getNumOfTrucksExecutingOrders() {
//        return numOfTrucksExecutingOrders;
//    }
//
//    public void setNumOfTrucksExecutingOrders(int numOfTrucksExecutingOrders) {
//        this.numOfTrucksExecutingOrders = numOfTrucksExecutingOrders;
//    }
//
//    public int getNumOfDriversTotal() {
//        return numOfDriversTotal;
//    }
//
//    public void setNumOfDriversTotal(int numOfDriversTotal) {
//        this.numOfDriversTotal = numOfDriversTotal;
//    }
//
//    public int getNumOfDriversFree() {
//        return numOfDriversFree;
//    }
//
//    public void setNumOfDriversFree(int numOfDriversFree) {
//        this.numOfDriversFree = numOfDriversFree;
//    }
//
//    public int getNumOfDriversExecutingOrders() {
//        return numOfDriversExecutingOrders;
//    }
//
//    public void setNumOfDriversExecutingOrders(int numOfDriversExecutingOrders) {
//        this.numOfDriversExecutingOrders = numOfDriversExecutingOrders;
//    }
//
//    public List<Integer> getAsIntegerList(){
//        List<Integer> result = new ArrayList<Integer>();
//        result.add(numOfTrucksTotal);
//        result.add(numOfTrucksFree);
//        result.add(numOfTrucksNotReady);
//        result.add(numOfTrucksExecutingOrders);
//        result.add(numOfDriversTotal);
//        result.add(numOfDriversFree);
//        result.add(numOfDriversExecutingOrders);
//        return result;
//    }
//
//    public Map<String,Integer> getAsMap(){
//        Map<String,Integer> map = new HashMap<String, Integer>();
//        map.put("trucksTotal",numOfTrucksTotal);
//        map.put("trucksFree",numOfTrucksFree);
//        map.put("trucksNotReady",numOfTrucksNotReady);
//        map.put("trucksExecOrders",numOfTrucksExecutingOrders);
//        map.put("driversTotal",numOfDriversTotal);
//        map.put("driversFree",numOfDriversFree);
//        map.put("driversExecOrders",numOfDriversExecutingOrders);
//        return map;
//    }
//}
