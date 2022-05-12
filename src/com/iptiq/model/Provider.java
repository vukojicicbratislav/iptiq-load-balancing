package com.iptiq.model;

import com.iptiq.util.Randomizer;

import java.util.Random;

public class Provider {

    String identifier;

    public Provider() {
        this.identifier = Randomizer.generateRandomString();
    }

    public Provider(String providerIdentifier) {
        this.identifier = providerIdentifier;
    }

    public String get() {
        return this.identifier;
    }

    public boolean check() {
        Random random = new Random();
        boolean isHealthy = random.nextBoolean();
        System.out.println(Thread.currentThread().getName() + ": Provider: " + this.identifier + " is isHealthy: " + isHealthy);
        return isHealthy;
    }
}
