package com.distributedsystems.loadbalancer.controller;

import com.distributedsystems.distributedcache.controller.Controller;
import com.distributedsystems.distributedcache.controller.ControllerServiceGrpc;
import com.distributedsystems.loadbalancer.Utilities.ControllerConfigurations;
import com.distributedsystems.loadbalancer.clients.ClientStubs;
import com.distributedsystems.loadbalancer.dao.ClientWriteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Random;

@RestController
public class LoadBalancerController {

    private static final String READ_REQUEST = "read";
    private static final String WRITE_REQUEST = "write";
    private static final Logger logger = LoggerFactory.getLogger(LoadBalancerController.class);

    @Autowired
    ControllerConfigurations appConfig;

    private ControllerServiceGrpc.ControllerServiceBlockingStub getClientBlockingStub(String typeOfRequest) {

        ClientStubs clientStubs = ClientStubs.getInstance();
        ArrayList<ControllerServiceGrpc.ControllerServiceBlockingStub> controllerServiceBlockingStubs = clientStubs.getStubs();
        ControllerServiceGrpc.ControllerServiceBlockingStub stub;

        if (getConsistencyLevel(appConfig.consistencyLevel).equals(Controller.ConsistencyLevel.CAUSAL)
                && WRITE_REQUEST.equals(typeOfRequest)) {

            stub = controllerServiceBlockingStubs.get(0);
        } else {
            Random random = new Random();
            int controllerFromList = random.nextInt(appConfig.numControllers);
            stub = controllerServiceBlockingStubs.get(controllerFromList);
        }
        return stub;
    }


    @RequestMapping(value = "/put", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public Controller.WriteResponse handlePutValueRequest(@RequestBody ClientWriteRequest clientWriteRequest) {

        ControllerServiceGrpc.ControllerServiceBlockingStub stub = getClientBlockingStub(WRITE_REQUEST);

        logger.debug("key " + clientWriteRequest.getKey() + "value" + clientWriteRequest.getValue());
        logger.debug("lamport clock " + clientWriteRequest.getLamportClock());

        Controller.WriteRequest writeRequest = Controller.WriteRequest.newBuilder()
                .setKey(clientWriteRequest.getKey())
                .setValue(clientWriteRequest.getValue())
                .setConsistencyLevel(getConsistencyLevel(appConfig.consistencyLevel))
                .setTimeStamp(clientWriteRequest.getLamportClock())
                .build();

        logger.info("Calling the stub from load balancer write request for key " + clientWriteRequest.getKey());

        Controller.WriteResponse writeResponse = stub.withWaitForReady().put(writeRequest);
        return writeResponse;
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public Controller.ReadResponse handleGetValueRequest(@RequestBody ClientWriteRequest clientWriteRequest) {

        ControllerServiceGrpc.ControllerServiceBlockingStub stub = getClientBlockingStub(READ_REQUEST);

        Controller.ReadRequest readRequest = Controller.ReadRequest.newBuilder()
                .setConsistencyLevel(getConsistencyLevel(appConfig.consistencyLevel))
                .setKey(clientWriteRequest.getKey())
                .setTimeStamp(clientWriteRequest.getLamportClock())
                .build();

        Controller.ReadResponse readResponse = stub.get(readRequest);
        return readResponse;
    }

    private Controller.ConsistencyLevel getConsistencyLevel(String consistency) {

        switch (consistency) {
            case "sequential":
                return Controller.ConsistencyLevel.SEQUENTIAL;
            case "eventual":
                return Controller.ConsistencyLevel.EVENTUAL;
            case "linearizability":
                return Controller.ConsistencyLevel.LINEARIZABILITY;
            case "causal":
                return Controller.ConsistencyLevel.CAUSAL;
            default:
                return Controller.ConsistencyLevel.DEFAULT;
        }
    }
}
