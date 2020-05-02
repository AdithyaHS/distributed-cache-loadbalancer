package com.distributedsystems.loadbalancer.clients;


import com.distributedsystems.loadbalancer.configuration.Configuration;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClientStubs {

    private static final Logger logger = LoggerFactory.getLogger(ClientStubs.class);

    private static ClientStubs ourInstance = new ClientStubs();

    private ArrayList<ControllerServiceGrpc.ControllerServiceBlockingStub> controllerServiceBlockingStubs =
            new ArrayList<ControllerServiceGrpc.ControllerServiceBlockingStub>();


    /**
     * @description creates clients stubs
     */
    private ClientStubs() {
        HashMap<String, String> config = Configuration.getInstance().readConfig();

        logger.debug(config.toString());


        for (Map.Entry<String, String> entry : config.entrySet()) {

            String[] address = entry.getValue().split(":");

            ManagedChannel channel = ManagedChannelBuilder
                    .forAddress(address[0].trim(), Integer.valueOf(address[1]))
                    .usePlaintext()
                    .build();

            ControllerServiceGrpc.ControllerServiceBlockingStub stub = ControllerServiceGrpc.newBlockingStub(channel);


            controllerServiceBlockingStubs.add(stub);
        }
    }


    public static ClientStubs getInstance() {
        return ourInstance;
    }

    public ArrayList<ControllerServiceGrpc.ControllerServiceBlockingStub> getStubs() {
        return controllerServiceBlockingStubs;
    }
}
