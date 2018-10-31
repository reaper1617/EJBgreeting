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
        LOGGER.info("MyWebSocket: in onOpen method");
        peers.add(peer);
    }

    @OnClose
    public  void onClose(Session peer){
        LOGGER.info("MyWebSocket: in onMClose method");
        peers.remove(peer);
    }

    @OnMessage
    public void onMessage(Session session, String msg){
        LOGGER.info("MyWebSocket: in onMessage method");
        try{
            LOGGER.info("Class:" + this.getClass() + " method: onMessage, message =  " + msg);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Throwable t){
        LOGGER.error("Class:" + this.getClass() + " method: onError!" + t.getLocalizedMessage());
    }

}
