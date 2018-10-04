package org.jboss.as.quickstarts.ejbinwar.ejb;


import javax.ejb.Stateful;
import javax.ejb.Stateless;

@Stateless
public class MyStatelessEJB {

    private static String statelessBeanMessage = "statelessBeanMsg";



    public String getMessage(){
        return statelessBeanMessage;
    }
}
