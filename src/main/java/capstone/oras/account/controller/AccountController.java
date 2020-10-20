package capstone.oras.account.controller;

import capstone.oras.account.service.IAccountService;
import capstone.oras.entity.AccountEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    AccountEntity createAccount(@RequestBody AccountEntity accountEntity) {
        return accountService.createAccount(accountEntity);
    }

    @RequestMapping(value = "/account", method = RequestMethod.PUT)
    @ResponseBody
    AccountEntity updateAccount(@RequestBody AccountEntity accountEntity) {
        return accountService.updateAccount(accountEntity);
    }
    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    @ResponseBody
    List<AccountEntity> getAllAccount() {
        return accountService.getAllAccount();
    }
}
