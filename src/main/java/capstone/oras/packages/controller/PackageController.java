package capstone.oras.packages.controller;

import capstone.oras.entity.PackageEntity;
import capstone.oras.packages.service.IPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/package-management")
public class PackageController {


    @Autowired
    private IPackageService packageService;

    HttpHeaders httpHeaders = new HttpHeaders();

    @RequestMapping(value = "/package", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<PackageEntity> createPackage(@RequestBody PackageEntity packageEntity) {

        return new ResponseEntity<>(packageService.createPackage(packageEntity), HttpStatus.OK);

    }

    @RequestMapping(value = "/package", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<PackageEntity> updatePackage(@RequestBody PackageEntity packageEntity) {

        return new ResponseEntity<>(packageService.updatePackage(packageEntity), HttpStatus.OK);

    }

    @RequestMapping(value = "/packages", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<PackageEntity>> getAllPackage() {
        return new ResponseEntity<List<PackageEntity>>(packageService.getAllPackage(), HttpStatus.OK);
    }


    @RequestMapping(value = "/package/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<PackageEntity> getPackageById(@PathVariable("id") int id) {
        return new ResponseEntity<PackageEntity>(packageService.findPackageById(id), HttpStatus.OK);
    }
}
