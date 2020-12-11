package capstone.oras.api.company.service;

import capstone.oras.dao.IAccountRepository;
import capstone.oras.dao.ICompanyRepository;
import capstone.oras.entity.CompanyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
    public List<CompanyEntity> getAllCompanyWithPaging(Pageable pageable, String status, String name) {
        status = StringUtils.isEmpty(status) ? "%" : status;
        name = "%" + name + "%";
        return ICompanyRepository.findAllByStatusLikeAndNameIgnoreCaseLike(pageable, status, name);
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
    public Integer verifyCompany(int id) {
        if (!ICompanyRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company does not exist.");
        }
        iAccountRepository.updateActiveByVerifyingCompany(id);
        return ICompanyRepository.verifyCompanyPass(id);
    }
}
