package capstone.oras.api.packages.service;

import capstone.oras.dao.IPackageRepository;
import capstone.oras.entity.PackageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackageService implements IPackageService {

    @Autowired
    private IPackageRepository IPackageRepository;

    @Override
    public PackageEntity createPackage(PackageEntity packageEntity) {
        return IPackageRepository.save(packageEntity);
    }

    @Override
    public PackageEntity updatePackage(PackageEntity packageEntity) {
        return IPackageRepository.save(packageEntity);
    }

    @Override
    public List<PackageEntity> getAllPackage() {
        return IPackageRepository.findAll();
    }

    @Override
    public PackageEntity findPackageById(int id) {
        if (IPackageRepository.findById(id).isPresent()) {
            return IPackageRepository.findById(id).get();
        } else return null;
    }
}
