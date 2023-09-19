package com.alesj.qcl.app;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * @author Ales Justin
 */
@Entity
public class MyEntity {
    @Id
    int id;

    String name;
}
