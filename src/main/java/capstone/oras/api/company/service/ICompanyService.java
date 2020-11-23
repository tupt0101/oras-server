package capstone.oras.api.company.service;

import capstone.oras.entity.CompanyEntity;

import java.util.List;

public interface ICompanyService {
    CompanyEntity createCompany(CompanyEntity companyEntity);
    CompanyEntity updateCompany(CompanyEntity companyEntity);
    List<CompanyEntity> getAllCompany();
    CompanyEntity findCompanyById(int id);
}
