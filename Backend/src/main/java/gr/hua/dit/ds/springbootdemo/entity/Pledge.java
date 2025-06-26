package gr.hua.dit.ds.springbootdemo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Pledge {

    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;

    // Amount
    @Column
    private Double amount;

    // Pledge date
    @Column
    private LocalDateTime pledgeDate;

    // Project
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    // Supporter
    @ManyToOne
    @JoinColumn(name = "supporter_id")
    private Supporter supporter;

    // Constructor
    public Pledge() {
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getPledgeDate() {
        return pledgeDate;
    }

    public void setPledgeDate(LocalDateTime pledgeDate) {
        this.pledgeDate = pledgeDate;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Supporter getSupporter() {
        return supporter;
    }

    public void setSupporter(Supporter supporter) {
        this.supporter = supporter;
    }
} 