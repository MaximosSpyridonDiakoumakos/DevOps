package gr.hua.dit.ds.springbootdemo.service;

import gr.hua.dit.ds.springbootdemo.entity.Creator;
import gr.hua.dit.ds.springbootdemo.entity.User;
import gr.hua.dit.ds.springbootdemo.repository.CreatorRepository;
import gr.hua.dit.ds.springbootdemo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreatorService {
    @Autowired
    private CreatorRepository creatorRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Creator createCreator(Creator creator, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        creator.setUser(user);
        return creatorRepository.save(creator);
    }

    @Transactional
    public List<Creator> getAllCreators() {
        return creatorRepository.findAll();
    }

    @Transactional
    public Creator getCreator(Integer creatorId) {
        return creatorRepository.findById(creatorId)
            .orElseThrow(() -> new RuntimeException("Creator not found"));
    }

    @Transactional
    public Creator getCreatorByUser(Long userId) {
        return creatorRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Creator not found for user"));
    }

    @Transactional
    public void deleteCreator(Integer creatorId) {
        creatorRepository.deleteById(creatorId);
    }
} 