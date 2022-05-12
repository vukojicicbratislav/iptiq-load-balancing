package com.iptiq.strategy;

public abstract class LoadBalancerStrategy {

    public abstract int nextProviderIndex(int registeredProvidersSize);
}
