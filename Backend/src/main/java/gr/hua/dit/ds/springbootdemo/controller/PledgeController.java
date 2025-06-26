package gr.hua.dit.ds.springbootdemo.controller;

import gr.hua.dit.ds.springbootdemo.entity.Pledge;
import gr.hua.dit.ds.springbootdemo.service.PledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pledges")
public class PledgeController {

    @Autowired
    private PledgeService pledgeService;

    @PostMapping // Create pledge (SUPPORTER only)
    @PreAuthorize("hasRole('ROLE_SUPPORTER')")
    public Pledge createPledge(@RequestBody Pledge pledge) {
        return pledgeService.createPledge(pledge);
    }

    @GetMapping("/project/{projectId}") // Get project pledges  (PUBLIC)
    public List<Pledge> getProjectPledges(@PathVariable Integer projectId) {
        return pledgeService.getProjectPledges(projectId);
    }

    @GetMapping("/supporter/{supporterId}") // Get supporter pledges (SUPPORTER only)
    @PreAuthorize("hasRole('ROLE_SUPPORTER')")
    public List<Pledge> getSupporterPledges(@PathVariable Integer supporterId) {
        return pledgeService.getSupporterPledges(supporterId);
    }

    @DeleteMapping("/{id}") // Delete pledge (ADMIN only)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deletePledge(@PathVariable Integer id) {
        pledgeService.deletePledge(id);
        return ResponseEntity.ok().build();
    }
} 