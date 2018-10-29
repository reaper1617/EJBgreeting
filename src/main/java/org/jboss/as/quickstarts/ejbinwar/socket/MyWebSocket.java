package org.jboss.as.quickstarts.ejbinwar.socket;


import javax.enterprise.context.ApplicationScoped;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
@ServerEndpoint("/myendpoint")
public class MyWebSocket {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(MyWebSocket.class);
    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    public static Set<Session> getPeers() {
        return peers;
    }

    @OnOpen
    public void onOpen(Session peer){
        System.out.println("OnOpen-server side");
        LOGGER.info("MyWebSocket: in onOpen method");
        peers.add(peer);
    }

    @OnClose
    public  void onClose(Session peer){
        LOGGER.info("MyWebSocket: in onMClose method");
        System.out.println("OnClose-server side");
        peers.remove(peer);
    }

    @OnMessage
    public void onMessage(Session session, String msg){
        LOGGER.info("MyWebSocket: in onMessage method");
        try{
            System.out.println("MyWebSocket: on message!");
            LOGGER.info("Class:" + this.getClass() + " method: onMessage, message =  " + msg);
            //session.getBasicRemote().sendText("From MyWebSocket with love!");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Throwable t){
        LOGGER.error("Class:" + this.getClass() + " method: onError!" + t.getLocalizedMessage());
    }
//
//    @OnMessage
//    public void onMessage(byte[] msg){
//        System.out.println("MyWebSocket: on message! byte[] type");
//    }

//    @OnMessage
//    public void onMessage(String msg){
//        System.out.println("in onMessage server side! " + msg);
//    }
}
