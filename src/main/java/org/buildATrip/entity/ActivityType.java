package org.buildATrip.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "activityType")
public class ActivityType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_type_id")
    private int id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;  // SIGHTS, BEACH_PARK,...

    @ManyToMany(mappedBy = "category")
    private Set<PointOfInterest> pois;

    // Constructors
    public ActivityType() {}

    public ActivityType(String name) {
        this.name = name;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
