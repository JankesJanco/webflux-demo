package cz.datera.rav.webfluxdemo.client;

import cz.datera.rav.webfluxdemo.hello.Greeting;
import cz.datera.rav.webfluxdemo.hello.RegisterUserRequest;
import cz.datera.rav.webfluxdemo.hello.UserResponse;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

/**
 * Service for constructing HTTP requests to the Hello API.
 */
@Service
public class HelloApiClient {

    private final WebClient webClient;

    public HelloApiClient(WebClient.Builder builder) {
        final HttpClient httpClient
                = HttpClient.create().keepAlive(true);

        final ReactorClientHttpConnector connector
                = new ReactorClientHttpConnector(httpClient);

        webClient = builder.baseUrl("http://localhost:8080")
                .clientConnector(connector)
                .defaultHeader(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<Greeting> getHello(String name) {
        Map<String, String> pathVariables = new HashMap();
        pathVariables.put("name", name);
        
        return getMono("/api/v1/hello/greeting/{name}", pathVariables, null, Greeting.class);
    }
    
    public Mono<UserResponse> registerUser(String name, String city) {
        return postMono("/api/v1/hello/register", null, null, new RegisterUserRequest(name, city), UserResponse.class);
    }
    
    public Mono<String> getRegisteredUsersRaw() {
        return getMono("/api/v1/hello/registered-users", null, null, String.class);
    }

    /**
     * Helper method for constructing GET request.
     * 
     * Note: similar method getFlux() can be constructed when you are expecting array of items in response body.
     * 
     * @param <P> class of data from response body
     * @param pathTemplate
     * @param pathVariables
     * @param queryParams
     * @param responseType class of data from response body
     * @return 
     */
    private <P> Mono<P> getMono(String pathTemplate, Map<String, String> pathVariables, MultiValueMap<String, 
            String> queryParams, Class<P> responseType) {
        return webClient.get()
                .uri(uriBuilder -> {
                    return uriBuilder
                            .path(pathTemplate)
                            .queryParams(queryParams)
                            .build(pathVariables == null ? new HashMap() : pathVariables);
                })
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(), 
                        response -> response.createException())
                .bodyToMono(responseType);
    }
    
    /**
     * Help request for constructing POST request.
     * 
     * Note: similar method postFlux() can be constructed when you are expecting array of items in response body.
     * 
     * @param <P> class of data from response body
     * @param pathTemplate
     * @param pathVariables
     * @param queryParams
     * @param body
     * @param responseType class of data from response body
     * @return 
     */
    private <P> Mono<P> postMono(String pathTemplate, Map<String, String> pathVariables, MultiValueMap<String, String> 
            queryParams, Object body, Class<P> responseType) {
        
        return webClient.post()
                .uri(uriBuilder -> {
                    return uriBuilder
                            .path(pathTemplate)
                            .queryParams(queryParams)
                            .build(pathVariables == null ? new HashMap() : pathVariables);
                })
                .bodyValue(body)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(), 
                        response -> response.createException())
                .bodyToMono(responseType);
    }
}
