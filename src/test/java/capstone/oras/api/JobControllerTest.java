package capstone.oras.api;

import capstone.oras.api.account.service.AccountService;
import capstone.oras.api.account.service.IAccountService;
import capstone.oras.api.activity.service.ActivityService;
import capstone.oras.api.activity.service.IActivityService;
import capstone.oras.api.job.controller.JobController;
import capstone.oras.api.job.service.IJobService;
import capstone.oras.api.job.service.JobService;
import capstone.oras.dao.IAccountRepository;
import capstone.oras.dao.IActivityRepository;
import capstone.oras.dao.IJobRepository;
import capstone.oras.entity.AccountEntity;
import capstone.oras.entity.ActivityEntity;
import capstone.oras.entity.JobEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static capstone.oras.common.Constant.JobStatus.PUBLISHED;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class JobControllerTest {
    private JobController jobController;
    private IJobService jobService;
    private IAccountService accountService;
    private IActivityService activityService;

    @Mock
    private IJobRepository jobRepository;

    @Mock
    private IAccountRepository accountRepository;

    @Mock
    private IActivityRepository activityRepository;
//
//    @Mock
//    private

    @Mock
    private IJobService jobServiceMock;

    @Before
    public void init() {
        accountService = new AccountService(accountRepository);
        jobService = new JobService(jobRepository,accountService);
        activityService = new ActivityService(activityRepository);
        jobController = new JobController(jobService,activityService);

    }

    @Test
    public void testCreateJob1() {
        //test data
        JobEntity testData = createTestData();
        Optional<AccountEntity> account = Optional.of(createAccountForTest());
        // expected return
        ResponseEntity<JobEntity> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(accountRepository.findById(1)).thenReturn(account);
        Mockito.when(jobRepository.save(testData)).thenReturn(testData);
        Mockito.when(activityRepository.save(new ActivityEntity())).thenReturn(new ActivityEntity());
        // call method
        ResponseEntity<JobEntity> actual = jobController.createJob(testData);
        // assert
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateJob2() {
        //test data
        JobEntity testData = createTestData();
        testData.setTitle(null);
        Optional<AccountEntity> account = Optional.of(createAccountForTest());
        // expected return
        ResponseEntity<JobEntity> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(accountRepository.findById(1)).thenReturn(account);
        Mockito.when(jobRepository.save(testData)).thenReturn(testData);
        Mockito.when(activityRepository.save(new ActivityEntity())).thenReturn(new ActivityEntity());
        // call method
        ResponseEntity<JobEntity> actual = jobController.createJob(testData);
        // assert
        assertEquals(expected, actual);
    }


    @Test
    public void testCreateJob3() {
        //test data
        JobEntity testData = createTestData();
        testData.setTitle("");
        Optional<AccountEntity> account = Optional.of(createAccountForTest());
        // expected return
        ResponseEntity<JobEntity> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(accountRepository.findById(1)).thenReturn(account);
        Mockito.when(jobRepository.save(testData)).thenReturn(testData);
        Mockito.when(activityRepository.save(new ActivityEntity())).thenReturn(new ActivityEntity());
        // call method
        ResponseEntity<JobEntity> actual = jobController.createJob(testData);
        // assert
        assertEquals(expected, actual);
    }


    @Test
    public void testCreateJob4() {
        //test data
        JobEntity testData = createTestData();
        testData.setDescription(null);
        Optional<AccountEntity> account = Optional.of(createAccountForTest());
        // expected return
        ResponseEntity<JobEntity> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(accountRepository.findById(1)).thenReturn(account);
        Mockito.when(jobRepository.save(testData)).thenReturn(testData);
        Mockito.when(activityRepository.save(new ActivityEntity())).thenReturn(new ActivityEntity());
        // call method
        ResponseEntity<JobEntity> actual = jobController.createJob(testData);
        // assert
        assertEquals(expected, actual);
    }


    @Test
    public void testCreateJob5() {
        //test data
        JobEntity testData = createTestData();
        testData.setDescription("");
        Optional<AccountEntity> account = Optional.of(createAccountForTest());
        // expected return
        ResponseEntity<JobEntity> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(accountRepository.findById(1)).thenReturn(account);
        Mockito.when(jobRepository.save(testData)).thenReturn(testData);
        Mockito.when(activityRepository.save(new ActivityEntity())).thenReturn(new ActivityEntity());
        // call method
        ResponseEntity<JobEntity> actual = jobController.createJob(testData);
        // assert
        assertEquals(expected, actual);
    }


    @Test
    public void testCreateJob6() {
        //test data
        JobEntity testData = createTestData();
        testData.setStatus(null);
        Optional<AccountEntity> account = Optional.of(createAccountForTest());
        // expected return
        ResponseEntity<JobEntity> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(accountRepository.findById(1)).thenReturn(account);
        Mockito.when(jobRepository.save(testData)).thenReturn(testData);
        Mockito.when(activityRepository.save(new ActivityEntity())).thenReturn(new ActivityEntity());
        // call method
        ResponseEntity<JobEntity> actual = jobController.createJob(testData);
        // assert
        assertEquals(expected, actual);
    }


    @Test
    public void testCreateJob7() {
        //test data
        JobEntity testData = createTestData();
        testData.setStatus("");
        Optional<AccountEntity> account = Optional.of(createAccountForTest());
        // expected return
        ResponseEntity<JobEntity> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(accountRepository.findById(1)).thenReturn(account);
        Mockito.when(jobRepository.save(testData)).thenReturn(testData);
        Mockito.when(activityRepository.save(new ActivityEntity())).thenReturn(new ActivityEntity());
        // call method
        ResponseEntity<JobEntity> actual = jobController.createJob(testData);
        // assert
        assertEquals(expected, actual);
    }


    @Test
    public void testCreateJob8() {
        //test data
        JobEntity testData = createTestData();
        testData.setApplyTo(null);
        Optional<AccountEntity> account = Optional.of(createAccountForTest());
        // expected return
        ResponseEntity<JobEntity> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(accountRepository.findById(1)).thenReturn(account);
        Mockito.when(jobRepository.save(testData)).thenReturn(testData);
        Mockito.when(activityRepository.save(new ActivityEntity())).thenReturn(new ActivityEntity());
        // call method
        ResponseEntity<JobEntity> actual = jobController.createJob(testData);
        // assert
        assertEquals(expected, actual);
    }


    @Test
    public void testCreateJob9() {
        //test data
        JobEntity testData = createTestData();
        testData.setVacancies(0);
        Optional<AccountEntity> account = Optional.of(createAccountForTest());
        // expected return
        ResponseEntity<JobEntity> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(accountRepository.findById(1)).thenReturn(account);
        Mockito.when(jobRepository.save(testData)).thenReturn(testData);
        Mockito.when(activityRepository.save(new ActivityEntity())).thenReturn(new ActivityEntity());
        // call method
        ResponseEntity<JobEntity> actual = jobController.createJob(testData);
        // assert
        assertEquals(expected, actual);
    }


    @Test
    public void testCreateJob10() {
        //test data
        JobEntity testData = createTestData();
        testData.setVacancies(null);
        Optional<AccountEntity> account = Optional.of(createAccountForTest());
        // expected return
        ResponseEntity<JobEntity> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(accountRepository.findById(1)).thenReturn(account);
        Mockito.when(jobRepository.save(testData)).thenReturn(testData);
        Mockito.when(activityRepository.save(new ActivityEntity())).thenReturn(new ActivityEntity());
        // call method
        ResponseEntity<JobEntity> actual = jobController.createJob(testData);
        // assert
        assertEquals(expected, actual);
    }


    @Test
    public void testCreateJob11() {
        //test data
        JobEntity testData = createTestData();
        testData.setSalaryHidden(null);
        Optional<AccountEntity> account = Optional.of(createAccountForTest());
        // expected return
        ResponseEntity<JobEntity> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(accountRepository.findById(1)).thenReturn(account);
        Mockito.when(jobRepository.save(testData)).thenReturn(testData);
        Mockito.when(activityRepository.save(new ActivityEntity())).thenReturn(new ActivityEntity());
        // call method
        ResponseEntity<JobEntity> actual = jobController.createJob(testData);
        // assert
        assertEquals(expected, actual);
    }


    @Test
    public void testCreateJob12() {
        //test data
        JobEntity testData = createTestData();
        testData.setCurrency(null);
        Optional<AccountEntity> account = Optional.of(createAccountForTest());
        // expected return
        ResponseEntity<JobEntity> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(accountRepository.findById(1)).thenReturn(account);
        Mockito.when(jobRepository.save(testData)).thenReturn(testData);
        Mockito.when(activityRepository.save(new ActivityEntity())).thenReturn(new ActivityEntity());
        // call method
        ResponseEntity<JobEntity> actual = jobController.createJob(testData);
        // assert
        assertEquals(expected, actual);
    }


    @Test
    public void testCreateJob13() {
        //test data
        JobEntity testData = createTestData();
        testData.setCreatorId(null);
        Optional<AccountEntity> account = Optional.of(createAccountForTest());
        // expected return
        ResponseEntity<JobEntity> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(accountRepository.findById(1)).thenReturn(account);
        Mockito.when(jobRepository.save(testData)).thenReturn(testData);
        Mockito.when(activityRepository.save(new ActivityEntity())).thenReturn(new ActivityEntity());
        // call method
        ResponseEntity<JobEntity> actual = jobController.createJob(testData);
        // assert
        assertEquals(expected, actual);
    }


    @Test
    public void testCreateJob14() {
        //test data
        JobEntity testData = createTestData();
        testData.setCreatorId(3);
        Optional<AccountEntity> account = Optional.of(createAccountForTest());
        // expected return
        ResponseEntity<JobEntity> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(accountRepository.findById(1)).thenReturn(account);
        Mockito.when(jobRepository.save(testData)).thenReturn(testData);
        Mockito.when(activityRepository.save(new ActivityEntity())).thenReturn(new ActivityEntity());
        // call method
        ResponseEntity<JobEntity> actual = jobController.createJob(testData);
        // assert
        assertEquals(expected, actual);
    }


    @Test
    public void testCreateJob15() {
        //test data
        JobEntity testData = createTestData();
        testData.setCategory(null);
        Optional<AccountEntity> account = Optional.of(createAccountForTest());
        // expected return
        ResponseEntity<JobEntity> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(accountRepository.findById(1)).thenReturn(account);
        Mockito.when(jobRepository.save(testData)).thenReturn(testData);
        Mockito.when(activityRepository.save(new ActivityEntity())).thenReturn(new ActivityEntity());
        // call method
        ResponseEntity<JobEntity> actual = jobController.createJob(testData);
        // assert
        assertEquals(expected, actual);
    }


    @Test
    public void testCreateJob16() {
        //test data
        JobEntity testData = createTestData();
        testData.setJobType(null);
        Optional<AccountEntity> account = Optional.of(createAccountForTest());
        // expected return
        ResponseEntity<JobEntity> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(accountRepository.findById(1)).thenReturn(account);
        Mockito.when(jobRepository.save(testData)).thenReturn(testData);
        Mockito.when(activityRepository.save(new ActivityEntity())).thenReturn(new ActivityEntity());
        // call method
        ResponseEntity<JobEntity> actual = jobController.createJob(testData);
        // assert
        assertEquals(expected, actual);
    }


    @Test
    public void testCreateJob17() {
        //test data
        JobEntity testData = createTestData();
        testData.setSalaryFrom(500.0);
        Optional<AccountEntity> account = Optional.of(createAccountForTest());
        // expected return
        ResponseEntity<JobEntity> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(accountRepository.findById(1)).thenReturn(account);
        Mockito.when(jobRepository.save(testData)).thenReturn(testData);
        Mockito.when(activityRepository.save(new ActivityEntity())).thenReturn(new ActivityEntity());
        // call method
        ResponseEntity<JobEntity> actual = jobController.createJob(testData);
        // assert
        assertEquals(expected, actual);
    }


    @Test
    public void testCreateJob18() {
        //test data
        JobEntity testData = createTestData();
        testData.setSalaryFrom(0.0);
        Optional<AccountEntity> account = Optional.of(createAccountForTest());
        // expected return
        ResponseEntity<JobEntity> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(accountRepository.findById(1)).thenReturn(account);
        Mockito.when(jobRepository.save(testData)).thenReturn(testData);
        Mockito.when(activityRepository.save(new ActivityEntity())).thenReturn(new ActivityEntity());
        // call method
        ResponseEntity<JobEntity> actual = jobController.createJob(testData);
        // assert
        assertEquals(expected, actual);
    }


    @Test
    public void testCreateJob19() {
        //test data
        JobEntity testData = createTestData();
        testData.setSalaryFrom(null);
        Optional<AccountEntity> account = Optional.of(createAccountForTest());
        // expected return
        ResponseEntity<JobEntity> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(accountRepository.findById(1)).thenReturn(account);
        Mockito.when(jobRepository.save(testData)).thenReturn(testData);
        Mockito.when(activityRepository.save(new ActivityEntity())).thenReturn(new ActivityEntity());
        // call method
        ResponseEntity<JobEntity> actual = jobController.createJob(testData);
        // assert
        assertEquals(expected, actual);
    }


    @Test
    public void testCreateJob20() {
        //test data
        JobEntity testData = createTestData();
        testData.setSalaryTo(0.0);
        Optional<AccountEntity> account = Optional.of(createAccountForTest());
        // expected return
        ResponseEntity<JobEntity> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(accountRepository.findById(1)).thenReturn(account);
        Mockito.when(jobRepository.save(testData)).thenReturn(testData);
        Mockito.when(activityRepository.save(new ActivityEntity())).thenReturn(new ActivityEntity());
        // call method
        ResponseEntity<JobEntity> actual = jobController.createJob(testData);
        // assert
        assertEquals(expected, actual);
    }


    @Test
    public void testCreateJob21() {
        //test data
        JobEntity testData = createTestData();
        testData.setSalaryTo(null);
        Optional<AccountEntity> account = Optional.of(createAccountForTest());
        // expected return
        ResponseEntity<JobEntity> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(accountRepository.findById(1)).thenReturn(account);
        Mockito.when(jobRepository.save(testData)).thenReturn(testData);
        Mockito.when(activityRepository.save(new ActivityEntity())).thenReturn(new ActivityEntity());
        // call method
        ResponseEntity<JobEntity> actual = jobController.createJob(testData);
        // assert
        assertEquals(expected, actual);
    }




    private JobEntity createTestData() {
        JobEntity e = new JobEntity();
        e.setTitle("Tester");
        e.setDescription("Description");
        e.setStatus(PUBLISHED);
        e.setApplyTo(LocalDateTime.parse("2020-12-23T17:00:00"));
        e.setVacancies(1);
        e.setSalaryHidden(true);
        e.setCurrency("USD");
        e.setCreatorId(1);
        e.setCategory("Coding");
        e.setJobType("Full time");
        e.setSalaryFrom(100.0);
        e.setSalaryTo(200.0);
        return e;
    }

    private AccountEntity createAccountForTest(){
        AccountEntity e = new AccountEntity();
        e.setActive(true);
        e.setCreateDate(LocalDateTime.parse("2020-12-23T17:00:00"));
        e.setPassword("123456");
        e.setFullname("abc doe");
        e.setEmail("abc");
        e.setId(1);
        return e;
    }
}
