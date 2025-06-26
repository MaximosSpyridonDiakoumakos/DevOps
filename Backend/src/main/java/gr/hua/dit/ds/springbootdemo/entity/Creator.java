package gr.hua.dit.ds.springbootdemo.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Creator {

    // Primary key  
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;

    // Bio  
    @Column
    private String bio;

    // Projects
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<Project> projects;

    // User
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    // Constructor
    public Creator() {
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
} 