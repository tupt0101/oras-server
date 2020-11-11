package capstone.oras.companyPackage.service;

import capstone.oras.dao.ICompanyPackageRepository;
import capstone.oras.entity.CompanyPackageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyPackageService implements ICompanyPackageService {

    @Autowired
    private ICompanyPackageRepository ICompanyPackageRepository;

    @Override
    public CompanyPackageEntity createCompanyPackage(CompanyPackageEntity companyPackageEntity) {
        return ICompanyPackageRepository.save(companyPackageEntity);
    }

    @Override
    public CompanyPackageEntity updateCompanyPackage(CompanyPackageEntity companyPackageEntity) {
        return ICompanyPackageRepository.save(companyPackageEntity);
    }

    @Override
    public List<CompanyPackageEntity> getAllCompanyPackage() {
        return ICompanyPackageRepository.findAll();
    }

    @Override
    public CompanyPackageEntity findCompanyPackageById(int id) {
        if (ICompanyPackageRepository.findById(id).isPresent()) {
            return ICompanyPackageRepository.findById(id).get();
        } else return null;
    }
}
