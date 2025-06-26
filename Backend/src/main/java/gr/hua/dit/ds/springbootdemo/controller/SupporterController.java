package gr.hua.dit.ds.springbootdemo.controller;

import gr.hua.dit.ds.springbootdemo.entity.Pledge;
import gr.hua.dit.ds.springbootdemo.entity.Supporter;
import gr.hua.dit.ds.springbootdemo.service.PledgeService;
import gr.hua.dit.ds.springbootdemo.service.SupporterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supporters")
public class SupporterController {

    @Autowired
    private SupporterService supporterService;
    
    @Autowired
    private PledgeService pledgeService;

    @GetMapping // List all supporters (ADMIN only)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Supporter> getAllSupporters() {
        return supporterService.getAllSupporters();
    }

    @GetMapping("/{id}") // Get supporter by id
    public Supporter getSupporter(@PathVariable Integer id) {
        return supporterService.getSupporter(id);
    }

    @GetMapping("/{id}/pledges") // Get supporter's pledges
    @PreAuthorize("hasRole('ROLE_SUPPORTER') or hasRole('ROLE_ADMIN')")
    public List<Pledge> getSupporterPledges(@PathVariable Integer id) {
        return pledgeService.getSupporterPledges(id);
    }

    @PostMapping // Create supporter (SUPPORTER only)
    @PreAuthorize("hasRole('ROLE_SUPPORTER')")
    public Supporter createSupporter(@RequestBody Supporter supporter, @RequestParam Long userId) {
        return supporterService.createSupporter(supporter, userId);
    }

    @DeleteMapping("/{id}") // Delete supporter (ADMIN only)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteSupporter(@PathVariable Integer id) {
        supporterService.deleteSupporter(id);
        return ResponseEntity.ok().build();
    }
} 