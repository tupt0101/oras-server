package capstone.oras.api.accountPackage.service;

import capstone.oras.entity.AccountPackageEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAccountPackageService {
    AccountPackageEntity createAccountPackage(AccountPackageEntity accountPackageEntity);
    AccountPackageEntity updateAccountPackage(AccountPackageEntity accountPackageEntity);
    List<AccountPackageEntity> updateAccountPackages(List<AccountPackageEntity> accountPackageEntity);
    List<AccountPackageEntity> getAllAccountPackage();
    List<AccountPackageEntity> getAllAccountPackageWithPaging(Pageable pageable);
    AccountPackageEntity findAccountPackageById(int id);
    AccountPackageEntity findAccountPackageByAccountId(int id);
    List<AccountPackageEntity> findAccountPackagesByAccountId(int id);
    List<AccountPackageEntity> findAllValidAccountPackages();


}
