package capstone.oras.api.packages.controller;

import capstone.oras.api.packages.service.IPackageService;
import capstone.oras.entity.PackageEntity;
import org.springframework.beans.factory.annotation.Autowired;
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
    private IPackageService packageService;

    @Autowired
    public PackageController(IPackageService packageService) {
        this.packageService = packageService;
    }

    @RequestMapping(value = "/package", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<PackageEntity> createPackage(@RequestBody PackageEntity packageEntity) {
        return new ResponseEntity<>(packageService.createPackage(packageEntity), HttpStatus.OK);
    }

    @RequestMapping(value = "/package", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<PackageEntity> updatePackage(@RequestBody PackageEntity packageEntity) {
        return new ResponseEntity<>(packageService.updatePackage(packageEntity), HttpStatus.OK);
    }

    @RequestMapping(value = "/packages", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<PackageEntity>> getAllPackage() {
        List<PackageEntity> lst = packageService.getAllPackage();
        if (!CollectionUtils.isEmpty(lst)) {
            lst.sort(Comparator.comparingInt(PackageEntity::getId));
        }
        return new ResponseEntity<>(lst, HttpStatus.OK);
    }

    @RequestMapping(value = "/active-packages", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<PackageEntity>> getAllActivePackage() {
        List<PackageEntity> lst = packageService.getAllActivePackage();
        if (!CollectionUtils.isEmpty(lst)) {
            lst.sort(Comparator.comparingInt(PackageEntity::getId));
        }
        return new ResponseEntity<>(lst, HttpStatus.OK);
    }


    @RequestMapping(value = "/package/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<PackageEntity> getPackageById(@PathVariable("id") int id) {
        return new ResponseEntity<>(packageService.findPackageById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/deactivate", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Integer> deactivatePackage(@RequestBody int id) {
        return new ResponseEntity<>(packageService.deactivatePackage(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/activate", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Integer> activatePackage(@RequestBody int id) {
        return new ResponseEntity<>(packageService.activatePackage(id), HttpStatus.OK);
    }
}
