package capstone.oras.api.accountPackage.controller;


import capstone.oras.api.accountPackage.service.IAccountPackageService;
import capstone.oras.api.packages.service.IPackageService;
import capstone.oras.api.purchase.service.IPurchaseService;
import capstone.oras.common.CommonUtils;
import capstone.oras.entity.AccountPackageEntity;
import capstone.oras.entity.PurchaseEntity;
import capstone.oras.entity.model.PurchaseAccountPackage;
import capstone.oras.model.custom.ListAccountPackageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static capstone.oras.common.Constant.TIME_ZONE;

@RestController
@CrossOrigin(value = "http://localhost:9527")
@RequestMapping(value = "/v1/account-package-management")
public class AccountPackageController {

    @Autowired
    private IAccountPackageService accountPackageService;

    @Autowired
    private IPackageService packageService;

    @Autowired
    private IPurchaseService purchaseService;

    @RequestMapping(value = "/account-package", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<AccountPackageEntity> createAccountPackage(@RequestBody PurchaseAccountPackage purchaseAccountPagkage) {
        if (purchaseAccountPagkage.getAccountPackageEntity().getPackageId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Package Id is a required field");
        } else if (purchaseAccountPagkage.getAccountPackageEntity().getPurchaseId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Purchase Id is a required field");
        } else if (purchaseAccountPagkage.getAccountPackageEntity().getValidTo() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valid To is a required field");
        } else if (purchaseService.findPurchaseById(purchaseAccountPagkage.getAccountPackageEntity().getPurchaseId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Purchase Id doesn't exist");
        } else if (packageService.findPackageById(purchaseAccountPagkage.getAccountPackageEntity().getPackageId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Package Id doesn't exist");
        }
        PurchaseEntity purchaseEntity = purchaseService.createPurchase(purchaseAccountPagkage.getPurchaseEntity());
        AccountPackageEntity accountPackage = purchaseAccountPagkage.getAccountPackageEntity();
        accountPackage.setPurchaseId(purchaseEntity.getId());
        accountPackage = accountPackageService.createAccountPackage(accountPackage);
        return new ResponseEntity<>(accountPackage, HttpStatus.OK);
    }

    @RequestMapping(value = "/account-package", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<AccountPackageEntity> updateAccountPackage(@RequestBody AccountPackageEntity accountPackageEntity) {
        if (accountPackageEntity.getPackageId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Package Id is a required field");
        } else if (accountPackageEntity.getPurchaseId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Purchase Id is a required field");
        } else if (accountPackageEntity.getValidTo() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valid To is a required field");
        } else if (purchaseService.findPurchaseById(accountPackageEntity.getPurchaseId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Purchase Id doesn't exist");
        } else if (packageService.findPackageById(accountPackageEntity.getPackageId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Package Id doesn't exist");
        }
        return new ResponseEntity<>(accountPackageService.updateAccountPackage(accountPackageEntity), HttpStatus.OK);

    }

    @RequestMapping(value = "/cancel-account-package/{accountId}", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<AccountPackageEntity> cancelCurrentAccountPackage(@PathVariable("accountId")int accountId) {
        AccountPackageEntity accountPackageEntity = accountPackageService.findAccountPackageByAccountId(accountId);
        if(accountPackageEntity == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No current active package");
        }
        accountPackageEntity.setExpired(true);
        return new ResponseEntity<>(accountPackageService.updateAccountPackage(accountPackageEntity), HttpStatus.OK);

    }

    @RequestMapping(value = "/account-packages", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<AccountPackageEntity>> getAllAccountPackage() {
        List<AccountPackageEntity> lst = accountPackageService.getAllAccountPackage();
        if (!CollectionUtils.isEmpty(lst)) {
            lst.sort(Comparator.comparingInt(AccountPackageEntity::getAccountId));
        }
        return new ResponseEntity<>(lst, HttpStatus.OK);
    }

    @RequestMapping(value = "/account-packages-paging", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<ListAccountPackageModel> getAllAccountPackageWithPaging(@RequestParam(value = "numOfElement") Integer numOfElement,
                                                                           @RequestParam(value = "page") Integer page,
                                                                           @RequestParam(value = "sort") String sort,
                                                                           @RequestParam(value = "name") String name,
                                                                           @RequestParam(value = "status") String status,
                                                                           @RequestParam(value = "package") String pkg) {
        Pageable pageable = CommonUtils.configPageable(numOfElement, page, sort);
        return new ResponseEntity<>(accountPackageService.getAllAccountPackageWithPaging(pageable, name, status, pkg), HttpStatus.OK);
    }


    @RequestMapping(value = "/account-package/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<AccountPackageEntity> getAccountPackageById(@PathVariable("id") int id) {
        return new ResponseEntity<>(accountPackageService.findAccountPackageById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/account-package-by-account-id/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<AccountPackageEntity>> getAccountPackagesByAccountId(@PathVariable("id") int id) {
        List<AccountPackageEntity> accountPackageEntities = accountPackageService.findAccountPackagesByAccountId(id);
        if (accountPackageEntities != null) {
            accountPackageEntities.sort(Comparator.comparing(AccountPackageEntity::getValidTo).reversed());
        }
        return new ResponseEntity<>(accountPackageEntities, HttpStatus.OK);
    }

    @RequestMapping(value = "/current-account-package-by-account-id/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<AccountPackageEntity> getAccountPackageByAccountId(@PathVariable("id") int id) {
        return new ResponseEntity<>(accountPackageService.findAccountPackageByAccountId(id), HttpStatus.OK);
    }


    @RequestMapping(value = "/starter-package/{accountId}", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<AccountPackageEntity> createStarterAccountPackage(@PathVariable("accountId") int accountId) {
        List<AccountPackageEntity> accountPackageEntities = accountPackageService.findAccountPackagesByAccountId(accountId);
        if (accountPackageEntities != null) {
            for (int i = 0; i < accountPackageEntities.size(); i++) {
                if (accountPackageEntities.get(i).getPackageId() == 1) {
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Package Starter has already been purchased");
                }
            }
        }
        AccountPackageEntity accountPackageEntity = accountPackageService.findAccountPackageByAccountId(accountId);
        if (accountPackageEntity != null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Account still has a usable package");
        }

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setAccountId(accountId);
        purchaseEntity.setAmount(0.0);
        purchaseEntity.setPurchaseDate(LocalDateTime.now(TIME_ZONE));
        purchaseEntity.setStatus("success");
        purchaseEntity = purchaseService.createPurchase(purchaseEntity);


        AccountPackageEntity accountPackage = new AccountPackageEntity();
        accountPackage.setNumOfPost(1);
        accountPackage.setValidTo(LocalDateTime.now(TIME_ZONE).plusMonths(1));
        accountPackage.setPackageId(1);
        accountPackage.setAccountId(accountId);
        accountPackage.setExpired(false);
        accountPackage.setPurchaseId(purchaseEntity.getId());
        return new ResponseEntity<>(accountPackageService.createAccountPackage(accountPackage), HttpStatus.OK);
    }
}
