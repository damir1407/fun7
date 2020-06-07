package com.outfit.fun7.api;

import com.outfit.fun7.model.User;
import com.outfit.fun7.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public @ResponseBody String getUser(@RequestParam("id") Long id) {
        User user = this.userService.getUserById(id);
        if(user != null) {
            return user.toString();
        }
        else {
            return "[]";
        }

    }
}
