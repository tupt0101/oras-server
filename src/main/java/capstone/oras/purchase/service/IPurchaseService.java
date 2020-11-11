package capstone.oras.purchase.service;


import capstone.oras.entity.PurchaseEntity;

import java.util.List;

public interface IPurchaseService {
    PurchaseEntity createPurchase(PurchaseEntity purchaseEntity);
    PurchaseEntity updatePurchase(PurchaseEntity purchaseEntity);
    List<PurchaseEntity> getAllPurchase();
    PurchaseEntity findPurchaseById(int id);
}
