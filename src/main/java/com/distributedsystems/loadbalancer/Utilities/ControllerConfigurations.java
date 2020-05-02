package com.distributedsystems.loadbalancer.Utilities;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class ControllerConfigurations {
    @org.springframework.beans.factory.annotation.Value("${num.controllers}")
    public int numControllers;
    @org.springframework.beans.factory.annotation.Value("${consistency.level}")
    public String consistencyLevel;
}
