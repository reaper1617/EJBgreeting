package org.jboss.as.quickstarts.ejbinwar.ejb;


import javax.ejb.Stateful;
import javax.ejb.Stateless;

@Stateless
public class MyStatelessEJB {


    public String getMessage(){
        String statelessBeanMessage = "statelessBeanMsg";
        return statelessBeanMessage;
    }
}
