package cz.datera.rav.webfluxdemo.hello;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class HelloUser implements Model {
    
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 4941816829584244318L;

    /**
     * Unique identifier.
     */
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private long id;

    /**
     * Name of the user.
     */
    @Column(name = "NAME")
    private String name;
    
    /**
     * City where the user lives.
     */
    @Column(name = "CITY")
    private String city;

    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", updatable = false)
    private Date createdAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
}
