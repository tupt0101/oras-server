package capstone.oras.api.companyPackage.service;

import capstone.oras.dao.ICompanyPackageRepository;
import capstone.oras.entity.AccountPackageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyPackageService implements ICompanyPackageService {

    @Autowired
    private ICompanyPackageRepository ICompanyPackageRepository;

    @Override
    public AccountPackageEntity createCompanyPackage(AccountPackageEntity accountPackageEntity) {
        return ICompanyPackageRepository.save(accountPackageEntity);
    }

    @Override
    public AccountPackageEntity updateCompanyPackage(AccountPackageEntity accountPackageEntity) {
        return ICompanyPackageRepository.save(accountPackageEntity);
    }

    @Override
    public List<AccountPackageEntity> getAllCompanyPackage() {
        return ICompanyPackageRepository.findAll();
    }

    @Override
    public AccountPackageEntity findCompanyPackageById(int id) {
        if (ICompanyPackageRepository.findById(id).isPresent()) {
            return ICompanyPackageRepository.findById(id).get();
        } else return null;
    }

    @Override
    public AccountPackageEntity findAccountPackageByAccountId(int id) {
        if (ICompanyPackageRepository.findAccountPackageEntityByAccountIdEqualsAndExpiredFalse(id).isPresent()) {
            return ICompanyPackageRepository.findAccountPackageEntityByAccountIdEqualsAndExpiredFalse(id).get();
        } else return null;
    }
}
