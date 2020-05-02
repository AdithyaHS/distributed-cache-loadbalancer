package com.distributedsystems.loadbalancer;

import com.distributedsystems.loadbalancer.Utilities.ControllerConfigurations;
import com.distributedsystems.loadbalancer.clients.ClientStubs;
import com.distributedsystems.loadbalancer.clients.ControllerServiceGrpc;
import com.distributedsystems.loadbalancer.dto.ClientRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class Test {

    @Autowired
    ControllerConfigurations appConfig;

    @RequestMapping(value = "/", method = RequestMethod.POST,consumes ="application/json")
    @ResponseBody
    public String index(@RequestBody ClientRequest clientRequest) {
        ClientStubs clientStubs = ClientStubs.getInstance();
        ArrayList<ControllerServiceGrpc.ControllerServiceBlockingStub> controllerServiceBlockingStubs = clientStubs.getStubs();
        System.out.println(controllerServiceBlockingStubs);
        return "Greetings from Spring Boot!" + clientRequest.getKey();
    }



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
