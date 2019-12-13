package cz.datera.rav.webfluxdemo.hello;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Provides API endpoints for greeting and registering users.
 */
@RestController
@RequestMapping("api/v1/hello")
public class HelloResource {
    
    private final HelloService helloService;
    
    private final Logger logger = LoggerFactory.getLogger(HelloResource.class);
    
    public HelloResource(HelloService helloService) {
        this.helloService = helloService;
    }
    
    @GetMapping("/greeting/{name}")
    public Mono<Greeting> greeting(@PathVariable String name) {
        logger.info("Processing request /api/v1/hello/greeting/{name}");
        
        Greeting g = new Greeting("Hello " + name + "!");
        return Mono.just(g);
    }

    @PostMapping("/register")
    public Mono<UserResponse> registerUserForGreeting(@RequestBody RegisterUserRequest request) {
        logger.info("Processing request /api/v1/hello/register");

        return helloService.registerUser(request.getName(), request.getCity());
    }
    
    @GetMapping("/registered-users")
    public Flux<UserResponse> getRegisteredUsers() {
        logger.info("Processing request /api/v1/hello/registered-users");

        return helloService.getRegisteredUsers();
    }

    @GetMapping("/greet-registered-users")
    public Mono<List<String>> greetRegisteredUsers() {
        // The comment below is only technical detail.
        // we want to return list of strings. The natural choice would be Flux<String> but Spring WebFlux RestController
        // will not convert Flux<String> to json array of strings. Instead it only returns concatenated strings. Check the
        // info-note here https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux-codecs-jackson
        logger.info("Processing request /api/v1/hello/greet-registered-users");

        return helloService.greetRegisteredUsers()
                .collectList(); 
    }
}
