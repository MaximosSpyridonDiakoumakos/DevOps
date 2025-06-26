package gr.hua.dit.ds.springbootdemo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Supporter {

    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;
    
    // Pledges
    @OneToMany(mappedBy = "supporter", cascade = CascadeType.ALL)
    private List<Pledge> pledges;

    // User
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    // Constructor
    public Supporter() {
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Pledge> getPledges() {
        return pledges;
    }

    public void setPledges(List<Pledge> pledges) {
        this.pledges = pledges;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
