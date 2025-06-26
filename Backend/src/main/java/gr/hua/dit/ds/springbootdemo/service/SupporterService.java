package gr.hua.dit.ds.springbootdemo.service;

import gr.hua.dit.ds.springbootdemo.entity.Supporter;
import gr.hua.dit.ds.springbootdemo.entity.User;
import gr.hua.dit.ds.springbootdemo.repository.SupporterRepository;
import gr.hua.dit.ds.springbootdemo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupporterService {
    @Autowired
    private SupporterRepository supporterRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Supporter createSupporter(Supporter supporter, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        supporter.setUser(user);
        return supporterRepository.save(supporter);
    }

    @Transactional
    public List<Supporter> getAllSupporters() {
        return supporterRepository.findAll();
    }

    @Transactional
    public Supporter getSupporter(Integer supporterId) {
        return supporterRepository.findById(supporterId)
            .orElseThrow(() -> new RuntimeException("Supporter not found"));
    }

    @Transactional
    public Supporter getSupporterByUser(Long userId) {
        return supporterRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Supporter not found for user"));
    }

    @Transactional
    public void deleteSupporter(Integer supporterId) {
        supporterRepository.deleteById(supporterId);
    }
} 