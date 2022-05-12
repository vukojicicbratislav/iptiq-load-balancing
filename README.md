# iptiq-load-balancing
Load balancing technical assignment

[How to start]
* run `main` method
* interact with console using predefined commands
  * `get` - simulate manual call to LoadBalancer 
  * `reg {providerIdentifier}` - Register new Provider
  * `unreg {providerIdentifier}` - Unregister existing provider
  * `exit` - stop the application

*Note*
* Current main method attempts to assign 4 providers to load balancer and 2 is maximum allowed number.
* `requestSimulation.start(5);` in `main` method sets expected number of concurrent requests to be executed against LoadBalancer
