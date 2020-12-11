package capstone.oras.api.accountPackage.service;

import capstone.oras.dao.IAccountPackageRepository;
import capstone.oras.entity.AccountPackageEntity;
import capstone.oras.model.custom.ListAccountPackageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    public List<AccountPackageEntity> updateAccountPackages(List<AccountPackageEntity> accountPackageEntity) {
        return IAccountPackageRepository.saveAll(accountPackageEntity);
    }

    @Override
    public List<AccountPackageEntity> getAllAccountPackage() {
        return IAccountPackageRepository.findAll();
    }

    @Override
    public ListAccountPackageModel getAllAccountPackageWithPaging(Pageable pageable, String name, String status, String pkg) {
        name = "%" + name + "%";
        pkg = "%" + pkg + "%";
        List<AccountPackageEntity> data;
        int count;
        if (StringUtils.isEmpty(status)) {
            data = IAccountPackageRepository.findAllByAccountById_FullnameAndPackageById_Name(pageable, name, pkg);
            count = IAccountPackageRepository.countByAccountById_FullnameAndPackageById_Name(name, pkg);
        } else {
            data = IAccountPackageRepository.findAllByAccountById_FullnameAndPackageById_NameAndExpiredIs(pageable, name, "Valid".equalsIgnoreCase(status), pkg);
            count = IAccountPackageRepository.countByAccountById_FullnameAndPackageById_NameAndExpiredIs(name, "Valid".equalsIgnoreCase(status), pkg);
        }
        return new ListAccountPackageModel(count, data);
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

    @Override
    public List<AccountPackageEntity> findAllValidAccountPackages() {
        if (IAccountPackageRepository.findAccountPackageEntitiesByExpiredFalse().isPresent()) {
            return IAccountPackageRepository.findAccountPackageEntitiesByExpiredFalse().get();
        } else return null;
    }

    @Override
    public List<AccountPackageEntity> findAccountPackageByPackageId(int id) {
        return IAccountPackageRepository.findAccountPackageEntitiesByPackageId(id);
    }
}
