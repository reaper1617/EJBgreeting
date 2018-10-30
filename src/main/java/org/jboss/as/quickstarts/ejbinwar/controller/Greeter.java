package org.jboss.as.quickstarts.ejbinwar.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rabbitmq.client.*;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.jboss.as.quickstarts.ejbinwar.dto.OrderDTO;
import org.jboss.as.quickstarts.ejbinwar.dto.StatsDTO;
import org.jboss.as.quickstarts.ejbinwar.ejb.GreeterEJB;

import org.jboss.as.quickstarts.ejbinwar.enums.UpdateMessageType;
import org.jboss.as.quickstarts.ejbinwar.socket.MyWebSocket;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.websocket.*;

import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.*;


@Named("greeter")
//@SessionScoped
@Singleton
public class Greeter implements Serializable {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(Greeter.class);


    /** Default value included to remove warning. **/
    private static final long serialVersionUID = 1L;
    private final static String QUEUE_NAME = "hello";


    /** for rabbitmq **/
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;


    @EJB
    private GreeterEJB greeterEJB;



    private String message = "First init";

    private static final String STATS_RESOURCE_URL = "http://localhost:8080/worldwidelogistics/stats";
    private static final String ORDERS_RESOURCE_URL = "http://localhost:8080/worldwidelogistics/orders";


    private static final int NUMBER_OF_ORDERS = 10;
    private List<OrderDTO> orders;




//    private List<Integer> currentStatsValues;
    // for statistics
//    private int numOfTrucksTotal;
//    private int numOfTrucksFree;
//    private int numOfTrucksNotReady;
//    private int numOfTrucksExecutingOrders;
//    private int numOfDriversTotal;
//    private int numOfDriversFree;
//    private int numOfDriversExecutingOrders;
    private StatsDTO statsDTO;


//    public Greeter() {
//        LOGGER.info("Init" + this.getClass());
//        initRabbitMQListener();
//        String rest = initOrdersListFromWebService();
//        orders = new ArrayList<OrderDTO>();
//        Gson gson = new Gson();
//        Type collectionType = new TypeToken<List<OrderDTO>>(){}.getType();
//        orders = gson.fromJson(rest, collectionType);
//        String statsFromWS = initStatsFieldsFromWebService();
//        statsDTO = gson.fromJson(statsFromWS, StatsDTO.class);
//    }

    @PostConstruct
    private void init(){
        LOGGER.info("Init " + this.getClass());
        initRabbitMQListener();
        String rest = initOrdersListFromWebService();
        orders = new ArrayList<OrderDTO>();
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<OrderDTO>>(){}.getType();
        orders = gson.fromJson(rest, collectionType);
        String statsFromWS = initStatsFieldsFromWebService();
        statsDTO = gson.fromJson(statsFromWS, StatsDTO.class);
    }

    @PreDestroy
    private void preDestroy(){
        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setName(String name) {
        message = greeterEJB.sayHello(name) ;
    }

    public String getMessage() {
        return message;
    }


    public List<OrderDTO> getOrders() {
        return orders;
    }

    public StatsDTO getStatsDTO() {
        return statsDTO;
    }

    private void initRabbitMQListener(){
        LOGGER.info("Class:" + this.getClass() + " metod: initRabbitMQListener() invoked.");
        factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    LOGGER.info("Class:" + this.getClass() + " in handleDelivery method");
                    String msg = new String(body, "UTF-8");
                    LOGGER.info("Class:" + this.getClass() + " in handleDelivery method " + " received message: " + msg );
                    message = msg;
                    //orders = generateList();
                    String messageToPeers = getProcessMessageResult(msg);
                    if (messageToPeers == null) messageToPeers = "{\"message\":\"null\"}";
                    LOGGER.info("Class:" + this.getClass() + " in handleDelivery method, Message to peers: " + messageToPeers);
                    LOGGER.info("Class:" + this.getClass() + " in handleDelivery method, sending message to peers...");
                    Set<Session> peers = MyWebSocket.getPeers();
                    LOGGER.info("Class:" + this.getClass() + " in handleDelivery method, peers: " + peers);
                    for(Session peer: peers){
                        LOGGER.info("Class:" + this.getClass() + " in handleDelivery method, sending message to peer: " + peer);
                        peer.getBasicRemote().sendText(messageToPeers);
                    }
                    LOGGER.info("Class:" + this.getClass() + " in handleDelivery method, message sent to all peers: ");
                   // myStatelessEJB.setStatelessBeanMessage(msg);
                    //todo: send command: refresh table with orders or refresh fields with statistics
                    LOGGER.info("Class:" + this.getClass() + " out from handleDelivery method");
                }
            };
        channel.basicConsume(QUEUE_NAME, true, consumer);
        LOGGER.info("Class:" + this.getClass() + " out from initRabbitMQListener() method");
        }
        catch (Exception e){
            e.printStackTrace();
            initRabbitMQListener();
        }
    }


//    public List<OrderDTO> generateList(){
//        List<OrderDTO> result = new ArrayList<OrderDTO>();
//        int size = new Random().nextInt(10) + 1;
//        for(int i = 0; i < size; i++){
//            result.add(new OrderDTO("id", "pNum", "descr","Status"));
//        }
//        return result;
//    }

    private String getProcessMessageResult(String message){
        LOGGER.info("Class:" + this.getClass() + " metod: getProcessMessageResult() invoked.");
        if (message == null || message.length()==0) return null;
        if (message.contains(UpdateMessageType.ORDER_CREATED.toString())){
            LOGGER.info("Class:" + this.getClass() + " metod: getProcessMessageResult() " + "message processing: " + message);
            // process: add new order into table
            refreshStatsFieldsFromWebService();
            return addNewOrderToList(message);
        }
        if (message.contains(UpdateMessageType.ORDER_EDITED.toString())){
            //process: find by id? if found - refresh and repaint
            refreshStatsFieldsFromWebService();
            return changeOrderDataInList(message);
        }
        if (message.contains(UpdateMessageType.ORDER_DELETED.toString())){
            //process: ?? add status: deleted , and repaint
            refreshStatsFieldsFromWebService();
            return deleteOrderDataInList(message);
        }
        if (message.contains(UpdateMessageType.DRIVER_CREATED.toString())
        || message.contains(UpdateMessageType.DRIVER_EDITED.toString())
        || message.contains(UpdateMessageType.DRIVER_DELETED.toString())
                ||message.contains(UpdateMessageType.TRUCK_CREATED.toString())
                || message.contains(UpdateMessageType.TRUCK_EDITED.toString())
                || message.contains(UpdateMessageType.TRUCK_DELETED.toString())){
            return refreshStatsFields(message);
        }
        if (message.contains(UpdateMessageType.USER_CREATED.toString())
                || message.contains(UpdateMessageType.USER_EDITED.toString())
                || message.contains(UpdateMessageType.USER_DELETED.toString())){
            //todo:: define if user is driver
            return refreshStatsFields(message);
        }
        LOGGER.info("Class:" + this.getClass() + " out from getProcessMessageResult() method.");
        return null;
    }

    // todo: may be not String but boolean
    private String addNewOrderToList(String messageFromServer){
        LOGGER.info("Class:" + this.getClass() + " metod: addNewOrderToList() invoked.");
        if (messageFromServer == null || messageFromServer.length() == 0) return null;
        String jsonString = messageFromServer.substring(14);
        LOGGER.info("Class:" + this.getClass() + " metod: addNewOrderToList() " + " jsonString processing: " + jsonString);
        Gson gson = new Gson();
        OrderDTO newOrderDTO = gson.fromJson(jsonString, OrderDTO.class);
        if (newOrderDTO == null) return null;
        orders.add(0,newOrderDTO);
        if (orders.size() > NUMBER_OF_ORDERS){
            while (orders.size()>NUMBER_OF_ORDERS){
                orders.remove(orders.size()-1);
            }
        }

        jsonString = jsonString.substring(1);
        String result = "{\"action\":\"ORDER_CREATED\"," + jsonString;
        LOGGER.info("Class:" + this.getClass() + " out from addNewOrderToList() method, result:" + result);
        return result;
    }

    // todo: may be not String but boolean
    private String changeOrderDataInList(String messageFromServer){
        LOGGER.info("Class:" + this.getClass() + " metod: changeOrderDataInList() invoked.");
        if (messageFromServer == null || messageFromServer.length() == 0) return null;
        String jsonString = messageFromServer.substring(13);
        LOGGER.info("Class:" + this.getClass() + " metod: changeOrderDataInList() " + " jsonString processing: " + jsonString);
        Gson gson = new Gson();
        OrderDTO newOrderDTO = gson.fromJson(jsonString, OrderDTO.class);
        if (newOrderDTO == null) return null;
        int pos = 0;
        boolean found = false;
        for(OrderDTO dto: orders){
            if (dto.getId().equals(newOrderDTO.getId())){
                found = true;
                break;
            }
            pos++;
        }
        if (found){
            orders.remove(pos);
            orders.add(pos, newOrderDTO);
        }
        jsonString = jsonString.substring(1);
        String result = "{\"action\":\"ORDER_UPDATED\"," + jsonString;
        LOGGER.info("Class:" + this.getClass() + " out from changeOrderDataInList() method, result:" + result);
        return result;
    }

    // todo: may be not String but boolean
    private String deleteOrderDataInList(String messageFromServer){
        LOGGER.info("Class:" + this.getClass() + " metod: deleteOrderDataInList() invoked.");
        if (messageFromServer == null || messageFromServer.length() == 0) return null;
        String jsonString = messageFromServer.substring(14);
        LOGGER.info("Class:" + this.getClass() + " metod: deleteOrderDataInList() " + " jsonString processing: " + jsonString);
        Gson gson = new Gson();
        OrderDTO newOrderDTO = gson.fromJson(jsonString, OrderDTO.class);
        LOGGER.info("Class:" + this.getClass() + " metod: deleteOrderDataInList() " + " newOrderDTO " + newOrderDTO);
        if (newOrderDTO == null) {
            LOGGER.info("Class:" + this.getClass() + " out from deleteOrderDataInList() method: orderDTO is null");
            return null;
        }
        int pos = 0;
        boolean found = false;
        if (orders == null){
            LOGGER.info("Class:" + this.getClass() + " out from deleteOrderDataInList() method: orders is null");
            return null;
        }
        for(OrderDTO dto: orders){
            if (dto.getId().equals(newOrderDTO.getId())){
                found = true;
                break;
            }
            pos++;
        }
        if (found){
            orders.get(pos).setStatus("DELETED");
        }
        jsonString = jsonString.substring(1);
        String result = "{\"action\":\"ORDER_DELETED\"," + jsonString;
        LOGGER.info("Class:" + this.getClass() + " out from deleteOrderDataInList() method, result:" + result);
        return result;
    }

    private String refreshStatsFields(String messageFromServer){
        LOGGER.info("Class:" + this.getClass() + " metod: refreshStatsFields() invoked.");
        if (messageFromServer == null || messageFromServer.length() == 0) return null;
        int firstSpacePos = messageFromServer.indexOf(' ');
        String jsonString = messageFromServer.substring(firstSpacePos+1);
        LOGGER.info("Class:" + this.getClass() + " metod: refreshStatsFields() " + " jsonString processing: " + jsonString);
        System.out.println("refreshStatsFields, jsonString=" + jsonString);
        Gson gson = new Gson();
//        StatsDTO statsFromJson = gson.fromJson(jsonString, StatsDTO.class);
//        statsDTO.setNumOfTrucksTotal(statsFromJson.getNumOfTrucksTotal());
//        statsDTO.setNumOfTrucksFree(statsFromJson.getNumOfTrucksFree());
//        statsDTO.setNumOfTrucksNotReady(statsFromJson.getNumOfTrucksNotReady());
//        statsDTO.setNumOfTrucksExecutingOrders(statsFromJson.getNumOfTrucksExecutingOrders());
//        statsDTO.setNumOfDriversTotal(statsFromJson.getNumOfDriversTotal());
//        statsDTO.setNumOfDriversFree(statsFromJson.getNumOfDriversFree());
//        statsDTO.setNumOfDriversExecutingOrders(statsFromJson.getNumOfDriversExecutingOrders());
        refreshStatsFieldsFromWebService();
        jsonString = jsonString.substring(1);
        String result = "{\"action\":\"STATS_UPDATED\"," + jsonString;
        LOGGER.info("Class:" + this.getClass() + " out from refreshStatsFields() method, result:" + result);
        return result;
    }

    private String refreshStatsFieldsFromWebService(){
        LOGGER.info("Class:" + this.getClass() + " metod: refreshStatFieldsFromWebService() invoked.");
        Client client = Client.create();
        WebResource webResource = client.resource("http://localhost:8080/worldwidelogistics/stats");
        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        if (response.getStatus()!=200){
            System.out.println("failed");
        }
        LOGGER.info("Class:" + this.getClass() + " metod: refreshStatFieldsFromWebService(), server responce status = " + response.getStatus());
        String statsFromWS = response.getEntity(String.class);
        LOGGER.info("Class:" + this.getClass() + " metod: refreshStatFieldsFromWebService(), statsFromWebService = " + statsFromWS);
        Gson gson = new Gson();
        StatsDTO statsFromJson = gson.fromJson(statsFromWS, StatsDTO.class);
        statsDTO = new StatsDTO();
        statsDTO.setNumOfTrucksTotal(statsFromJson.getNumOfTrucksTotal());
        statsDTO.setNumOfTrucksFree(statsFromJson.getNumOfTrucksFree());
        statsDTO.setNumOfTrucksNotReady(statsFromJson.getNumOfTrucksNotReady());
        statsDTO.setNumOfTrucksExecutingOrders(statsFromJson.getNumOfTrucksExecutingOrders());
        statsDTO.setNumOfDriversTotal(statsFromJson.getNumOfDriversTotal());
        statsDTO.setNumOfDriversFree(statsFromJson.getNumOfDriversFree());
        statsDTO.setNumOfDriversExecutingOrders(statsFromJson.getNumOfDriversExecutingOrders());
        LOGGER.info("Class:" + this.getClass() + " out from refreshStatFieldsFromWebService() method");
        return "{\"action\":\"STATS_UPDATED\"}";
    }


    private String initStatsFieldsFromWebService(){
        return getDataFromWebService(STATS_RESOURCE_URL);
    }



    private String  initOrdersListFromWebService() {
        return getDataFromWebService(ORDERS_RESOURCE_URL);
    }

    private String getDataFromWebService(String ordersResourceUrl) {
        Client client = Client.create();
        WebResource webResource = client.resource(ordersResourceUrl);
        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        if (response.getStatus()!=200){
            System.out.println("failed");
        }
        String output = response.getEntity(String.class);
        System.out.println("OUTPUT STRING = " + output);
        return output;
    }

}