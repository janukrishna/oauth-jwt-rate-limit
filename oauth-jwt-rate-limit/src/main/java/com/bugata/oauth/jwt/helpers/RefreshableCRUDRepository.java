package com.bugata.oauth.jwt.helpers;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
/**
 * @author Bugata
 * Created By Krishna on 01-07-2024
 * @project oauth-jwt
 */
@NoRepositoryBean
public interface RefreshableCRUDRepository<T, ID> extends CrudRepository<T, ID> {

    void refresh(T t);

    void refresh(Collection<T> s); 

    void flush();
}
