package gr.hua.dit.ds.springbootdemo.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;


@Entity
public class Project {

    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;

    // Title
    @Column
    private String title;

    // Description
    @Column(length = 1000)
    private String description;

    // Funding goal
    @Column
    private Double fundingGoal;

    // Current funding
    @Column
    private Double currentFunding = 0.0;

    // Deadline
    private LocalDateTime deadline;

    // Approved
    @Column
    private Boolean isApproved = false;

    // Creator
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Creator creator;

    // Pledges
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Pledge> pledges;

    public Project() {
    }

    public Project(String title, String description, Double fundingGoal, LocalDateTime deadline, Creator creator) {
        this.title = title;
        this.description = description;
        this.fundingGoal = fundingGoal;
        this.deadline = deadline;
        this.creator = creator;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getFundingGoal() {
        return fundingGoal;
    }

    public void setFundingGoal(Double fundingGoal) {
        this.fundingGoal = fundingGoal;
    }

    public Double getCurrentFunding() {
        return currentFunding;
    }

    public void setCurrentFunding(Double currentFunding) {
        this.currentFunding = currentFunding;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public List<Pledge> getPledges() {
        return pledges;
    }

    public void setPledges(List<Pledge> pledges) {
        this.pledges = pledges;
    }

    public void addPledge(Pledge pledge) {
        if (pledges == null) {
            pledges = new ArrayList<>();
        }
        pledges.add(pledge);
        currentFunding += pledge.getAmount(); // Automatically updates funding
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", fundingGoal=" + fundingGoal +
                ", currentFunding=" + currentFunding +
                ", deadline=" + deadline +
                ", isApproved=" + isApproved +
                ", creator=" + creator +
                ", pledges=" + pledges +
                '}';
    }
}
