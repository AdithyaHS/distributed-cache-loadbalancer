# distributed-cache-loadbalancer

###Steps to run:

#### Build the maven project
```
mvn clean install
```
#### Checkout the controller code

```
git clone https://github.com/AdithyaHS/distributed-cache.git
```

#### Build the controller project

```
mvn clean install
```

#### Start the controllers

```
docker-compose up -d --build
```

#### Start the load balancer

Go to com.distributedsystems.LoadBalancerApplicationTests and run the main function

#### Change the consistency level

Consistency can be changed in application.properties in the resources folder

#### Run the test cases

Run the com.distributedsystems.TestClient
