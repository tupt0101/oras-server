package capstone.oras.api.companyPackage.controller;


import capstone.oras.api.company.service.ICompanyService;
import capstone.oras.api.companyPackage.service.ICompanyPackageService;
import capstone.oras.api.packages.service.IPackageService;
import capstone.oras.api.purchase.service.IPurchaseService;
import capstone.oras.entity.AccountPackageEntity;
import capstone.oras.entity.PurchaseEntity;
import capstone.oras.entity.model.PurchaseCompanyPagkage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@RestController
@CrossOrigin(value = "http://localhost:9527")
@RequestMapping(value = "/v1/company-package-management")
public class CompanyPackageController {

    @Autowired
    private ICompanyPackageService companyPackageService;

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private IPackageService packageService;

    @Autowired
    private IPurchaseService purchaseService;

    HttpHeaders httpHeaders = new HttpHeaders();

    @RequestMapping(value = "/company-package", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<AccountPackageEntity> createCompanyPackage(@RequestBody PurchaseCompanyPagkage purchaseCompanyPagkage) {
        if (purchaseCompanyPagkage.getAccountPackageEntity().getPackageId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Package Id is empty");
        } else if (purchaseCompanyPagkage.getAccountPackageEntity().getPurchaseId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Purchase Id is empty");
        } else if (purchaseCompanyPagkage.getAccountPackageEntity().getValidTo() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valid To is empty");
        } else if (purchaseService.findPurchaseById(purchaseCompanyPagkage.getAccountPackageEntity().getPurchaseId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Purchase Id doesn't exist");
        } else if (packageService.findPackageById(purchaseCompanyPagkage.getAccountPackageEntity().getPackageId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Package Id doesn't exist");
        }
        PurchaseEntity purchaseEntity = purchaseService.createPurchase(purchaseCompanyPagkage.getPurchaseEntity());
        AccountPackageEntity companyPackage = new AccountPackageEntity();
        companyPackage = purchaseCompanyPagkage.getAccountPackageEntity();
        companyPackage.setPurchaseId(purchaseEntity.getId());
        companyPackage = companyPackageService.createCompanyPackage(companyPackage);
        return new ResponseEntity<>(companyPackage, HttpStatus.OK);
    }

    @RequestMapping(value = "/company-package", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<AccountPackageEntity> updateCompanyPackage(@RequestBody AccountPackageEntity accountPackageEntity) {
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
        return new ResponseEntity<>(companyPackageService.updateCompanyPackage(accountPackageEntity), HttpStatus.OK);

    }

    @RequestMapping(value = "/company-packages", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<AccountPackageEntity>> getAllCompanyPackage() {
        List<AccountPackageEntity> lst = companyPackageService.getAllCompanyPackage();
        if (!CollectionUtils.isEmpty(lst)) {
            lst.sort(Comparator.comparingInt(AccountPackageEntity::getAccountId));
        }
        return new ResponseEntity<List<AccountPackageEntity>>(lst, HttpStatus.OK);
    }


    @RequestMapping(value = "/company-package/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<AccountPackageEntity> getCompanyPackageById(@PathVariable("id") int id) {
        return new ResponseEntity<AccountPackageEntity>(companyPackageService.findCompanyPackageById(id), HttpStatus.OK);
    }

}
