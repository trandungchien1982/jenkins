package demo.helloworld.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @Column(name = "user_id")
    Long userId;

    @Column(name = "name")
    String name;

    @Column(name = "age")
    Integer age;
}
