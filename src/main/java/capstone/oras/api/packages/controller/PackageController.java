package capstone.oras.api.packages.controller;

import capstone.oras.api.packages.service.IPackageService;
import capstone.oras.entity.PackageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@CrossOrigin(value = "http://localhost:9527")
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
        List<PackageEntity> lst = packageService.getAllPackage();
        if (!CollectionUtils.isEmpty(lst)) {
            lst.sort(Comparator.comparingInt(PackageEntity::getId));
        }
        return new ResponseEntity<List<PackageEntity>>(lst, HttpStatus.OK);
    }


    @RequestMapping(value = "/package/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<PackageEntity> getPackageById(@PathVariable("id") int id) {
        return new ResponseEntity<PackageEntity>(packageService.findPackageById(id), HttpStatus.OK);
    }
}
