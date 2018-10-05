package org.jboss.as.quickstarts.ejbinwar.controller;

import com.rabbitmq.client.*;
import org.jboss.as.quickstarts.ejbinwar.dto.OrderDTO;
import org.jboss.as.quickstarts.ejbinwar.ejb.GreeterEJB;
import org.jboss.as.quickstarts.ejbinwar.ejb.MyStatelessEJB;
import org.jboss.as.quickstarts.ejbinwar.socket.MyWebSocket;

import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Named("greeter")
@SessionScoped
public class Greeter implements Serializable {

    /** Default value included to remove warning. **/
    private static final long serialVersionUID = 1L;
    private final static String QUEUE_NAME = "hello";


    /** for rabbitmq **/
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;

    /**
     * Injected GreeterEJB client
     */
    @EJB
    private GreeterEJB greeterEJB;

    @EJB
    private MyStatelessEJB myStatelessEJB;


//    @EJB
//    private MyWebSocket myWebSocket;

    public Greeter() {
        initRabbitMQListener();
        //ServerEndpointConfig.Builder.create(MyWebSocket.class, "/myendpoint").build();
    }

    /**
     * Stores the response from the call to greeterEJB.sayHello(...)
     */
    private String message;

    /** collection of orders**/

    private List<OrderDTO> orders;

    /**
     * Invoke greeterEJB.sayHello(...) and store the message
     *
     * @param name The name of the person to be greeted
     */
    public void setName(String name) {
        message = greeterEJB.sayHello(name) + myStatelessEJB.getMessage();
    }

    /**
     * Get the greeting message, customized with the name of the person to be greeted.
     *
     * @return message. The greeting message.
     */
    public String getMessage() {
        return message;
    }

    public List<OrderDTO> getOrders() {
        return orders;
    }

    private void initRabbitMQListener(){
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
                    String msg = new String(body, "UTF-8");
                    message = msg;
                    orders = generateList();
                   // myStatelessEJB.setStatelessBeanMessage(msg);
                    System.out.println(" [x] Received '" + msg + "'");
                }
            };
        channel.basicConsume(QUEUE_NAME, true, consumer);
        }
        catch (Exception e){
            e.printStackTrace();
            initRabbitMQListener();
        }
    }


    public List<OrderDTO> generateList(){
        List<OrderDTO> result = new ArrayList<OrderDTO>();

        int size = new Random().nextInt(10) + 1;

        for(int i = 0; i < size; i++){
            result.add(new OrderDTO("id", "pNum", "descr","Status"));
        }
        return result;
    }

}