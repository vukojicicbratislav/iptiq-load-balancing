package com.iptiq;

import com.iptiq.model.LoadBalancer;
import com.iptiq.model.Provider;
import com.iptiq.simulation.RequestSimulation;
import com.iptiq.strategy.impl.RoundRobinStrategy;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        LoadBalancer lb = new LoadBalancer(new RoundRobinStrategy());
        //registration
        for (int i = 0; i < 5; i++) {
            lb.register(new Provider());
        }
        lb.startHealthCheck();

        //lb.startCapacityLimitProtection(5);
        RequestSimulation requestSimulation = new RequestSimulation(lb);
        requestSimulation.start(5);

        Scanner console = new Scanner(System.in);
        System.out.println("Enter command: get, register {x}, unregister {x}, stop");
        System.out.println();

        String input;
        String providerIdentifier;
        while (true) {
            input = console.next();
            switch (input) {
                case "reg":
                    System.out.println(Thread.currentThread().getName() + ": Enter provider identifier.");
                    providerIdentifier = console.next();
                    lb.register(new Provider(providerIdentifier));
                    break;
                case "unreg":
                    System.out.println(Thread.currentThread().getName() + ": Enter provider identifier.");
                    providerIdentifier = console.next();
                    lb.unregister(providerIdentifier);
                    break;
                case "get":
                    System.out.println(Thread.currentThread().getName() + ": Getting response from loadBalancer:" + lb.get());
                    break;
                case "exit":
                    System.out.println(Thread.currentThread().getName() + ": Exiting");
                    System.exit(0);
                    break;
                default:
            }
        }
    }
}
