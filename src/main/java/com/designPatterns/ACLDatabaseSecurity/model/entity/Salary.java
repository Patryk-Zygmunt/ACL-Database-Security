package com.designPatterns.ACLDatabaseSecurity.model.entity;

import com.designPatterns.ACLDatabaseSecurity.aspect.MyAspect;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Component
public class Salary {

    private long id;
    private double value;

    @Override
    public String toString() {
        return "Salary{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }

    // @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)@Id
	// @GeneratedValue(strategy = GenerationType.SEQUENCE,
	// generator="salaries_id_seq")
	// @SequenceGenerator(name="salaries_id_seq", sequenceName="salaries_id_seq",
	// allocationSize=1)
	// @Column(name = "SALARY_ID")
	// @Id
	// @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	@GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    @MyAspect
    public void setValue(double value) {
        this.value = value;
    }

}
