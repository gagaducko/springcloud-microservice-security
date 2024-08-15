package auth.gagaduck.userauthservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menu")
public class TestController {

    @GetMapping("/isMenuForUser")
    public Boolean test() {
        System.out.println("test api successfully!");
        return true;
    }
}
