package com.iptiq.strategy.impl;

import com.iptiq.strategy.LoadBalancerStrategy;

import java.util.Random;

public class RandomStrategy extends LoadBalancerStrategy {

    @Override
    public int nextProviderIndex(int registeredProvidersSize) {
        Random r = new Random();
        return r.nextInt(registeredProvidersSize);
    }
}
