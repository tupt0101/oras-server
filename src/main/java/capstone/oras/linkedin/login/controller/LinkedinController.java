package capstone.oras.linkedin.login.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/linkedin")
public class LinkedinController {

    @RequestMapping(value = "/authorization_code")
    @ResponseBody
    String recievedAuthorizationCode(@RequestParam("code") String code, @RequestParam("state") String state) {
        System.out.println(code);
        return "ahihi";
    }
}
