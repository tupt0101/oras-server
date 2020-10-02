package capstone.oras.account.controller;

import capstone.oras.account.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @Autowired
    private IAccountService accountService;

    @RequestMapping(value = "/login")
    @ResponseBody
    String getAllJob() {
        String token = accountService.login("admin@gmail.com","123456");
        return token;
    }


}
