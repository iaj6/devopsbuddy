package com.devopsbuddy;

import com.devopsbuddy.backend.persistence.domain.backend.Plan;
import com.devopsbuddy.backend.persistence.domain.backend.Role;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.devopsbuddy.backend.persistence.domain.repositories.PlanRepository;
import com.devopsbuddy.backend.persistence.domain.repositories.RoleRepository;
import com.devopsbuddy.backend.persistence.domain.repositories.UserRepository;
import com.devopsbuddy.web.i18n.I18NService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DevopsbuddyApplication.class)
@WebAppConfiguration
public class DevopsbuddyApplicationTests {

	@Autowired
	private I18NService i18NService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PlanRepository planRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Test
	public void testMessageByLocaleService() throws Exception {
		String expectedResult = "Bootstrap starter template";
		String messageId = "index.main.callout";
		String actual = i18NService.getMessage(messageId);
		Assert.assertEquals("The actual and expected Strings don't match", expectedResult, actual);
	}


	@Test
	public void testCreateUser() throws Exception {

		//First we create a plan
		Plan basicPlan = createBasicPlan();
		planRepository.save(basicPlan);
		basicPlan = planRepository.findOne(basicPlan.getId());
		Assert.assertNotNull(basicPlan);

		User user = createBasicUser();
		user.setPlan(basicPlan);

		Role basicRole = createBasicRole();
		Set<UserRole> userRoles = new HashSet<>();
		UserRole basicUserRole = new UserRole();
		basicUserRole.setRole(basicRole);
		basicUserRole.setUser(user);
		userRoles.add(basicUserRole);

		user.getUserRoles().addAll(userRoles);

		for (UserRole userRole : userRoles) {
			roleRepository.save(userRole.getRole());
		}

		user = userRepository.save(user);
		Assert.assertNotNull(user);
		Assert.assertNotNull(user.getId());


	}

	private Role createBasicRole() {

		Role role = new Role();
		role.setId(1);
		role.setName("ROLE_USER");
		return role;
	}

	private User createBasicUser() {

		User user = new User();
		user.setUsername("basicUser");
		user.setPassword("secret");
		user.setEmail("me@example.com");
		user.setFirstName("firstName");
		user.setLastName("lastName");
		user.setPhoneNumber("123456789123");
		user.setCountry("GB");
		user.setEnabled(true);
		user.setDescription("A basic user");
		user.setProfileImageUrl("https://blabla.images.com/basicuser");

		return user;
	}

	private Plan createBasicPlan() {
		Plan plan = new Plan();
		plan.setId(1);
		plan.setName("Basic");
		return plan;
	}

}
