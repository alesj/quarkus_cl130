package com.alesj.qcl.engine.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 */
@Entity
@Table(name = TableModCount.TABLE_NAME)
public class TableModCount {
    // underscore prefix makes Hibernate order DDL in front of other entities which is important
    public static final String TABLE_NAME = "_table_mod_count";
    public static final String TABLE_NAME_COLUMN = "table_name";
    public static final String MOD_COUNT_COLUMN = "mod_count";

    @Id
    @Column(name = TABLE_NAME_COLUMN)
    String tableName;

    @Column(name = MOD_COUNT_COLUMN)
    long modCount;

    public String getTableName() {
        return tableName;
    }

    public long getModCount() {
        return modCount;
    }
}
