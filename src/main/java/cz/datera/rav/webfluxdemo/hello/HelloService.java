package cz.datera.rav.webfluxdemo.hello;

import java.util.Date;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class HelloService {

    private final HelloUserRepository repository;

    private final ModelMapper mapper;

    public HelloService(HelloUserRepository helloUserRepository) {
        repository = helloUserRepository;

        // init model mapper - helper tool for mapping one class to another
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper
                .getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        mapper = modelMapper;
    }

    public Mono<UserResponse> registerUser(String username, String city) {
        return Mono.fromSupplier(() -> {
            HelloUser user = new HelloUser();
            user.setName(username);
            user.setCity(city);
            user.setCreatedAt(new Date());

            user = repository.save(user);

            return mapper.map(user, UserResponse.class);
        });
    }

    public Flux<UserResponse> getRegisteredUsers() {
        return Flux.fromIterable(repository.findAll())
                .map(dbEntity -> mapper.map(dbEntity, UserResponse.class));
    }

    public Flux<String> greetRegisteredUsers() {
        return Flux.fromIterable(repository.findAll())
                .map(dbEntity -> String.format("Hello %s from %s", dbEntity.getName(), dbEntity.getCity()));

    }
}
