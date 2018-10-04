//package org.jboss.as.quickstarts.ejbinwar.rabbitmq;
//
//import com.rabbitmq.client.*;
//
//import javax.ejb.Singleton;
//import javax.jms.Message;
//import javax.jms.MessageListener;
//import java.io.IOException;
//
//
//@Singleton
//public class RabbitMQReceiverImpl implements MessageListener {
//
//    private final static String QUEUE_NAME = "hello";
//
//    private ConnectionFactory factory;
//    private Connection connection;
//    private Channel channel;
//    private void init(){
//        factory = new ConnectionFactory();
//        factory.setHost("localhost");
//        try {
//            connection = factory.newConnection();
//            channel = connection.createChannel();
//            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
//
//            Consumer consumer = new DefaultConsumer(channel) {
//                @Override
//                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
//                        throws IOException {
//                    String message = new String(body, "UTF-8");
//                    System.out.println(" [x] Received '" + message + "'");
//                }
//            };
//        channel.basicConsume(QUEUE_NAME, true, consumer);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    public RabbitMQReceiverImpl() {
//        init();
//    }
//
//    @Override
//    public void onMessage(Message message) {
//        System.out.println("Gotcha!" + message);
//    }
//}
