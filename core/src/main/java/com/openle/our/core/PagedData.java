package com.openle.our.core;

import java.util.List;

/**
 *
 * @author xiaodong
 * @param <TEntity> 实体
 */
public class PagedData<TEntity> {

    private long total; // 总条数
    private int lastPageNumber;   // 最后页码
    private List<TEntity> entities;

    public int getLastPageNumber() {
        return lastPageNumber;
    }

    public void setLastPageNumber(int lastPageNumber) {
        this.lastPageNumber = lastPageNumber;
    }

    public List<TEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<TEntity> entities) {
        this.entities = entities;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

}
