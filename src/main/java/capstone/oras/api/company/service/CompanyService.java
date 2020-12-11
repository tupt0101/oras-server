package capstone.oras.api.company.service;

import capstone.oras.common.CommonUtils;
import capstone.oras.dao.IAccountRepository;
import capstone.oras.dao.ICompanyRepository;
import capstone.oras.entity.CompanyEntity;
import capstone.oras.model.custom.ListCompanyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import java.util.List;

import static capstone.oras.common.Constant.EmailForm.VERIFY_COMPANY_NOTI;

@Service
public class CompanyService implements ICompanyService{

    @Autowired
    private ICompanyRepository ICompanyRepository;
    @Autowired
    private IAccountRepository iAccountRepository;

    @Override
    public CompanyEntity createCompany(CompanyEntity companyEntity) {
        return ICompanyRepository.save(companyEntity);
    }

    @Override
    public CompanyEntity updateCompany(CompanyEntity companyEntity) {
        return ICompanyRepository.save(companyEntity);
    }

    @Override
    public List<CompanyEntity> getAllCompany() {
        return ICompanyRepository.findAll();
    }

    @Override
    public ListCompanyModel getAllCompanyWithPaging(Pageable pageable, String status, String name) {
        name = "%" + name + "%";
        int count;
        List<CompanyEntity> data;
        if (StringUtils.isEmpty(status)) {
            data =  ICompanyRepository.findAllByNameIgnoreCaseLike(pageable, name);
            count =  ICompanyRepository.countByNameIgnoreCaseLike(name);
        } else {
            data = ICompanyRepository.findAllByVerifiedAndNameIgnoreCaseLike(pageable, "Verified".equalsIgnoreCase(status), name);
            count = ICompanyRepository.countByVerifiedAndNameIgnoreCaseLike("Verified".equalsIgnoreCase(status), name);
        }
        return new ListCompanyModel(count, data);
    }

    @Override
    public CompanyEntity findCompanyById(int id) {
        if (ICompanyRepository.findById(id).isPresent()) {
            return ICompanyRepository.findById(id).get();
        } else return null;
    }

    @Override
    public List<CompanyEntity> getAllCompanyWithNameAndIsVerified(String name) {
        if(ICompanyRepository.findCompanyEntitiesByNameEqualsAndVerifiedEquals(name, true).isPresent()) {
            return ICompanyRepository.findCompanyEntitiesByNameEqualsAndVerifiedEquals(name,true).get();
        } else return null;
    }

    @Override
    public Boolean checkCompanyName(Integer id, String name) {
        if (id == null) {
            id = 0;
        }
        return ICompanyRepository.findCompanyEntitiesByIdIsNotAndNameEqualsAndVerifiedEquals(id, name, true).isPresent();
    }

    @Override
    public Integer verifyCompany(int id, String email) throws MessagingException {
        if (!ICompanyRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company does not exist.");
        }
        iAccountRepository.updateActiveByVerifyingCompany(id);
        CommonUtils.sendMail(email, "Complete Registration!",VERIFY_COMPANY_NOTI);
        return ICompanyRepository.verifyCompanyPass(id);
    }
}
