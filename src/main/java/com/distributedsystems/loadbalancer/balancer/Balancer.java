package com.distributedsystems.loadbalancer.balancer;

import com.distributedsystems.loadbalancer.Utilities.ControllerConfigurations;
import org.springframework.beans.factory.annotation.Autowired;

public class Balancer {
    @Autowired
    ControllerConfigurations appConfig;

    public void balance(){

        int controllerFromList = ((int) (Math.random()*(appConfig.numControllers))) + 1 ; // to randomly select one of the controllers from config file

        if(appConfig.consistencyLevel.equals("causal")){
            String ip = "server1";
            System.out.println(ip);
        } else {
            String ip = "server" + Integer.toString(controllerFromList);
            System.out.println(ip);
        }
    }
}
