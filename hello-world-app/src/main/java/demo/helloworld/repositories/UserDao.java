package demo.helloworld.repositories;

import demo.helloworld.entities.User;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.Id;

public interface UserDao extends CrudRepository<User, Id> {
}
