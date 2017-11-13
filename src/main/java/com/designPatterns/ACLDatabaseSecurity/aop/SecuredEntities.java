package com.designPatterns.ACLDatabaseSecurity.aop;

import org.reflections.Reflections;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.Root;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Ładowanie nazwy oznaczonego entities do odpowiedniego seta w zależności od wybranego typu zabezpieczenia.
 * Do wyboru select (s), update (u), delete (d)
 */
@Component
@Scope("singleton")
public class SecuredEntities {
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
        deleteProtectedEntities = filterEntityAnnotatedWithMode(annotated, Modes.DELETE);
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
        return isRootProtected(root, selectProtectedEntities);
    }

    public boolean isRootUpdateProtected(Root root){
        return isRootProtected(root, updateProtectedEntities);
    }

    public boolean isRootDeleteProtected(Root root){
        return isRootProtected(root, deleteProtectedEntities);
    }

    public boolean isRootSelectProtected(String name){
        return isRootProtected(name, selectProtectedEntities);
    }

    public boolean isRootUpdateProtected(String name){
        return isRootProtected(name, updateProtectedEntities);
    }

    public boolean isRootDeleteProtected(String name){
        return isRootProtected(name, deleteProtectedEntities);
    }

    private boolean isRootProtected(Root root, Set<String> protectedRoots){
        return protectedRoots.stream().anyMatch(x -> x.equals(root.getModel().getName()));
    }

    private boolean isRootProtected(String name, Set<String> protectedRoots){
        return protectedRoots.stream().anyMatch(x->x.equals(name));
    }

    public boolean isRootProtected(String name, String action){
        switch (action){
            case "select" : return isRootSelectProtected(name);
            case "update" : return isRootUpdateProtected(name);
            case "delete" : return isRootDeleteProtected(name);
            default: return false;
        }
    }
}
