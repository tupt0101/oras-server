package capstone.oras.api.companyPackage.service;

import capstone.oras.entity.AccountPackageEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICompanyPackageService {
    AccountPackageEntity createCompanyPackage(AccountPackageEntity accountPackageEntity);
    AccountPackageEntity updateCompanyPackage(AccountPackageEntity accountPackageEntity);
    List<AccountPackageEntity> getAllCompanyPackage();
    AccountPackageEntity findCompanyPackageById(int id);
}
