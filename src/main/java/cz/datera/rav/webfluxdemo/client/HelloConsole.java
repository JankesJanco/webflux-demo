package cz.datera.rav.webfluxdemo.client;

import cz.datera.rav.webfluxdemo.hello.Greeting;
import cz.datera.rav.webfluxdemo.hello.UserResponse;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Scheduled tasks for calling Hello API and writing progress/result to console.
 */
@Service
public class HelloConsole {

    private final HelloApiClient apiClient;

    private final Logger logger = LoggerFactory.getLogger(HelloConsole.class);

    private final Random random = new Random();

    private final String[] names = {"Fred", "Jane", "Miss America"};
    private final String[] cities = {"London", "Prague", "New York"};

    public HelloConsole(HelloApiClient client) {
        apiClient = client;
    }

//    @Scheduled(fixedDelay = 10000L, initialDelay = 7000L)
    public void sayHelloToConsole() throws InterruptedException {
        Mono<Greeting> greetingResponse = apiClient.getHello("Miro");

        logger.info("Requesting greeting for Miro");
        greetingResponse.subscribe(greeting -> {
            logger.info("Obtained greeting: " + greeting.getGreeting() + "\n\n");
        });
    }

    @Scheduled(fixedDelay = 10000L, initialDelay = 7000L)
    public void sayHelloToConsoleAlternative() throws InterruptedException {
        Mono<Greeting> greetingResponse = apiClient.getHello("Miro");

        logger.info("Requesting greeting for Miro");
        // map() method is one of possible Mono publisher transformation methods. In these specific case is map() equal
        // to identity.
        greetingResponse.map(greeting -> {
            logger.info("Obtained greeting: " + greeting.getGreeting() + "\n\n");
            return greeting;
        }).block(); // we have to call block() method to subscribe to the publisher. Otherwise no request to Hello API will be made.
    }

    @Scheduled(fixedDelay = 10000L, initialDelay = 4000L)
    public void registerUser() {
        String name = getRandomName();
        String city = getRandomCity();

        Mono<UserResponse> response = apiClient.registerUser(name, city);
        logger.info(String.format("Requesting registration for %s from %s", name, city));
        response.subscribe(userResponse -> {
            logger.info(String.format("User registered: id = %d, name = %s, city = %s\n", userResponse.getId(),
                    userResponse.getName(), userResponse.getCity()));
        });
    }

    @Scheduled(fixedDelay = 10000L, initialDelay = 1000L)
    public void printRegisteredUsersToConsoleRaw() {
        Mono<String> response = apiClient.getRegisteredUsersRaw();

        logger.info("Requesting list of registered users.");
        response.subscribe(registeredUsersRaw -> {
            logger.info("Registered users raw response: " + registeredUsersRaw + "\n");
        });
    }

    @Scheduled(fixedDelay = 10000L, initialDelay = 1000L)
    public void printRegisteredUsersToConsole() {
        Flux<UserResponse> response = apiClient.getRegisteredUsers();

        logger.info("Requesting list of registered users and filtering only London users.");
        response.subscribe(userResponse -> {
            String message = String.format("%s from %s (id=%d)", userResponse.getName(), userResponse.getCity(),
                    userResponse.getId());
            logger.info(message);
        });
    }

    @Scheduled(fixedDelay = 10000L, initialDelay = 1000L)
    public void printRegisteredUsersFromLondonToConsole1() {
        Flux<UserResponse> response = apiClient.getRegisteredUsers();

        logger.info("Requesting list of registered users and filtering only London users.");
        response.filter(userResponse -> "London".equals(userResponse.getCity()))
                .map(userResponse -> String.format("%s from %s (id=%d)", userResponse.getName(), userResponse.getCity(),
                userResponse.getId()))
                .subscribe(message -> {
                    logger.info(message);
                });
    }

    /**
     * Get registered London users. Should print same results as printRegisteredUsersFromLondonToConsole1().
     */
    @Scheduled(fixedDelay = 10000L, initialDelay = 1000L)
    public void printRegisteredUsersFromLondonToConsole2() {
        Flux<UserResponse> response = apiClient.getRegisteredUsers();

        logger.info("Requesting list of registered users.");
        response.subscribe(userResponse -> {
            if ("London".equals(userResponse.getCity())) {
                String message = String.format("%s from %s (id=%d)", userResponse.getName(), userResponse.getCity(),
                        userResponse.getId());
                logger.info(message);
            }
        });
    }

    private String getRandomName() {
        int index = random.nextInt(names.length);
        return names[index];
    }

    private String getRandomCity() {
        int index = random.nextInt(cities.length);
        return cities[index];
    }

}
