package gr.hua.dit.ds.springbootdemo.controller;

import gr.hua.dit.ds.springbootdemo.entity.Project;
import gr.hua.dit.ds.springbootdemo.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    
    @Autowired
    private ProjectService projectService;

    @GetMapping // List all approved projects (PUBLIC)
    public List<Project> getAllProjects() {
        return projectService.getApprovedProjects();
    }

    @GetMapping("/pending") // List all pending projects (ADMIN only)   
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Project> getPendingProjects() {
        return projectService.getPendingProjects();
    }

    @GetMapping("/{id}") // Get project by id (PUBLIC)  
    public Project getProject(@PathVariable Integer id) {
        return projectService.getProject(id);
    }

    @PostMapping // Create project (CREATOR only)   
    @PreAuthorize("hasRole('ROLE_CREATOR')")
    public Project createProject(@RequestBody Project project) {
        return projectService.saveProject(project);
    }

    @PutMapping("/{id}/approve") // Approve project (ADMIN only)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Project approveProject(@PathVariable Integer id) {
        return projectService.approveProject(id);
    }

    @DeleteMapping("/{id}") // Delete project (ADMIN only)  
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteProject(@PathVariable Integer id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{projectId}/updates")
    @PreAuthorize("hasRole('ROLE_CREATOR')")
    public ResponseEntity<?> createProjectUpdate(
            @PathVariable Integer projectId,
            @RequestBody Map<String, String> update) {
        try {
            projectService.sendProjectUpdate(projectId, update.get("content"));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send update: " + e.getMessage());
        }
    }
} 