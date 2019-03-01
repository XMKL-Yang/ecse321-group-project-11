package ca.mcgill.ecse321.cooperator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.test.context.junit4.SpringRunner;

import ca.mcgill.ecse321.cooperator.controller.CooperatorRestController;

import ca.mcgill.ecse321.cooperator.service.CooperatorService;

import ca.mcgill.ecse321.cooperator.model.*;

import ca.mcgill.ecse321.cooperator.dao.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CooperatorEmployerTests {
	@Test
	public void testJUnit4() {
		
	}
	
	@Mock
	private EmployerRepository employerDao;
	
	@InjectMocks
	private CooperatorService service;
	@InjectMocks
	private CooperatorRestController controller;
	
	private Employer employer;
	
	private static final int VALID_EMPLOYER_KEY = 1;
	private static final int INVALID_EMPLOYER_KEY = 3;
	
	@Before
	public void setMockOutput() {
		when(employerDao.findEmployerByUserId(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(VALID_EMPLOYER_KEY)) {
				Employer employer = new Employer();
				employer.setUserId(VALID_EMPLOYER_KEY);
				return employer;
			} else {
				return null;
			}
		});
	}
	
	@Before
	public void setupMock() {
		employer = mock(Employer.class);
	}
	
	@Test
	public void testEmployerCreation() {
		assertNotNull(employer);
	}
	
	@Test
	public void testEmployerQueryFound() {
		assertEquals(VALID_EMPLOYER_KEY, service.getEmployer(VALID_EMPLOYER_KEY).getUserId());
	}
	
	@Test
	public void testEmployerQueryNotFound() {
		assertNull(service.getEmployer(INVALID_EMPLOYER_KEY));
	}
}