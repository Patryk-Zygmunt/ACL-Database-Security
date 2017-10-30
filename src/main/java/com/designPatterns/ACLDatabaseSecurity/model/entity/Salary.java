package com.designPatterns.ACLDatabaseSecurity.model.entity;

import com.designPatterns.ACLDatabaseSecurity.aop.ProtectedEntity;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@ProtectedEntity(mode = "s")
@Entity
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

    public void setValue(double value) {
        this.value = value;
    }

}
