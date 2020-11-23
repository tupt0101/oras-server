package capstone.oras.api.companyPackage.controller;


import capstone.oras.api.company.service.ICompanyService;
import capstone.oras.api.companyPackage.service.ICompanyPackageService;
import capstone.oras.api.packages.service.IPackageService;
import capstone.oras.api.purchase.service.IPurchaseService;
import capstone.oras.entity.CompanyPackageEntity;
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

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Package Id is empty");
        } else if (companyPackageEntity.getCompanyId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company Id is empty");
        } else if (companyPackageEntity.getPurchaseId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Purchase Id is empty");
        } else if (companyPackageEntity.getValidTo() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valid To is empty");
        } else if (purchaseService.findPurchaseById(companyPackageEntity.getPurchaseId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Purchase Id doesn't exist");
        } else if (companyService.findCompanyById(companyPackageEntity.getCompanyId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company Id doesn't exist");
        } else if (packageService.findPackageById(companyPackageEntity.getPackageId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Package Id doesn't exist");
        }
        return new ResponseEntity<>(companyPackageService.createCompanyPackage(companyPackageEntity), HttpStatus.OK);

    }

    @RequestMapping(value = "/company-package", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<CompanyPackageEntity> updateCompanyPackage(@RequestBody CompanyPackageEntity companyPackageEntity) {
        if (companyPackageEntity.getPackageId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Package Id is empty");
        } else if (companyPackageEntity.getCompanyId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company Id is empty");
        } else if (companyPackageEntity.getPurchaseId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Purchase Id is empty");
        } else if (companyPackageEntity.getValidTo() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valid To is empty");
        } else if (purchaseService.findPurchaseById(companyPackageEntity.getPurchaseId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Purchase Id doesn't exist");
        } else if (companyService.findCompanyById(companyPackageEntity.getCompanyId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company Id doesn't exist");
        } else if (packageService.findPackageById(companyPackageEntity.getPackageId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Package Id doesn't exist");
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
