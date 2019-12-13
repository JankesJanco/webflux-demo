package cz.datera.rav.webfluxdemo.client;

import cz.datera.rav.webfluxdemo.hello.Greeting;
import cz.datera.rav.webfluxdemo.hello.UserResponse;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class HelloConsole {

    private final HelloApiClient apiClient;

    private final Logger logger = LoggerFactory.getLogger(HelloConsole.class);

    private final Random random = new Random();

    private final String[] names = {"Fred", "Jane", "Miss America"};
    private final String[] cities = {"New York", "London", "Prague"};

    public HelloConsole(HelloApiClient client) {
        apiClient = client;
    }

    @Scheduled(fixedDelay = 10000L, initialDelay = 7000L)
    public void sayHelloToConsole() throws InterruptedException {
        Mono<Greeting> greetingResponse = apiClient.getHello("Miro");

        logger.info("Requesting greeting for Miro");
        greetingResponse.subscribe(greeting -> {
            logger.info("Obtained greeting: " + greeting.getGreeting() + "\n\n");
        });
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
    public void printRegisteredUsersToConsole() {
        Mono<String> response = apiClient.getRegisteredUsersRaw();

        logger.info("Requesting list of registered users.");
        response.subscribe(registeredUsersRaw -> {
            logger.info("Registered users raw response: " + registeredUsersRaw + "\n");
        });
    }

    private String getRandomName() {
        int index = random.nextInt(names.length);
        return names[index];
    }

    private String getRandomCity() {
        int index = random.nextInt(names.length);
        return cities[index];
    }

}
