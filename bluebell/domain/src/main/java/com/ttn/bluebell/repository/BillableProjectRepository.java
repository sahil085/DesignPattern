package com.ttn.bluebell.repository;

import com.ttn.bluebell.domain.project.EBillableProject;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by ttnd on 20/9/16.
 */
@NoRepositoryBean
public interface BillableProjectRepository<E extends EBillableProject> extends BaseProjectRepository<E> {
}
