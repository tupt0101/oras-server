package capstone.oras.companyPackage.controller;


import capstone.oras.company.service.ICompanyService;
import capstone.oras.companyPackage.service.ICompanyPackageService;
import capstone.oras.packages.service.IPackageService;
import capstone.oras.purchase.service.IPurchaseService;
import capstone.oras.entity.CompanyPackageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    ResponseEntity<CompanyPackageEntity> createCompanyPackage(@RequestBody CompanyPackageEntity companyPackageEntity) {
        if (companyPackageEntity.getPackageId() == null) {
            httpHeaders.set("error", "Package Id is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (companyPackageEntity.getCompanyId() == null) {
            httpHeaders.set("error", "Company Id is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (companyPackageEntity.getPurchaseId() == null) {
            httpHeaders.set("error", "Purchase Id is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (companyPackageEntity.getValidTo() == null) {
            httpHeaders.set("error", "Valid To is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (purchaseService.findPurchaseById(companyPackageEntity.getPurchaseId()) == null) {
            httpHeaders.set("error", "Purchase Id doesn't exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (companyService.findCompanyById(companyPackageEntity.getCompanyId()) == null) {
            httpHeaders.set("error", "Company Id doesn't exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (packageService.findPackageById(companyPackageEntity.getPackageId()) == null) {
            httpHeaders.set("error", "Package Id doesn't exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(companyPackageService.createCompanyPackage(companyPackageEntity), HttpStatus.OK);

    }

    @RequestMapping(value = "/company-package", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<CompanyPackageEntity> updateCompanyPackage(@RequestBody CompanyPackageEntity companyPackageEntity) {
        if (companyPackageEntity.getPackageId() == null) {
            httpHeaders.set("error", "Package Id is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (companyPackageEntity.getCompanyId() == null) {
            httpHeaders.set("error", "Company Id is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (companyPackageEntity.getPurchaseId() == null) {
            httpHeaders.set("error", "Purchase Id is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (companyPackageEntity.getValidTo() == null) {
            httpHeaders.set("error", "Valid To is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (purchaseService.findPurchaseById(companyPackageEntity.getPurchaseId()) == null) {
            httpHeaders.set("error", "Purchase Id doesn't exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (companyService.findCompanyById(companyPackageEntity.getCompanyId()) == null) {
            httpHeaders.set("error", "Company Id doesn't exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (packageService.findPackageById(companyPackageEntity.getPackageId()) == null) {
            httpHeaders.set("error", "Package Id doesn't exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(companyPackageService.updateCompanyPackage(companyPackageEntity), HttpStatus.OK);

    }

    @RequestMapping(value = "/company-packages", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<CompanyPackageEntity>> getAllCompanyPackage() {
        List<CompanyPackageEntity> lst = companyPackageService.getAllCompanyPackage();
        lst.sort(Comparator.comparingInt(CompanyPackageEntity::getCompanyId));
        return new ResponseEntity<List<CompanyPackageEntity>>(lst, HttpStatus.OK);
    }


    @RequestMapping(value = "/company-package/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<CompanyPackageEntity> getCompanyPackageById(@PathVariable("id") int id) {
        return new ResponseEntity<CompanyPackageEntity>(companyPackageService.findCompanyPackageById(id), HttpStatus.OK);
    }
}
