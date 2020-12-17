package capstone.oras.api.company.controller;


import capstone.oras.api.company.service.ICompanyService;
import capstone.oras.common.CommonUtils;
import capstone.oras.entity.CompanyEntity;
import capstone.oras.model.custom.ListAccountModel;
import capstone.oras.oauth2.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@CrossOrigin(value = "http://localhost:9527")
@RequestMapping(value = "/v1/company-management")
public class CompanyController {

    @Autowired
    private ICompanyService companyService;

    @RequestMapping(value = "/company", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<CompanyEntity> createCompany(@RequestBody CompanyEntity companyEntity) {
        if (companyEntity.getEmail() == null || companyEntity.getEmail().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is a required field");
        } else if (companyEntity.getName() == null || companyEntity.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is a required field");
        } else if (companyEntity.getTaxCode() == null || companyEntity.getTaxCode().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tax Code is a required field");
        } else if (companyEntity.getPhoneNo() == null || companyEntity.getPhoneNo().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone Number is a required field");
        } else if (companyEntity.getLocation() == null || companyEntity.getLocation().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location is a required field");
        } else if (companyService.checkCompanyName(companyEntity.getId(), companyEntity.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name already exist");
        }
        return new ResponseEntity<>(companyService.createCompany(companyEntity), HttpStatus.OK);

    }

    @RequestMapping(value = "/company", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<CompanyEntity> updateCompany(@RequestBody CompanyEntity companyEntity) {
        if (companyEntity.getEmail() == null || companyEntity.getEmail().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is a required field");
        } else if (companyEntity.getName() == null || companyEntity.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is a required field");
        } else if (companyEntity.getTaxCode() == null || companyEntity.getTaxCode().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tax Code is a required field");
        } else if (companyEntity.getPhoneNo() == null || companyEntity.getPhoneNo().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone Number is a required field");
        } else if (companyEntity.getLocation() == null || companyEntity.getLocation().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location is a required field");
        } else if (companyService.checkCompanyName(companyEntity.getId(), companyEntity.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name already exist");
        }
        return new ResponseEntity<>(companyService.updateCompany(companyEntity), HttpStatus.OK);

    }

    @RequestMapping(value = "/check-company-name", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<String> checkCompanyName(@Param("id") Integer id, @Param("name") String name) {
        if (companyService.checkCompanyName(id, name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Name already exist");
        }
        return new ResponseEntity<>("Name is ok to use", HttpStatus.OK);
    }

    @RequestMapping(value = "/companies", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<CompanyEntity>> getAllCompany() {
        List<CompanyEntity> lst = companyService.getAllCompany();
        if (!CollectionUtils.isEmpty(lst)) {
            lst.sort(Comparator.comparingInt(CompanyEntity::getId));
        }
        return new ResponseEntity<List<CompanyEntity>>(lst, HttpStatus.OK);
    }


    @RequestMapping(value = "/companies-paging", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<ListAccountModel> getAllCompanyWithPaging(@RequestParam(value = "numOfElement") Integer numOfElement,
                                                             @RequestParam(value = "page") Integer page,
                                                             @RequestParam(value = "sort") String sort,
                                                             @RequestParam(value = "status") String status,
                                                             @RequestParam(value = "name") String name) {
        Pageable pageable = CommonUtils.configPageable(numOfElement, page, sort);
        return new ResponseEntity<>(companyService.getAllCompanyWithPaging(pageable, status, name), HttpStatus.OK);
    }

    @RequestMapping(value = "/company/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<CompanyEntity> getCompanyById(@PathVariable("id") int id) {
        return new ResponseEntity<CompanyEntity>(companyService.findCompanyById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/company/verify", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<Integer> verifyCompany(@Param("id") int id, @Param("email") String email) {
        try {
            return new ResponseEntity<>(companyService.verifyCompany(id, email), HttpStatus.OK);
        } catch (MessagingException e) {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, "Cannot send email.");
        }
    }

    @RequestMapping(value = "/company-openjob", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<CompanyEntity> createCompanyMulti(@RequestBody CompanyEntity companyEntity) {
        if (companyEntity.getEmail() == null || companyEntity.getEmail().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is a required field");
        } else if (companyEntity.getName() == null || companyEntity.getName().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is a required field");
        } else if (companyEntity.getTaxCode() == null || companyEntity.getTaxCode().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tax Code is a required field");
        } else if (companyEntity.getPhoneNo() == null || companyEntity.getPhoneNo().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone Number is a required field");
        } else if (companyEntity.getLocation() == null || companyEntity.getLocation().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location is a required field");
        }
        //get openjob token
        CustomUserDetailsService userDetailsService = new CustomUserDetailsService();
        String token = CommonUtils.getOjToken();
        // post company to openjob
        String uri = "https://openjob-server.herokuapp.com/v1/company-management/company";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
//        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<CompanyEntity> entity = new HttpEntity<>(companyEntity, headers);
        System.out.println(entity.getHeaders());
        CompanyEntity openJobEntity = restTemplate.postForObject(uri, entity, CompanyEntity.class);
//        ResponseEntity<CompanyEntity> openJobEntity = restTemplate.exchange(uri,HttpMethod.POST, entity, CompanyEntity.class);

        return new ResponseEntity(openJobEntity, HttpStatus.OK);

    }
}
