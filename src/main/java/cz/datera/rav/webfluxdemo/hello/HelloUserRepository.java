package cz.datera.rav.webfluxdemo.hello;

import org.springframework.data.repository.CrudRepository;

/**
 * HelloUser CRUD interface for querying database.
 */
public interface HelloUserRepository extends CrudRepository<HelloUser, Long>{
    
}
