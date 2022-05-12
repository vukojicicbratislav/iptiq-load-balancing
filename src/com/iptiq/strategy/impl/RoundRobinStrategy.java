package com.iptiq.strategy.impl;

import com.iptiq.strategy.LoadBalancerStrategy;

public class RoundRobinStrategy extends LoadBalancerStrategy {

    private int lastProviderIndex = -1;

    @Override
    public int nextProviderIndex(int registeredProvidersSize) {
        if (registeredProvidersSize == 0) {
            System.out.println(Thread.currentThread().getName() + ": No registered providers.");
            //no registered providers -> returning -1
            return -1;
        } else {
            lastProviderIndex++;
            if (lastProviderIndex == registeredProvidersSize) {
                System.out.println(Thread.currentThread().getName() + ": Time for a new round.");
                lastProviderIndex = 0;
                return lastProviderIndex;
            }
            return lastProviderIndex;
        }
    }
}
