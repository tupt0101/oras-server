package capstone.oras.api.company.controller;


import capstone.oras.api.company.service.ICompanyService;
import capstone.oras.entity.CompanyEntity;
import capstone.oras.oauth2.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@CrossOrigin(value = "http://localhost:9527")
@RequestMapping(value = "/v1/company-management")
public class CompanyController {

    @Autowired
    private ICompanyService companyService;

    HttpHeaders httpHeaders = new HttpHeaders();

    @RequestMapping(value = "/company", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<CompanyEntity> createCompany(@RequestBody CompanyEntity companyEntity) {
        if (companyEntity.getEmail() == null || companyEntity.getEmail().isEmpty()) {
            httpHeaders.set("error", "Email is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (companyEntity.getName() == null || companyEntity.getName().isEmpty()) {
            httpHeaders.set("error", "Name is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (companyEntity.getTaxCode() == null || companyEntity.getTaxCode().isEmpty()) {
            httpHeaders.set("error", "Tax Code is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (companyEntity.getPhoneNo() == null || companyEntity.getPhoneNo().isEmpty()) {
            httpHeaders.set("error", "Phone Number is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (companyEntity.getLocation() == null || companyEntity.getLocation().isEmpty()) {
            httpHeaders.set("error", "Location is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }
            return new ResponseEntity<>(companyService.createCompany(companyEntity), HttpStatus.OK);

    }

    @RequestMapping(value = "/company", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<CompanyEntity> updateCompany(@RequestBody CompanyEntity companyEntity) {
        if (companyEntity.getEmail() == null || companyEntity.getEmail().isEmpty()) {
            httpHeaders.set("error", "Email is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (companyEntity.getName() == null || companyEntity.getName().isEmpty()) {
            httpHeaders.set("error", "Name is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (companyEntity.getTaxCode() == null || companyEntity.getTaxCode().isEmpty()) {
            httpHeaders.set("error", "Tax Code is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (companyEntity.getPhoneNo() == null || companyEntity.getPhoneNo().isEmpty()) {
            httpHeaders.set("error", "Phone Number is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (companyEntity.getLocation() == null || companyEntity.getLocation().isEmpty()) {
            httpHeaders.set("error", "Location is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(companyService.updateCompany(companyEntity), HttpStatus.OK);

    }

    @RequestMapping(value = "/companies", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<CompanyEntity>> getAllCompany() {
        List<CompanyEntity> lst = companyService.getAllCompany();
        lst.sort(Comparator.comparingInt(CompanyEntity::getId));
        return new ResponseEntity<List<CompanyEntity>>(lst, HttpStatus.OK);
    }


    @RequestMapping(value = "/company/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<CompanyEntity> getCompanyById(@PathVariable("id") int id) {
        return new ResponseEntity<CompanyEntity>(companyService.findCompanyById(id), HttpStatus.OK);
    }



    @RequestMapping(value = "/company-openjob", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<CompanyEntity> createCompanyMulti(@RequestBody CompanyEntity companyEntity) {
        if (companyEntity.getEmail() == null || companyEntity.getEmail().isEmpty()) {
            httpHeaders.set("error", "Email is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (companyEntity.getName() == null || companyEntity.getName().isEmpty()) {
            httpHeaders.set("error", "Name is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (companyEntity.getTaxCode() == null || companyEntity.getTaxCode().isEmpty()) {
            httpHeaders.set("error", "Tax Code is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (companyEntity.getPhoneNo() == null || companyEntity.getPhoneNo().isEmpty()) {
            httpHeaders.set("error", "Phone Number is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (companyEntity.getLocation() == null || companyEntity.getLocation().isEmpty()) {
            httpHeaders.set("error", "Location is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }
        //get openjob token
        CustomUserDetailsService userDetailsService = new CustomUserDetailsService();
        String token = "Bearer " + userDetailsService.getOpenJobToken();
        // post company to openjob
        String uri = "https://openjob-server.herokuapp.com/v1/company-management/company";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
//        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<CompanyEntity> entity = new HttpEntity<>(companyEntity,headers);
        System.out.println(entity.getHeaders());
        CompanyEntity openJobEntity = restTemplate.postForObject(uri, entity, CompanyEntity.class);
//        ResponseEntity<CompanyEntity> openJobEntity = restTemplate.exchange(uri,HttpMethod.POST, entity, CompanyEntity.class);

        return new ResponseEntity(openJobEntity, HttpStatus.OK);

    }
}
