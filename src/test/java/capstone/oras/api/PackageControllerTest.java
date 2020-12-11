package capstone.oras.api;

import capstone.oras.api.packages.controller.PackageController;
import capstone.oras.api.packages.service.IPackageService;
import capstone.oras.api.packages.service.PackageService;
import capstone.oras.dao.IPackageRepository;
import capstone.oras.entity.PackageEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(MockitoJUnitRunner.class)
public class PackageControllerTest {
    private PackageController controller;
    private IPackageService packageService;
    @Mock
    private IPackageRepository packageRepository;

    @Before
    public void init() {
        packageService = new PackageService(packageRepository);
        controller = new PackageController(packageService);
    }

    @Test
    public void testCreatePackage() {
        // test data
        PackageEntity testData = createTestData();
        // expected return
        ResponseEntity<PackageEntity> expected = new ResponseEntity<>(new PackageEntity(), HttpStatus.OK);
        // mock function
        Mockito.when(packageRepository.save(testData)).thenReturn(testData);
        // call method
        ResponseEntity<PackageEntity> actual = controller.createPackage(testData);
        // assert
        assertEquals(expected, actual);
    }

    @Test
    public void testUpdatePackage() {
        // test data
        PackageEntity testData = createTestData();
        // expected return
        ResponseEntity<PackageEntity> expected = new ResponseEntity<>(new PackageEntity(), HttpStatus.OK);
        // mock function
        Mockito.when(packageRepository.save(testData)).thenReturn(testData);
        // call method
        ResponseEntity<PackageEntity> actual = controller.updatePackage(testData);
        // assert
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAllPackage() {
        // test data
        PackageEntity data = createTestData();
        List<PackageEntity> testData = Collections.singletonList(data);
        // expected return
        ResponseEntity<List<PackageEntity>> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(packageRepository.findAll()).thenReturn(testData);
        // call method
        ResponseEntity<List<PackageEntity>> actual = controller.getAllPackage();
        // assert
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAllActivePackage() {
        // test data
        PackageEntity data = createTestData();
        List<PackageEntity> testData = Collections.singletonList(data);
        // expected return
        ResponseEntity<List<PackageEntity>> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(packageRepository.findPackageEntitiesByActiveTrue()).thenReturn(testData);
        // call method
        ResponseEntity<List<PackageEntity>> actual = controller.getAllActivePackage();
        // assert
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPackageById() {
        // test data
        PackageEntity testData = createTestData();
        int id = 1;
        // expected return
        ResponseEntity<PackageEntity> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(packageRepository.findById(id)).thenReturn(java.util.Optional.of(testData));
        // call method
        ResponseEntity<PackageEntity> actual = controller.getPackageById(id);
        // assert
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPackageByIdNull() {
        int id = 1;
        // expected return
        ResponseEntity<PackageEntity> expected = new ResponseEntity<>(null, HttpStatus.OK);
        // mock function
        Mockito.when(packageRepository.findById(id)).thenReturn(java.util.Optional.empty());
        // call method
        ResponseEntity<PackageEntity> actual = controller.getPackageById(id);
        // assert
        assertEquals(expected, actual);
    }

    @Test
    public void testDeactivatePackage() {
        // test data
        int id = 1;
        // expected return
        ResponseEntity<Integer> expected = new ResponseEntity<>(1, HttpStatus.OK);
        // mock function
        Mockito.when(packageRepository.existsById(id)).thenReturn(true);
        Mockito.when(packageRepository.changePackageActive(id, false)).thenReturn(1);
        // call method
        ResponseEntity<Integer> actual = controller.deactivatePackage(id);
        // assert
        assertEquals(expected, actual);
    }

    @Test(expected = ResponseStatusException.class)
    public void testDeactivatePackageError() {
        // test data
        int id = 1;
        // mock function
        Mockito.when(packageRepository.existsById(id)).thenReturn(false);
        // call method
        controller.deactivatePackage(id);
    }

    private PackageEntity createTestData() {
        PackageEntity e = new PackageEntity();
        e.setActive(true);
        e.setCurrency("USD");
        e.setDescription("This is test");
        e.setName("TEST");
        e.setNumOfPost(1);
        e.setPrice((double) 10);
        e.setTag("TEST");
        return new PackageEntity();
    }
}
