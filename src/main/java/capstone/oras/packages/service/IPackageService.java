package capstone.oras.packages.service;

import capstone.oras.entity.PackageEntity;

import java.util.List;

public interface IPackageService {
    PackageEntity createPackage(PackageEntity packageEntity);
    PackageEntity updatePackage(PackageEntity packageEntity);
    List<PackageEntity> getAllPackage();
    PackageEntity findPackageById(int id);
}