package capstone.oras.company.controller;


import capstone.oras.company.service.ICompanyService;
import capstone.oras.entity.CompanyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/company-management")
public class CompanyController {

    @Autowired
    private ICompanyService companyService;

    HttpHeaders httpHeaders = new HttpHeaders();

    @RequestMapping(value = "/company", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<CompanyEntity> createCompany(@RequestBody CompanyEntity companyEntity) {

            return new ResponseEntity<>(companyService.createCompany(companyEntity), HttpStatus.OK);

    }

    @RequestMapping(value = "/company", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<CompanyEntity> updateCompany(@RequestBody CompanyEntity companyEntity) {

        return new ResponseEntity<>(companyService.updateCompany(companyEntity), HttpStatus.OK);

    }

    @RequestMapping(value = "/companies", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<CompanyEntity>> getAllCompany() {
        return new ResponseEntity<List<CompanyEntity>>(companyService.getAllCompany(), HttpStatus.OK);
    }


    @RequestMapping(value = "/company/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<CompanyEntity> getCompanyById(@PathVariable("id") int id) {
        return new ResponseEntity<CompanyEntity>(companyService.findCompanyById(id), HttpStatus.OK);
    }
}
