package com.designPatterns.ACLDatabaseSecurity.aop;
import com.google.common.collect.Maps;
import org.hibernate.jpa.criteria.CriteriaQueryImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;
import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Scope("singleton")
public class SecurityInjections {

    enum Type{
        SELECT("Select"), UPDATE("Update"), DELETE("Delete");

        private String val;

        Type(String val) {
            this.val = val;
        }

        public String getVal() {
            return val;
        }
    }

    private Map<String, BiConsumer<Object, Root>> criteriaMap = Maps.newHashMap();
    private Map<String, Function<String, String>> sqlMap = Maps.newHashMap();

    @PostConstruct
    private void postConstructor(){
       criteriaMap.put("defaultSelect", (query, root) -> {});
       criteriaMap.put("defaultUpdate", (query, root) -> {});
       criteriaMap.put("defaultDelete", (query, root) -> {});

       sqlMap.put("defaultSelect", (query) -> query);
       sqlMap.put("defaultUpdate", (query) -> query);
       sqlMap.put("defaultDelete", (query) -> query);
    }

    public void injectToQuery(CriteriaQueryImpl query, Root root){
        String name = root.getModel().getName() + "Select";
        Optional.ofNullable(criteriaMap.get(name)).orElse(criteriaMap.get("defaultSelect")).accept(query,root);
    }
     public void injectToQuery(CriteriaUpdate query){
        Root root = query.getRoot();
        String name = root.getModel().getName()+"Update";
        Optional.ofNullable(criteriaMap.get(name)).orElse(criteriaMap.get("defaultUpdate")).accept(query,root);
    }
     public void injectToQuery(CriteriaDelete query){
         Root root = query.getRoot();
         String name = root.getModel().getName()+"Delete";
         Optional.ofNullable(criteriaMap.get(name)).orElse(criteriaMap.get("defaultDelete")).accept(query,root);
    }

    public String injectToQuery(String query){
        String action = StringUtils.capitalize(query.substring(0, query.indexOf(' ')).toLowerCase());
        String key = getTableName(query) + action;
        return sqlMap.get(key).apply(query);
    }

    private String getTableName(String sql){
        Pattern p = Pattern.compile("(?:FROM|from) (\\w+)"); //TODO it sucks
        Matcher m = p.matcher(sql);
        if (m.find())
            return StringUtils.capitalize(m.group(1).toLowerCase());
        return "default";
    }

    public void addInjection(Class<?> c, Type type, BiConsumer consumer){
        criteriaMap.put(c.getSimpleName()+type.getVal(), consumer);
    }

    public void addInjection(Class<?> c, Type type, Function<String, String> function){
        sqlMap.put(c.getSimpleName()+type.getVal(), function);
    }

    public void clearInjections(){
        criteriaMap.clear();
        sqlMap.clear();
        postConstructor();
    }
}
