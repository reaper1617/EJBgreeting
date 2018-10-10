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
import org.jboss.as.quickstarts.ejbinwar.ejb.MyStatelessEJB;

import org.jboss.as.quickstarts.ejbinwar.enums.UpdateMessageType;
import org.jboss.as.quickstarts.ejbinwar.socket.MyWebSocket;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.websocket.Session;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.*;


@Named("greeter")
@SessionScoped
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
    @EJB
    private MyStatelessEJB myStatelessEJB;

    private String message;

    private static final int NUMBER_OF_ORDERS = 10;
    private List<OrderDTO> orders;


//    private List<Integer> currentStatsValues;
    // for statistics
    private int numOfTrucksTotal;
    private int numOfTrucksFree;
    private int numOfTrucksNotReady;
    private int numOfTrucksExecutingOrders;
    private int numOfDriversTotal;
    private int numOfDriversFree;
    private int numOfDriversExecutingOrders;

    public Greeter() {
        LOGGER.info("Init" + this.getClass());
        initRabbitMQListener();
        String rest = initOrdersListFromWebService();
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<OrderDTO>>(){}.getType();
        orders = gson.fromJson(rest, collectionType);

        String statsFromWS = initStatsFieldsFromWebService();
        String stats = statsFromWS.substring(1, statsFromWS.length()-1);
        System.out.println("Stats string: " + stats);
//        System.out.println("REST Stats from WS: " + statsFromWS);
       String[] strs = stats.split(",");

       try {
           numOfTrucksTotal = Integer.parseInt(strs[0]);
           numOfTrucksFree = Integer.parseInt(strs[1]);
           numOfTrucksNotReady = Integer.parseInt(strs[2]);
           numOfTrucksExecutingOrders = Integer.parseInt(strs[3]);
           numOfDriversTotal = Integer.parseInt(strs[4]);
           numOfDriversFree = Integer.parseInt(strs[5]);
           numOfDriversExecutingOrders = Integer.parseInt(strs[6]);
       }
       catch (Exception e){
           System.out.println("In except!");
           e.printStackTrace();
       }
//        currentStatsValues = statisticBean.getAsIntegerList();


//        Gson gson2 = new Gson();
//        gson2.fromJson()
    }

    public int getNumOfTrucksTotal() {
        return numOfTrucksTotal;
    }

    public int getNumOfTrucksFree() {
        return numOfTrucksFree;
    }

    public int getNumOfTrucksNotReady() {
        return numOfTrucksNotReady;
    }

    public int getNumOfTrucksExecutingOrders() {
        return numOfTrucksExecutingOrders;
    }

    public int getNumOfDriversTotal() {
        return numOfDriversTotal;
    }

    public int getNumOfDriversFree() {
        return numOfDriversFree;
    }

    public int getNumOfDriversExecutingOrders() {
        return numOfDriversExecutingOrders;
    }

    public void setName(String name) {
        message = greeterEJB.sayHello(name) + myStatelessEJB.getMessage();
    }

    public String getMessage() {
        return message;
    }


    public List<OrderDTO> getOrders() {
        return orders;
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
                    Set<Session> peers = MyWebSocket.getPeers();
                    for(Session peer: peers){
                        peer.getBasicRemote().sendText("Sending msg to peer! " + messageToPeers);
                    }
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
            return addNewOrderToList(message);
        }
        if (message.contains(UpdateMessageType.ORDER_EDITED.toString())){
            //process: find by id? if found - refresh and repaint
            return changeOrderDataInList(message);
        }
        if (message.contains(UpdateMessageType.ORDER_DELETED.toString())){
            //process: ?? add status: deleted , and repaint
            return deleteOrderDataInList(message);
        }
        if (message.contains(UpdateMessageType.DRIVER_CREATED.toString())
        || message.contains(UpdateMessageType.DRIVER_EDITED.toString())
        || message.contains(UpdateMessageType.DRIVER_DELETED.toString())
                ||message.contains(UpdateMessageType.TRUCK_CREATED.toString())
                || message.contains(UpdateMessageType.TRUCK_EDITED.toString())
                || message.contains(UpdateMessageType.TRUCK_DELETED.toString())){
            // process: update statistictable
            return refreshStatFields(message);
        }
        if (message.contains(UpdateMessageType.USER_CREATED.toString())
                || message.contains(UpdateMessageType.USER_EDITED.toString())
                || message.contains(UpdateMessageType.USER_DELETED.toString())){
            //process: define if user is driver
            return refreshStatFields(message);
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
        orders.add(0,newOrderDTO);
        if (orders.size() > NUMBER_OF_ORDERS){
            while (orders.size()>NUMBER_OF_ORDERS){
                orders.remove(orders.size()-1);
            }
        }
        LOGGER.info("Class:" + this.getClass() + " out from addNewOrderToList() method");
        return jsonString;
    }

    // todo: may be not String but boolean
    private String changeOrderDataInList(String messageFromServer){
        LOGGER.info("Class:" + this.getClass() + " metod: changeOrderDataInList() invoked.");
        if (messageFromServer == null || messageFromServer.length() == 0) return null;
        String jsonString = messageFromServer.substring(13);
        LOGGER.info("Class:" + this.getClass() + " metod: changeOrderDataInList() " + " jsonString processing: " + jsonString);
        Gson gson = new Gson();
        OrderDTO newOrderDTO = gson.fromJson(jsonString, OrderDTO.class);
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
        LOGGER.info("Class:" + this.getClass() + " out from changeOrderDataInList() method.");
        return jsonString;
    }

    // todo: may be not String but boolean
    private String deleteOrderDataInList(String messageFromServer){
        LOGGER.info("Class:" + this.getClass() + " metod: deleteOrderDataInList() invoked.");
        if (messageFromServer == null || messageFromServer.length() == 0) return null;
        String jsonString = messageFromServer.substring(14);
        LOGGER.info("Class:" + this.getClass() + " metod: deleteOrderDataInList() " + " jsonString processing: " + jsonString);
        Gson gson = new Gson();
        OrderDTO newOrderDTO = gson.fromJson(jsonString, OrderDTO.class);
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
            orders.get(pos).setStatus("DELETED");
        }
        LOGGER.info("Class:" + this.getClass() + " out from deleteOrderDataInList() method.");
        return jsonString;
    }

    private String refreshStatFields(String messageFromServer){
        LOGGER.info("Class:" + this.getClass() + " metod: refreshStatFields() invoked.");
        if (messageFromServer == null || messageFromServer.length() == 0) return null;
        int firstSpacePos = messageFromServer.indexOf(' ');
        String jsonString = messageFromServer.substring(firstSpacePos+1);
        LOGGER.info("Class:" + this.getClass() + " metod: refreshStatFields() " + " jsonString processing: " + jsonString);
        System.out.println("refreshStatsFields, jsonString=" + jsonString);
        Gson gson = new Gson();
        StatsDTO statsDTO = gson.fromJson(jsonString, StatsDTO.class);
        numOfTrucksTotal = Integer.parseInt(statsDTO.getTrucksTotal());
        numOfTrucksFree = Integer.parseInt(statsDTO.getTrucksFree());
        numOfTrucksNotReady = Integer.parseInt(statsDTO.getTrucksNotReady());
        numOfTrucksExecutingOrders = Integer.parseInt(statsDTO.getTrucksExecOrders());
        numOfDriversTotal = Integer.parseInt(statsDTO.getDriversTotal());
        numOfDriversFree = Integer.parseInt(statsDTO.getDriversFree());
        numOfDriversExecutingOrders = Integer.parseInt(statsDTO.getDriversExecOrders());
        LOGGER.info("Class:" + this.getClass() + " out from refreshStatFields() method.");
        return jsonString;
    }

    private String initStatsFieldsFromWebService(){
        Client client = Client.create();
        WebResource webResource = client.resource("http://localhost:8085/worldwidelogistics/rest/mainservice/stats");
        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        if (response.getStatus()!=200){
            System.out.println("failed");
        }
        String output = response.getEntity(String.class);
        System.out.println("OUTPUT STRING = " + output);
        return output;
    }

    private String  initOrdersListFromWebService() {
        Client client = Client.create();
        WebResource webResource = client.resource("http://localhost:8085/worldwidelogistics/rest/mainservice/orders");
        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        if (response.getStatus()!=200){
            System.out.println("failed");
        }
        String output = response.getEntity(String.class);
        System.out.println("OUTPUT STRING = " + output);
        return output;
    }



//    private List<OrderDTO> initOrdersListFromWebServiceAsList(){
//        ClientConfig clientConfig = new DefaultClientConfig();
//        //clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
//        Client client = Client.create(clientConfig);
//        WebResource webResource = client.resource("http://localhost:8085/worldwidelogistics/rest/mainservice/orders");
//        WebResource.Builder builder = webResource.accept(MediaType.APPLICATION_JSON).header("content-type", MediaType.APPLICATION_JSON);
//        ClientResponse response = builder.get(ClientResponse.class);
//        if (response.getStatus() != 200){
//            // bad
//            System.out.println("Error 200");
//            return null;
//        }
//        GenericType<List<OrderDTO>> generic = new GenericType<List<OrderDTO>>(){
//        };
//        List<OrderDTO> list = response.getEntity(generic);
//        return list;
//    }

}