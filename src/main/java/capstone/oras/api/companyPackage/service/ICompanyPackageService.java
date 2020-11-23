package capstone.oras.api.companyPackage.service;

import capstone.oras.entity.CompanyPackageEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICompanyPackageService {
    CompanyPackageEntity createCompanyPackage(CompanyPackageEntity companyPackageEntity);
    CompanyPackageEntity updateCompanyPackage(CompanyPackageEntity companyPackageEntity);
    List<CompanyPackageEntity> getAllCompanyPackage();
    CompanyPackageEntity findCompanyPackageById(int id);
}
