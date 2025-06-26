package gr.hua.dit.ds.springbootdemo.service;

import gr.hua.dit.ds.springbootdemo.entity.Project;
import gr.hua.dit.ds.springbootdemo.entity.Creator;
import gr.hua.dit.ds.springbootdemo.entity.Pledge;
import gr.hua.dit.ds.springbootdemo.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Transactional
    public List<Project> getApprovedProjects() {
        return projectRepository.findByIsApproved(true);
    }

    @Transactional
    public List<Project> getPendingProjects() {
        return projectRepository.findByIsApproved(false);
    }

    @Transactional
    public List<Project> getCreatorProjects(Integer creatorId) {
        return projectRepository.findByCreatorId(creatorId);
    }

    @Transactional
    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    @Transactional
    public Project approveProject(Integer projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found"));
        project.setIsApproved(true);
        return projectRepository.save(project);
    }

    @Transactional
    public void deleteProject(Integer projectId) {
        projectRepository.deleteById(projectId);
    }

    @Transactional
    public Project getProject(Integer projectId) {
        return projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    @Transactional
    public void sendProjectUpdate(Integer projectId, String updateContent) {
        Project project = getProject(projectId);
        
        // Get all pledges for the project
        List<Pledge> pledges = project.getPledges();
        
        // Send email to each unique supporter
        Set<String> notifiedEmails = new HashSet<>();
        for (Pledge pledge : pledges) {
            String supporterEmail = pledge.getSupporter().getUser().getEmail();
            if (!notifiedEmails.contains(supporterEmail)) {
                emailService.sendProjectUpdateEmail(
                    supporterEmail,
                    project.getTitle(),
                    updateContent
                );
                notifiedEmails.add(supporterEmail);
            }
        }
    }
} 