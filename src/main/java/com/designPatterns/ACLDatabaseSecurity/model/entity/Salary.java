package com.designPatterns.ACLDatabaseSecurity.model.entity;

import com.designPatterns.ACLDatabaseSecurity.aop.ProtectedEntity;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.function.BiConsumer;

@ProtectedEntity(mode = "sud")
@Entity
@NamedQuery(name = "Salary.findUsingNamedQueryByValueBefore",
        query = "select s from Salary s where s.value < ?1")
public class Salary {
    @Transient
    BiConsumer selectInjection;

    private long id;
    private double value;

    public Salary(long id, double value) {
        this.id = id;
        this.value = value;
    }

    public Salary() {
    }

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
