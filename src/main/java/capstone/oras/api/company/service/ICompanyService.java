package capstone.oras.api.company.service;

import capstone.oras.entity.CompanyEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICompanyService {
    CompanyEntity createCompany(CompanyEntity companyEntity);
    CompanyEntity updateCompany(CompanyEntity companyEntity);
    List<CompanyEntity> getAllCompany();
    List<CompanyEntity> getAllCompanyWithPaging(Pageable pageable);
    CompanyEntity findCompanyById(int id);
    Integer verifyCompany(int id);
    List<CompanyEntity> getAllCompanyWithNameAndIsVerified(String name);
    Boolean checkCompanyName(Integer id, String name);

}
