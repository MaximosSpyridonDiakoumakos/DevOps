package gr.hua.dit.ds.springbootdemo.controller;

import gr.hua.dit.ds.springbootdemo.entity.Creator;
import gr.hua.dit.ds.springbootdemo.entity.Project;
import gr.hua.dit.ds.springbootdemo.service.CreatorService;
import gr.hua.dit.ds.springbootdemo.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/creators")
public class CreatorController {

    @Autowired
    private CreatorService creatorService;
    
    @Autowired
    private ProjectService projectService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')") // List all creators (ADMIN only)
    public List<Creator> getAllCreators() {
        return creatorService.getAllCreators();
    }

    @GetMapping("/{id}") // Get creator by id
    public Creator getCreator(@PathVariable Integer id) {
        return creatorService.getCreator(id);
    }

    @GetMapping("/{id}/projects") // Get creator's projects
    public List<Project> getCreatorProjects(@PathVariable Integer id) {
        return projectService.getCreatorProjects(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CREATOR')") // Create creator (CREATOR only)    
    public Creator createCreator(@RequestBody Creator creator, @RequestParam Long userId) {
        return creatorService.createCreator(creator, userId);
    }
    
    @DeleteMapping("/{id}") // Delete creator (ADMIN only)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteCreator(@PathVariable Integer id) {
        creatorService.deleteCreator(id);
        return ResponseEntity.ok().build();
    }
} 