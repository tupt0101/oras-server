package capstone.oras.api.companyPackage.service;

import capstone.oras.entity.AccountPackageEntity;

import java.util.List;

public interface ICompanyPackageService {
    AccountPackageEntity createCompanyPackage(AccountPackageEntity accountPackageEntity);
    AccountPackageEntity updateCompanyPackage(AccountPackageEntity accountPackageEntity);
    List<AccountPackageEntity> getAllCompanyPackage();
    AccountPackageEntity findCompanyPackageById(int id);
}
