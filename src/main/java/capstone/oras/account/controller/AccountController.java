package capstone.oras.account.controller;

import capstone.oras.account.service.IAccountService;
import capstone.oras.entity.AccountEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/v1/account-mananagement")
public class AccountController {

    @Autowired
    private IAccountService accountService;

    HttpHeaders httpHeaders = new HttpHeaders();

    @RequestMapping(value = "/account", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<AccountEntity> createAccount(@RequestBody AccountEntity accountEntity) {
        if (accountEntity.getEmail() == null || accountEntity.getEmail().isEmpty()) {
            httpHeaders.set("error", "Email is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (accountEntity.getFullname() == null || accountEntity.getFullname().isEmpty()) {
            httpHeaders.set("error", "Fullname is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (accountEntity.getPassword() == null || accountEntity.getPassword().isEmpty()) {
            httpHeaders.set("error", "Password is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (accountService.findAccountByEmail(accountEntity.getEmail()) != null) {
            httpHeaders.set("error", "This email is already registered");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (accountService.findAccountEntityById(accountEntity.getId()) != null) {
            httpHeaders.set("error", "Account already exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(accountService.createAccount(accountEntity), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/account", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<AccountEntity> updateAccount(@RequestBody AccountEntity accountEntity) {

        if (accountEntity.getEmail() == null || accountEntity.getEmail().isEmpty()) {
            httpHeaders.set("error", "Email is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (accountEntity.getFullname() == null || accountEntity.getFullname().isEmpty()) {
            httpHeaders.set("error", "Fullname is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (accountEntity.getPassword() == null || accountEntity.getPassword().isEmpty()) {
            httpHeaders.set("error", "Password is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (accountService.findAccountByEmail(accountEntity.getEmail()) == null) {
            httpHeaders.set("error", "Can not find this account");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (accountService.findAccountEntityById(accountEntity.getId()) == null) {
            httpHeaders.set("error", "Account doesn't exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(accountService.updateAccount(accountEntity), HttpStatus.OK);
        }

    }

    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<AccountEntity>> getAllAccount() {
        return new ResponseEntity<List<AccountEntity>>(accountService.getAllAccount(), HttpStatus.OK);
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<AccountEntity>> test() {
        return new ResponseEntity<List<AccountEntity>>(accountService.getAllAccount(), HttpStatus.OK);
    }
}
