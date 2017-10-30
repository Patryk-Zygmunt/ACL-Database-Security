package com.designPatterns.ACLDatabaseSecurity.aop;

import org.reflections.Reflections;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.Root;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@Scope("singleton")
public class GuardedEntitiesHolder {
    private Set<String> selectProtectedEntities = null;
    private Set<String> updateProtectedEntities = null;
    private Set<String> deleteProtectedEntities = null;

    enum Modes{
        SELECT("s"), UPDATE("u"), DELETE("d");

        private String value;

        Modes(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @PostConstruct
    public void findAnnotatedEntities(){
        Reflections reflections = new Reflections("com.designPatterns.ACLDatabaseSecurity.model.entity");
        Set<Class<?>> annotated =  reflections.getTypesAnnotatedWith(ProtectedEntity.class);
        setSelect(annotated);
        setDelete(annotated);
        setUpdate(annotated);
    }

    private void setSelect(Set<Class<?>> annotated){
         selectProtectedEntities = filterEntityAnnotatedWithMode(annotated, Modes.SELECT);
    }

    private void setUpdate(Set<Class<?>> annotated){
        updateProtectedEntities = filterEntityAnnotatedWithMode(annotated, Modes.UPDATE);
    }

    private void setDelete(Set<Class<?>> annotated){
        deleteProtectedEntities = filterEntityAnnotatedWithMode(annotated, Modes.UPDATE);
    }

    private Set<String> filterEntityAnnotatedWithMode(Set<Class<?>> annotated, Modes mode){
        return annotated.stream().
                filter(x -> getAnnotationModes(x).contains(mode.getValue())).
                map(Class::getSimpleName)
                .collect(Collectors.toSet());
    }

    private String getAnnotationModes(Class<?> c){
        return c.getAnnotation(ProtectedEntity.class).mode();
    }

    public boolean isRootSelectProtected(Root root){
        return isEntityProtected(root.getModel().getName());
    }

    public boolean isEntityProtected(String name){
        return selectProtectedEntities.stream().anyMatch(x -> x.equals(name));
    }
}
