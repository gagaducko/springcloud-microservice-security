package auth.gagaduck.userauthservice.userInfo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, CrudRepository<T, ID>, JpaSpecificationExecutor<T> {
    default <S extends T> S saveIfNotNull(S entity) {
        return entity != null ? save(entity) : null;
    }
}
