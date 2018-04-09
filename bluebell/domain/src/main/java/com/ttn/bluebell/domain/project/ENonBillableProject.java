package com.ttn.bluebell.domain.project;

import javax.persistence.*;

/**
 * Created by ashutoshmeher on 16/9/16.
 */
@Entity
@DiscriminatorValue("NON_BILLABLE")
public class ENonBillableProject extends EProject{
}
