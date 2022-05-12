package com.iptiq.model;

import com.iptiq.heartbeat.HeartBeatChecker;
import com.iptiq.strategy.LoadBalancerStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadBalancer {

    private static final int MAX_NUM_OF_SUPPORTED_PROVIDERS = 2;
    private static final int MAX_PARALLEL_REQUESTS_PER_PROVIDER = 2;
    private final LoadBalancerStrategy loadBalancerStrategy;
    private List<Provider> registeredProviders = new ArrayList<>();
    private AtomicInteger liveSessionsCount = new AtomicInteger(0);

    private HeartBeatChecker checker;

    public LoadBalancer(LoadBalancerStrategy loadBalancerStrategy) {
        this.loadBalancerStrategy = loadBalancerStrategy;
        this.checker = new HeartBeatChecker(registeredProviders);
    }

    /**
     * Get method to delegate request to providers.
     * @return response from provider
     */
    public String get() {
        updateLiveSessionsCount();

        //using Thread.sleep() for demonstration purposes
        //real life scenario does not need this method
        slowDownExecutionForDemonstration();

        return getResponseFrom(checker.getHealthyProviders());
    }

    /**
     * Used for new provider registration
     * @param provider - a new providert
     */
    public synchronized void register(Provider provider) {
        if (isMaxNumberOfProviders()) {
            registerNew(provider);
        } else {
            System.out.println(Thread.currentThread().getName() + ": Max number of Providers reached. Try again later");
        }
    }

    /**
     * Used for provider de-registration
     * @param providerIdentifier - provider unique identifier
     */
    public synchronized void unregister(String providerIdentifier) {
        Optional<Provider> optional = registeredProviders.stream()
                .filter(provider -> Objects.equals(provider.get(), providerIdentifier))
                .findFirst();

        if (optional.isEmpty()) {
            System.out.println(Thread.currentThread().getName() + ": Provider does not exist!");
        } else {
            if (registeredProviders.remove(optional.get())) {
                System.out.println(Thread.currentThread().getName() + ": Provider with identifier: " + providerIdentifier + " unregistered.");
            } else {
                System.out.println(Thread.currentThread().getName() + ": Provider with identifier: " + providerIdentifier + " not found.");
            }
        }
    }

    private void registerNew(Provider provider) {
        if (isAlreadyRegistered(provider)) {
            System.out.println(Thread.currentThread().getName() + ": Provider with identifier: " + provider.get() + " already registered.");
        } else {
            registeredProviders.add(provider);
            System.out.println(Thread.currentThread().getName() + ": Provider " + provider.get() + " registered!");
        }
    }

    private boolean isAlreadyRegistered(Provider provider) {
        return registeredProviders.contains(provider);
    }

    private boolean isMaxNumberOfProviders() {
        return registeredProviders.size() < MAX_NUM_OF_SUPPORTED_PROVIDERS;
    }

    public void startHealthCheck() {
        checker.start();
    }

    private String getResponseFrom(List<Provider> healthyProviders) {
        //check cluster capacity
        if (isClusterCapacityReached(healthyProviders)) {
            int providerIndex = loadBalancerStrategy.nextProviderIndex(healthyProviders.size());
            liveSessionsCount.decrementAndGet();
            return getResponseFromSpecificProvider(healthyProviders, providerIndex);
        } else {
            liveSessionsCount.decrementAndGet();
            return Thread.currentThread().getName() + ": Mac cluster capacity reached!";
        }
    }

    private boolean isClusterCapacityReached(List<Provider> healthyProviders) {
        return liveSessionsCount.get() <= healthyProviders.size() * MAX_PARALLEL_REQUESTS_PER_PROVIDER;
    }

    private void updateLiveSessionsCount() {
        System.out.println(Thread.currentThread().getName() + ": live sessions: " + liveSessionsCount.get());
        liveSessionsCount.incrementAndGet();
    }

    private String getResponseFromSpecificProvider(List<Provider> healthyProviders, int providerIndex) {
        if (isValidProviderIndex(providerIndex)) {
            return healthyProviders.get(providerIndex).get();
        } else {
            return Thread.currentThread().getName() + ": 0 providers available";
        }
    }

    private boolean isValidProviderIndex(int providerIndex) {
        return providerIndex != -1;
    }

    private void slowDownExecutionForDemonstration() {
        //slow down response
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
