package capstone.oras.api.purchase.controller;

import capstone.oras.api.purchase.service.IPurchaseService;
import capstone.oras.entity.PurchaseEntity;
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
@RequestMapping(value = "/v1/purchase-management")
public class PurchaseController {


    @Autowired
    private IPurchaseService purchaseService;

    HttpHeaders httpHeaders = new HttpHeaders();

    @RequestMapping(value = "/purchase", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<PurchaseEntity> createPurchase(@RequestBody PurchaseEntity purchaseEntity) {
        return new ResponseEntity<>(purchaseService.createPurchase(purchaseEntity), HttpStatus.OK);
    }

    @RequestMapping(value = "/purchase", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<PurchaseEntity> updatePackage(@RequestBody PurchaseEntity purchaseEntity) {

        return new ResponseEntity<>(purchaseService.updatePurchase(purchaseEntity), HttpStatus.OK);

    }

    @RequestMapping(value = "/purchases", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<PurchaseEntity>> getAllPackage() {
        List<PurchaseEntity> lst = purchaseService.getAllPurchase();
        if (!CollectionUtils.isEmpty(lst)) {
            lst.sort(Comparator.comparingInt(PurchaseEntity::getId));
        }
        return new ResponseEntity<List<PurchaseEntity>>(lst, HttpStatus.OK);
    }


    @RequestMapping(value = "/purchase/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<PurchaseEntity> getPackageById(@PathVariable("id") int id) {
        return new ResponseEntity<PurchaseEntity>(purchaseService.findPurchaseById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/purchase-by-account-id/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<PurchaseEntity>> getPurchaseByAccountId(@PathVariable("id") int id) {
        List<PurchaseEntity> purchaseEntityList = purchaseService.findPurchaseEntityByAccountID(id);
        purchaseEntityList.sort(Comparator.comparing(PurchaseEntity::getPurchaseDate).reversed());
        return new ResponseEntity<List<PurchaseEntity>>(purchaseEntityList, HttpStatus.OK);
    }

}
