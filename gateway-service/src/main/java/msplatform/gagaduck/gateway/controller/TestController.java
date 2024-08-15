package msplatform.gagaduck.gateway.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RequestMapping("/menu")
@RestController
public class TestController {

    @GetMapping("/**")
    public void getMenuTest() {
        System.out.println("OPTIONS HERE");
        return;
    }
}
