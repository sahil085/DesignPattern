package com.ttn.bluebell.repository;

import com.ttn.bluebell.domain.project.EProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * Created by praveshsaini on 16/9/16.
 */
@NoRepositoryBean
public interface BaseProjectRepository<E extends EProject> extends JpaRepository<E,Long> {

    E findByProjectIdAndActiveIsTrue(Long id);
    List<E> findAllByActiveIsTrueOrderByProjectNameAsc();



}
