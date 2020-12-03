package capstone.oras.api.accountPackage.service;

import capstone.oras.entity.AccountPackageEntity;

import java.util.List;

public interface IAccountPackageService {
    AccountPackageEntity createAccountPackage(AccountPackageEntity accountPackageEntity);
    AccountPackageEntity updateAccountPackage(AccountPackageEntity accountPackageEntity);
    List<AccountPackageEntity> getAllAccountPackage();
    AccountPackageEntity findAccountPackageById(int id);
    AccountPackageEntity findAccountPackageByAccountId(int id);
    List<AccountPackageEntity> findAccountPackagesByAccountId(int id);


}
