package demo.helloworld.controllers;

import demo.helloworld.entities.User;
import demo.helloworld.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path="/api/get-users")
    public List<User> listUsers() {
        return userService.getListUsers();
    }

    @PostMapping(path="/api/add-user")
    public String addUsers() {
        return userService.addUser();
    }
}
