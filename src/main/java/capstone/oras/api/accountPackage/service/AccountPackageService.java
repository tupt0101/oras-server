package capstone.oras.api.accountPackage.service;

import capstone.oras.dao.IAccountPackageRepository;
import capstone.oras.entity.AccountPackageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountPackageService implements IAccountPackageService {

    @Autowired
    private IAccountPackageRepository IAccountPackageRepository;

    @Override
    public AccountPackageEntity createAccountPackage(AccountPackageEntity accountPackageEntity) {
        return IAccountPackageRepository.save(accountPackageEntity);
    }

    @Override
    public AccountPackageEntity updateAccountPackage(AccountPackageEntity accountPackageEntity) {
        return IAccountPackageRepository.save(accountPackageEntity);
    }

    @Override
    public List<AccountPackageEntity> getAllAccountPackage() {
        return IAccountPackageRepository.findAll();
    }

    @Override
    public AccountPackageEntity findAccountPackageById(int id) {
        if (IAccountPackageRepository.findById(id).isPresent()) {
            return IAccountPackageRepository.findById(id).get();
        } else return null;
    }

    @Override
    public AccountPackageEntity findAccountPackageByAccountId(int id) {
        if (IAccountPackageRepository.findAccountPackageEntityByAccountIdEqualsAndExpiredFalse(id).isPresent()) {
            return IAccountPackageRepository.findAccountPackageEntityByAccountIdEqualsAndExpiredFalse(id).get();
        } else return null;
    }

    @Override
    public List<AccountPackageEntity> findAccountPackagesByAccountId(int id) {
        if (IAccountPackageRepository.findAccountPackageEntitiesByAccountIdEquals(id).isPresent()) {
            return IAccountPackageRepository.findAccountPackageEntitiesByAccountIdEquals(id).get();
        } else return null;    }
}
