package com.iptiq.simulation;

import com.iptiq.model.LoadBalancer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RequestSimulation implements Runnable {

    public static final int INITIAL_DELAY = 3;
    public static final int FREQUENCY = 10;
    private LoadBalancer loadBalancer;

    public RequestSimulation(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Load balancer returned: " + loadBalancer.get());
    }

    public void start(int numberOfConcurrentRequests){
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(numberOfConcurrentRequests);
        for (int i = 0; i <= numberOfConcurrentRequests; i++) {
            RequestSimulation requestSimulation = new RequestSimulation(loadBalancer);
            executorService.scheduleWithFixedDelay(requestSimulation, INITIAL_DELAY, FREQUENCY, TimeUnit.SECONDS);
        }
    }
}
