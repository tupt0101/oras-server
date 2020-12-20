package capstone.oras.api.company.service;

import capstone.oras.entity.AccountEntity;
import capstone.oras.entity.BuffCompanyEntity;
import capstone.oras.entity.CompanyEntity;
import capstone.oras.model.custom.ListAccountBuffModel;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.util.List;

public interface ICompanyService {
    CompanyEntity createCompany(CompanyEntity companyEntity);
    CompanyEntity updateCompany(CompanyEntity companyEntity);
    CompanyEntity updateCompanyByAdmin(CompanyEntity companyEntity) throws MessagingException;
    List<CompanyEntity> getAllCompany();
    ListAccountBuffModel getAllCompanyWithPaging(Pageable pageable, String status, String name);
    CompanyEntity findCompanyById(int id);
    void verifyCompany(int id, String email) throws MessagingException;
    List<CompanyEntity> getAllCompanyWithNameAndIsVerified(String name);
    Boolean checkCompanyName(Integer id, String name);
    AccountEntity getAccountCompany(int id);
    int updateCompanyStatus(int id, boolean status);
    BuffCompanyEntity saveBufferCompany(CompanyEntity companyEntity);
}
