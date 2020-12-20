package capstone.oras.api;

import capstone.oras.api.account.controller.AccountController;
import capstone.oras.api.account.service.AccountService;
import capstone.oras.api.account.service.IAccountService;
import capstone.oras.dao.IAccountRepository;
import capstone.oras.entity.AccountEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {
    private AccountController controller;
    private IAccountService accountService;

    @Mock
    private IAccountRepository IAccountRepository;

    @Before
    public void init() {
        accountService = new AccountService(new BCryptPasswordEncoder(), IAccountRepository);
        controller = new AccountController(accountService , new BCryptPasswordEncoder());
    }

    @Test
    public void testCreatePackage() {
        // test data
        AccountEntity testData = createTestData();
        // expected return
        ResponseEntity<AccountEntity> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(IAccountRepository.save(testData)).thenReturn(testData);
        // call method
        ResponseEntity<AccountEntity> actual = controller.createAccount(testData);
        // assert
        assertEquals(expected, actual);
    }

    @Test
    public void testCreatePackage1() {
        // test data
        AccountEntity testData = createTestData();
        testData.setEmail(null);
        // expected return
        ResponseStatusException expected = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is a required field");
        String actualMsg = "";
        // call method
        try {
            ResponseEntity<AccountEntity> actual = controller.createAccount(testData);

        } catch (ResponseStatusException e) {
            actualMsg = e.getMessage();
        }
        // assert
        assertEquals(expected.getMessage(), actualMsg);
    }

    @Test
    public void testCreatePackage6() {
        // test data
        AccountEntity testData = createTestData();
        testData.setEmail("");
        // expected return
        ResponseStatusException expected = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is a required field");
        String actualMsg = "";
        // call method
        try {
            ResponseEntity<AccountEntity> actual = controller.createAccount(testData);

        } catch (ResponseStatusException e) {
            actualMsg = e.getMessage();
        }
        // assert
        assertEquals(expected.getMessage(), actualMsg);
    }

    @Test
    public void testCreatePackage2() {
        // test data
        AccountEntity testData = createTestData();
        testData.setFullname(null);
        // expected return
        ResponseStatusException expected = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Full name is a required field");
        String actualMsg = "";
        // call method
        try {
            ResponseEntity<AccountEntity> actual = controller.createAccount(testData);

        } catch (ResponseStatusException e) {
            actualMsg = e.getMessage();
        }
        // assert
        assertEquals(expected.getMessage(), actualMsg);
    }
    @Test
    public void testCreatePackage7() {
        // test data
        AccountEntity testData = createTestData();
        testData.setFullname("");
        // expected return
        ResponseStatusException expected = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Full name is a required field");
        String actualMsg = "";
        // call method
        try {
            ResponseEntity<AccountEntity> actual = controller.createAccount(testData);

        } catch (ResponseStatusException e) {
            actualMsg = e.getMessage();
        }
        // assert
        assertEquals(expected.getMessage(), actualMsg);
    }
    @Test
    public void testCreatePackage3() {
        // test data
        AccountEntity testData = createTestData();
        testData.setPassword(null);
        // expected return
        ResponseStatusException expected = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is a required field");
        String actualMsg = "";
        // call method
        try {
            ResponseEntity<AccountEntity> actual = controller.createAccount(testData);

        } catch (ResponseStatusException e) {
            actualMsg = e.getMessage();
        }
        // assert
        assertEquals(expected.getMessage(), actualMsg);
    }
    @Test
    public void testCreatePackage8() {
        // test data
        AccountEntity testData = createTestData();
        testData.setPassword("");
        // expected return
        ResponseStatusException expected = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is a required field");
        String actualMsg = "";
        // call method
        try {
            ResponseEntity<AccountEntity> actual = controller.createAccount(testData);

        } catch (ResponseStatusException e) {
            actualMsg = e.getMessage();
        }
        // assert
        assertEquals(expected.getMessage(), actualMsg);
    }
    @Test
    public void testCreatePackage4() {
        //test data
        AccountEntity testData = createTestData();
        Optional<AccountEntity> account = Optional.of(testData);
        // expected return
        ResponseStatusException expected = new ResponseStatusException(HttpStatus.BAD_REQUEST, "This email is already registered");
        String actualMsg = "";
        // mock function
        Mockito.lenient().when(IAccountRepository.findAccountEntitiesByEmailEquals(testData.getEmail())).thenReturn(account);
        // call method
        try {
            ResponseEntity<AccountEntity> actual = controller.createAccount(testData);

        } catch (ResponseStatusException e) {
            actualMsg = e.getMessage();
        }
        // assert
        assertEquals(expected.getMessage(), actualMsg);
    }
    @Test
    public void testCreatePackage5() {
        //test data
        AccountEntity testData = createTestData();
        Optional<AccountEntity> account = Optional.of(testData);
        // expected return
        ResponseStatusException expected = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account already exist");
        String actualMsg = "";
        // mock function
        Mockito.lenient().when(IAccountRepository.findById(account.get().getId())).thenReturn(account);
        // call method
        try {
            testData.setEmail("test2@gmail.com");
            ResponseEntity<AccountEntity> actual = controller.createAccount(testData);

        } catch (ResponseStatusException e) {
            actualMsg = e.getMessage();
        }
        // assert
        assertEquals(expected.getMessage(), actualMsg);
    }

    private AccountEntity createTestData() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setEmail("test@gmail.com");
        accountEntity.setConfirmMail(true);
        accountEntity.setPassword("test");
        accountEntity.setActive(true);
        accountEntity.setCreateDate(LocalDateTime.now());
        accountEntity.setFullname("test");
        accountEntity.setPhoneNo("0123456789");
        accountEntity.setRole("user");
        return accountEntity;
    }

}
