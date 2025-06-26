package gr.hua.dit.ds.springbootdemo.service;

import com.github.javafaker.Faker;
import gr.hua.dit.ds.springbootdemo.entity.*;
import gr.hua.dit.ds.springbootdemo.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class InitialDataService {
    private static final int CREATORS_COUNT = 5;
    private static final int SUPPORTERS_COUNT = 10;
    private static final int PROJECTS_PER_CREATOR = 2;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CreatorRepository creatorRepository;
    private final SupporterRepository supporterRepository;
    private final ProjectRepository projectRepository;
    private final PasswordEncoder passwordEncoder;

    public InitialDataService(UserRepository userRepository,
                            RoleRepository roleRepository,
                            CreatorRepository creatorRepository,
                            SupporterRepository supporterRepository,
                            ProjectRepository projectRepository,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.creatorRepository = creatorRepository;
        this.supporterRepository = supporterRepository;
        this.projectRepository = projectRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    private void createUsersAndRoles() {
        // First ensure all roles exist and store them
        final List<String> rolesToCreate = List.of(
            "ROLE_ADMIN", 
            "ROLE_CREATOR", 
            "ROLE_SUPPORTER"
        );
        
        // Create all roles first
        for (final String roleName : rolesToCreate) {
            if (!roleRepository.findByName(roleName).isPresent()) {
                Role role = new Role(roleName);
                roleRepository.save(role);
            }
        }
        
        // Fetch the ADMIN role after ensuring it exists
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
            .orElseThrow(() -> new RuntimeException("Admin role not found after creation"));
        
        // Create admin user if it doesn't exist
        if (!userRepository.findByUsername("admin").isPresent()) {
            User adminUser = new User("admin", "admin@crowdfund.com", 
                                   passwordEncoder.encode("1234"));
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            adminUser.setRoles(roles);
            userRepository.save(adminUser);
        }
    }

    private void createCreatorUsersAndProfiles() {
        Role creatorRole = roleRepository.findByName("ROLE_CREATOR").orElseThrow();
        
        for (int i = 1; i <= CREATORS_COUNT; i++) {
            final Faker faker = new Faker(new Random(i));
            final String firstName = faker.name().firstName();
            final String lastName = faker.name().lastName();
            final String email = faker.internet().emailAddress();
            
            // First create the user account for authentication
            User user = new User("creator" + i, email, passwordEncoder.encode("1234"));
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setRoles(Set.of(creatorRole));
            userRepository.save(user);

            // Then create the creator profile with business data
            Creator creator = new Creator();
            creator.setBio(faker.lorem().paragraph());
            creator.setUser(user);
            creatorRepository.save(creator);
        }
    }

    private void createSupporters() {
        Role supporterRole = roleRepository.findByName("ROLE_SUPPORTER").orElseThrow();
        
        for (int i = 1; i <= SUPPORTERS_COUNT; i++) {
            final Faker faker = new Faker(new Random(i));
            final String firstName = faker.name().firstName();
            final String lastName = faker.name().lastName();
            final String email = faker.internet().emailAddress();
            
            User user = new User(
                "supporter" + i,
                email,
                passwordEncoder.encode("1234")
            );
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setRoles(Set.of(supporterRole));
            userRepository.save(user);

            Supporter supporter = new Supporter();
            supporter.setUser(user);
            supporterRepository.save(supporter);
        }
    }

    private void createProjects() {
        List<Creator> creators = creatorRepository.findAll();
        
        for (Creator creator : creators) {
            for (int i = 1; i <= PROJECTS_PER_CREATOR; i++) {
                final Faker faker = new Faker(new Random(creator.getId() * 10 + i));
                
                Project project = new Project(
                    faker.commerce().productName(),
                    faker.lorem().paragraph(),
                    faker.number().randomDouble(2, 1000, 100000),
                    LocalDateTime.now().plusMonths(3),
                    creator
                );
                project.setIsApproved(faker.random().nextBoolean());
                projectRepository.save(project);
            }
        }
    }

    @PostConstruct
    public void setup() {
        try {
            // Check if admin user exists first
            if (!userRepository.findByUsername("admin").isPresent()) {
                this.createUsersAndRoles();
                this.createCreatorUsersAndProfiles();
                this.createSupporters();
                this.createProjects();
            }
        } catch (Exception e) {
            // Log error but don't prevent application startup
            System.err.println("Error during data initialization: " + e.getMessage());
        }
    }
}
