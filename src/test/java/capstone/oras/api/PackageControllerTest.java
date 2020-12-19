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
    public void testCreatePackage1() {
        // test data
        PackageEntity testData = createTestData();
        // expected return
        ResponseEntity<PackageEntity> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(packageRepository.save(testData)).thenReturn(testData);
        // call method
        ResponseEntity<PackageEntity> actual = controller.createPackage(testData);
        // assert
        assertEquals(expected, actual);
    }

    @Test
    public void testCreatePackage2() {
        // test data
        PackageEntity testData = createTestData();
        testData.setDuration(0);
        // expected return
        ResponseStatusException expected = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duration is invalid");
        String actualMsg = "";
        // mock function
        // call method
        try {
            ResponseEntity<PackageEntity> actual = controller.createPackage(testData);
        } catch (ResponseStatusException e) {
            actualMsg = e.getMessage();
        }
        // assert
        assertEquals(expected.getMessage(), actualMsg);
    }

    @Test
    public void testCreatePackage3() {
        // test data
        PackageEntity testData = createTestData();
        testData.setDuration(null);
        // expected return
        ResponseStatusException expected = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duration is invalid");
        String actualMsg = "";        // mock function
// call method
        try {
            ResponseEntity<PackageEntity> actual = controller.createPackage(testData);
        } catch (ResponseStatusException e) {
            actualMsg = e.getMessage();
        }
        // assert
        assertEquals(expected.getMessage(), actualMsg);
    }

    @Test
    public void testCreatePackage4() {
        // test data
        PackageEntity testData = createTestData();
        testData.setCurrency(null);
        // expected return
        ResponseStatusException expected = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency is a required field");
        String actualMsg = "";
        // mock function
        // call method
        try {
            ResponseEntity<PackageEntity> actual = controller.createPackage(testData);
        } catch (ResponseStatusException e) {
            actualMsg = e.getMessage();
        }
        // assert
        assertEquals(expected.getMessage(), actualMsg);
    }

    @Test
    public void testCreatePackage5() {
        // test data
        PackageEntity testData = createTestData();
        testData.setCurrency("");
        // expected return
        ResponseStatusException expected = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency is a required field");
        String actualMsg = "";
        // mock function
        // call method
        try {
            ResponseEntity<PackageEntity> actual = controller.createPackage(testData);
        } catch (ResponseStatusException e) {
            actualMsg = e.getMessage();
        }
        // assert
        assertEquals(expected.getMessage(), actualMsg);
    }

    @Test
    public void testCreatePackage6() {
        // test data
        PackageEntity testData = createTestData();
        testData.setPrice(-10.0);
        // expected return
        ResponseStatusException expected = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Price is invalid");
        String actualMsg = "";
        // mock function
        // call method
        try {
            ResponseEntity<PackageEntity> actual = controller.createPackage(testData);
        } catch (ResponseStatusException e) {
            actualMsg = e.getMessage();
        }
        // assert
        assertEquals(expected.getMessage(), actualMsg);
    }

    @Test
    public void testCreatePackage7() {
        // test data
        PackageEntity testData = createTestData();
        testData.setPrice(null);
        // expected return
        ResponseStatusException expected = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Price is invalid");
        String actualMsg = "";
        // mock function
        // call method
        try {
            ResponseEntity<PackageEntity> actual = controller.createPackage(testData);
        } catch (ResponseStatusException e) {
            actualMsg = e.getMessage();
        }
        // assert
        assertEquals(expected.getMessage(), actualMsg);
    }

    @Test
    public void testCreatePackage8() {
        // test data
        PackageEntity testData = createTestData();
        testData.setName(null);
        // expected return
        ResponseStatusException expected = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is a required field");
        String actualMsg = "";
        // mock function
        // call method
        try {
            ResponseEntity<PackageEntity> actual = controller.createPackage(testData);
        } catch (ResponseStatusException e) {
            actualMsg = e.getMessage();
        }
        // assert
        assertEquals(expected.getMessage(), actualMsg);
    }

    @Test
    public void testCreatePackage9() {
        // test data
        PackageEntity testData = createTestData();
        testData.setName("");
        // expected return
        ResponseStatusException expected = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is a required field");
        String actualMsg = "";
        // mock function
        // call method
        try {
            ResponseEntity<PackageEntity> actual = controller.createPackage(testData);
        } catch (ResponseStatusException e) {
            actualMsg = e.getMessage();
        }
        // assert
        assertEquals(expected.getMessage(), actualMsg);
    }

    @Test
    public void testCreatePackage10() {
        // test data
        PackageEntity testData = createTestData();
        testData.setNumOfPost(0);
        // expected return
        ResponseStatusException expected = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Number of Post is invalid");
        String actualMsg = "";
        // mock function
        // call method
        try {
            ResponseEntity<PackageEntity> actual = controller.createPackage(testData);
        } catch (ResponseStatusException e) {
            actualMsg = e.getMessage();
        }
        // assert
        assertEquals(expected.getMessage(), actualMsg);
    }

    @Test
    public void testCreatePackage11() {
        // test data
        PackageEntity testData = createTestData();
        testData.setNumOfPost(null);
        // expected return
        ResponseStatusException expected = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Number of Post is invalid");
        String actualMsg = "";
        // mock function
        // call method
        try {
            ResponseEntity<PackageEntity> actual = controller.createPackage(testData);
        } catch (ResponseStatusException e) {
            actualMsg = e.getMessage();
        }
        // assert
        assertEquals(expected.getMessage(), actualMsg);
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
        e.setDuration(30);
        return e;
    }
}
