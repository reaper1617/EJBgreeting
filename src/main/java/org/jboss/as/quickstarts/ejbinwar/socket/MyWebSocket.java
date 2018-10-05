package org.jboss.as.quickstarts.ejbinwar.socket;


import javax.ejb.Stateless;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

//@Stateless
@ServerEndpoint("/myendpoint")
public class MyWebSocket {

    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session peer){
        System.out.println("OnOpen-server side");
        peers.add(peer);
    }

    @OnClose
    public  void onClose(Session peer){
        System.out.println("OnClose-server side");
        peers.remove(peer);
    }

    @OnMessage
    public void onMessage(Session session, String msg){
        try{
            System.out.println("MyWebSocket: on message!");
            session.getBasicRemote().sendText("From MyWebSocket with love!");
        }
        catch (IOException e){
            e.printStackTrace();
        }
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
