package capstone.oras.api.accountPackage.controller;


import capstone.oras.api.company.service.ICompanyService;
import capstone.oras.api.accountPackage.service.IAccountPackageService;
import capstone.oras.api.packages.service.IPackageService;
import capstone.oras.api.purchase.service.IPurchaseService;
import capstone.oras.entity.AccountPackageEntity;
import capstone.oras.entity.PurchaseEntity;
import capstone.oras.entity.model.PurchaseAccountPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@RestController
@CrossOrigin(value = "http://localhost:9527")
@RequestMapping(value = "/v1/account-package-management")
public class AccountPackageController {

    @Autowired
    private IAccountPackageService accountPackageService;

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private IPackageService packageService;

    @Autowired
    private IPurchaseService purchaseService;

    HttpHeaders httpHeaders = new HttpHeaders();

    @RequestMapping(value = "/account-package", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<AccountPackageEntity> createaccountPackage(@RequestBody PurchaseAccountPackage purchaseAccountPagkage) {
        if (purchaseAccountPagkage.getAccountPackageEntity().getPackageId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Package Id is empty");
        } else if (purchaseAccountPagkage.getAccountPackageEntity().getPurchaseId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Purchase Id is empty");
        } else if (purchaseAccountPagkage.getAccountPackageEntity().getValidTo() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valid To is empty");
        } else if (purchaseService.findPurchaseById(purchaseAccountPagkage.getAccountPackageEntity().getPurchaseId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Purchase Id doesn't exist");
        } else if (packageService.findPackageById(purchaseAccountPagkage.getAccountPackageEntity().getPackageId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Package Id doesn't exist");
        }
        PurchaseEntity purchaseEntity = purchaseService.createPurchase(purchaseAccountPagkage.getPurchaseEntity());
        AccountPackageEntity accountPackage = new AccountPackageEntity();
        accountPackage = purchaseAccountPagkage.getAccountPackageEntity();
        accountPackage.setPurchaseId(purchaseEntity.getId());
        accountPackage = accountPackageService.createAccountPackage(accountPackage);
        return new ResponseEntity<>(accountPackage, HttpStatus.OK);
    }

    @RequestMapping(value = "/account-package", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<AccountPackageEntity> updateAccountPackage(@RequestBody AccountPackageEntity accountPackageEntity) {
        if (accountPackageEntity.getPackageId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Package Id is empty");
        } else if (accountPackageEntity.getPurchaseId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Purchase Id is empty");
        } else if (accountPackageEntity.getValidTo() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valid To is empty");
        } else if (purchaseService.findPurchaseById(accountPackageEntity.getPurchaseId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Purchase Id doesn't exist");
        } else if (packageService.findPackageById(accountPackageEntity.getPackageId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Package Id doesn't exist");
        }
        return new ResponseEntity<>(accountPackageService.updateAccountPackage(accountPackageEntity), HttpStatus.OK);

    }

    @RequestMapping(value = "/account-packages", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<AccountPackageEntity>> getAllAccountPackage() {
        List<AccountPackageEntity> lst = accountPackageService.getAllAccountPackage();
        lst.sort(Comparator.comparingInt(AccountPackageEntity::getAccountId));
        return new ResponseEntity<List<AccountPackageEntity>>(lst, HttpStatus.OK);
    }


    @RequestMapping(value = "/account-package/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<AccountPackageEntity> getAccountPackageById(@PathVariable("id") int id) {
        return new ResponseEntity<AccountPackageEntity>(accountPackageService.findAccountPackageById(id), HttpStatus.OK);
    }

}
