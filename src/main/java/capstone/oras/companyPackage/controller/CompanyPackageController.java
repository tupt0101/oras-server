package capstone.oras.companyPackage.controller;


import capstone.oras.companyPackage.service.ICompanyPackageService;
import capstone.oras.entity.CompanyPackageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/company-package-management")
public class CompanyPackageController {

    @Autowired
    private ICompanyPackageService companyPackageService;

    HttpHeaders httpHeaders = new HttpHeaders();

    @RequestMapping(value = "/company-package", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<CompanyPackageEntity> createCompanyPackage(@RequestBody CompanyPackageEntity companyEntity) {

        return new ResponseEntity<>(companyPackageService.createCompanyPackage(companyEntity), HttpStatus.OK);

    }

    @RequestMapping(value = "/company-package", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<CompanyPackageEntity> updateCompanyPackage(@RequestBody CompanyPackageEntity companyEntity) {

        return new ResponseEntity<>(companyPackageService.updateCompanyPackage(companyEntity), HttpStatus.OK);

    }

    @RequestMapping(value = "/company-packages", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<CompanyPackageEntity>> getAllCompanyPackage() {
        return new ResponseEntity<List<CompanyPackageEntity>>(companyPackageService.getAllCompanyPackage(), HttpStatus.OK);
    }


    @RequestMapping(value = "/company-package/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<CompanyPackageEntity> getCompanyPackageById(@PathVariable("id") int id) {
        return new ResponseEntity<CompanyPackageEntity>(companyPackageService.findCompanyPackageById(id), HttpStatus.OK);
    }
}
