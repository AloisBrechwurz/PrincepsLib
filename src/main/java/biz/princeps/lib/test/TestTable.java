package biz.princeps.lib.test;

import biz.princeps.lib.storage.annotation.Column;
import biz.princeps.lib.storage.annotation.Constructor;
import biz.princeps.lib.storage.annotation.Table;
import biz.princeps.lib.storage.annotation.Unique;

/**
 * Created by spatium on 16.07.17.
 */
@Table(name = "test")
public class TestTable {

    @Column(name = "NAME_COLUMN", length = 36)
    private String blub;

    @Unique
    @Column(name = "count")
    private int coundt;

    @Column(name = "longii", length = 30)
    private long counat;

    @Column(name = "floatiii", length = 30)
    private float counst;

    @Column(name = "boolii")
    private boolean count;

    @Constructor
    public TestTable(@Column(name = "NAME_COLUMN") String blub,
                     @Column(name = "count") int coundt,
                     @Column(name = "longii") long counat,
                     @Column(name = "floatiii") float counst,
                     @Column(name = "boolii") boolean count) {
        this.blub = blub;
        this.coundt = coundt;
        this.counat = counat;
        this.counst = counst;
        this.count = count;
    }

    public boolean getCount() {
        return count;
    }
}
