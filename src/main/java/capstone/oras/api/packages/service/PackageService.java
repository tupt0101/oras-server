package capstone.oras.api.packages.service;

import capstone.oras.dao.IPackageRepository;
import capstone.oras.entity.PackageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PackageService implements IPackageService {
    private IPackageRepository IPackageRepository;
    @Autowired
    public PackageService(IPackageRepository IPackageRepository) {
        this.IPackageRepository = IPackageRepository;
    }

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

    @Override
    public Integer deactivatePackage(int id) {
        if (!IPackageRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "This package ID does not exist.");
        }
        return IPackageRepository.deactivatePackage(id);
    }

    @Override
    public List<PackageEntity> getAllActivePackage() {
        return IPackageRepository.findPackageEntitiesByActiveTrue();
    }
}
