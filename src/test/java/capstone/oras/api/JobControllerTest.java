package capstone.oras.api;

import capstone.oras.api.account.service.AccountService;
import capstone.oras.api.account.service.IAccountService;
import capstone.oras.api.job.controller.JobController;
import capstone.oras.api.job.service.IJobService;
import capstone.oras.api.job.service.JobService;
import capstone.oras.dao.IAccountRepository;
import capstone.oras.dao.IJobRepository;
import capstone.oras.entity.AccountEntity;
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

    @Mock
    private IJobRepository jobRepository;

    @Mock
    private IAccountRepository accountRepository;
//
//    @Mock
//    private

    @Mock
    private IJobService jobServiceMock;

    @Before
    public void init() {
        accountService = new AccountService(accountRepository);
        jobService = new JobService(jobRepository,accountService);
        jobController = new JobController(jobService);

    }

    @Test
    public void testCreateJob() {
        //test data
        JobEntity testData = createTestData();
        AccountEntity accountEntity = createAccountForTest();
        Optional<AccountEntity> account = Optional.of(accountEntity);
        // expected return
        ResponseEntity<JobEntity> expected = new ResponseEntity<>(testData, HttpStatus.OK);
        // mock function
        Mockito.when(accountRepository.findById(testData.getCreatorId())).thenReturn(account);
        Mockito.when(jobRepository.save(testData)).thenReturn(testData);
        Mockito.when(jobServiceMock.processJd(testData.getDescription())).thenReturn("asd");
        // call method
        ResponseEntity<JobEntity> actual = jobController.createJob(testData);
        // assert
        assertEquals(expected.getBody().getTitle(), actual.getBody().getTitle());
    }


    private JobEntity createTestData() {
        JobEntity e = new JobEntity();
        e.setTitle("New job");
        e.setDescription("This is a new job");
        e.setStatus(PUBLISHED);
        e.setApplyTo(LocalDateTime.parse("2020-12-23T17:00:00"));
        e.setVacancies(1);
        e.setSalaryHidden(true);
        e.setCurrency("USD");
        e.setCreatorId(1);
        e.setCategory("Category");
        e.setJobType("Full time");
        e.setSalaryFrom(500.0);
        e.setSalaryTo(1500.0);
        return e;
    }

    private AccountEntity createAccountForTest(){
        AccountEntity e = new AccountEntity();
        e.setActive(true);
        e.setCreateDate(LocalDateTime.parse("2020-12-23T17:00:00"));
        e.setPassword("123456");
        e.setFullname("abc doe");
        e.setEmail("abc");
        return e;
    }
}
