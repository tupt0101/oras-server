package capstone.oras.api.packages.service;

import capstone.oras.dao.IPackageRepository;
import capstone.oras.entity.PackageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
        packageValidation(packageEntity);
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
        return IPackageRepository.changePackageActive(id, false);
    }

    @Override
    public Integer activatePackage(int id) {
        if (!IPackageRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "This package ID does not exist.");
        }
        return IPackageRepository.changePackageActive(id, true);
    }

    @Override
    public List<PackageEntity> getAllActivePackage() {
        return IPackageRepository.findPackageEntitiesByActiveTrue();
    }

    private void packageValidation(PackageEntity packageEntity) {
        if (StringUtils.isEmpty(packageEntity.getCurrency())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency is a required field");
        } else if (StringUtils.isEmpty(packageEntity.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is a required field");
        } else if (packageEntity.getDuration() == null || packageEntity.getDuration() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duration is invalid");
        } else if (packageEntity.getNumOfPost() == null || packageEntity.getNumOfPost() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Number of Post is invalid");
        } else if (packageEntity.getPrice() == null || packageEntity.getPrice() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Price is invalid");
        }
    }
}
