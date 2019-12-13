package cz.datera.rav.webfluxdemo.hello;

public class RegisterUserRequest {

    /**
     * Name of a user.
     */
    private String name;
    
    /**
     * City where the user lives.
     */
    private String city;
    
    public RegisterUserRequest() {
        
    }
    
    public RegisterUserRequest(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
}
