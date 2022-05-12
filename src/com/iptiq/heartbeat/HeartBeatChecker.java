package com.iptiq.heartbeat;

import com.iptiq.model.Provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class HeartBeatChecker implements Runnable {

    private static final long HEALTH_CHECK_FREQUENCY_SECONDS = 10L;
    private static final long HEALTH_CHECK_INITIAL_DELAY_SECONDS = 2L;

    private final List<Provider> registeredProviders;
    private List<Provider> healthyProviders;
    private final Map<Provider, Integer> healthCheckResults = new HashMap<>();

    public HeartBeatChecker(List<Provider> registeredProviders) {
        this.registeredProviders = registeredProviders;
    }

    @Override
    public void run() {
        //check for recovery
        System.out.println(Thread.currentThread().getName() + ": Checking " + registeredProviders.size() + " providers. ");
        registeredProviders.forEach(provider -> {
            if (provider.check()) {
                healthCheckResults.put(provider, healthCheckResults.get(provider) + 1);
            } else {
                healthCheckResults.put(provider, 0);
            }
        });

        this.healthyProviders = healthCheckResults.keySet()
                .stream()
                .filter(provider -> healthCheckResults.get(provider) >= 2)
                .collect(Collectors.toList());

        System.out.println(Thread.currentThread().getName() + ": Remaining healthy " + healthyProviders.size() + " providers. ");
        System.out.println();
    }

    public void start() {
        registeredProviders.forEach(provider -> healthCheckResults.put(provider, 2));
        ScheduledExecutorService executorService;
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this, HEALTH_CHECK_INITIAL_DELAY_SECONDS, HEALTH_CHECK_FREQUENCY_SECONDS, TimeUnit.SECONDS);
    }

    public List<Provider> getHealthyProviders() {
        return this.healthyProviders;
    }
}
