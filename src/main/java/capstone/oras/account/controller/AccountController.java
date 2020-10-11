package capstone.oras.account.controller;

import capstone.oras.account.service.IAccountService;
import capstone.oras.entity.AccountEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/v1/account-mananagement")
public class AccountController {

    @Autowired
    private IAccountService accountService;

//    @RequestMapping(value = "/login")
//    @ResponseBody
//    String login() {
////        String token = accountService.login("admin@gmail.com","123456");
////        return token;
//
//    }

    @RequestMapping(value = "/account", method = RequestMethod.POST)
    @ResponseBody
    AccountEntity createAccount(String email, String password,String fullname) {
        AccountEntity accountEntity = accountService.createAccount(email, password, fullname);
        return accountEntity;
    }

}
