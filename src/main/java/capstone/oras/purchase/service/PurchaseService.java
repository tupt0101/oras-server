package capstone.oras.purchase.service;

import capstone.oras.dao.IPurchaseRepository;
import capstone.oras.entity.PurchaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseService implements IPurchaseService {

    @Autowired
    private IPurchaseRepository IPurchaseRepository;


    @Override
    public PurchaseEntity createPurchase(PurchaseEntity purchaseEntity) {
        return IPurchaseRepository.save(purchaseEntity);
    }

    @Override
    public PurchaseEntity updatePurchase(PurchaseEntity purchaseEntity) {
        return IPurchaseRepository.save(purchaseEntity);
    }

    @Override
    public List<PurchaseEntity> getAllPurchase() {
        return IPurchaseRepository.findAll();
    }

    @Override
    public PurchaseEntity findPurchaseById(int id) {
        if (IPurchaseRepository.findById(id).isPresent()) {
            return IPurchaseRepository.findById(id).get();
        } else return null;
    }
}
