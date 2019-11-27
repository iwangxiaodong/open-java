package com.openle.our.core.specification;

public abstract class AbstractSpecification<T> implements Specification<T> {

    @Override
    public abstract boolean isSatisfiedBy(T t);

    @Override
    public Specification<T> and(final Specification<T> specification) {
        return new AndSpecification<>(this, specification);
    }

    @Override
    public Specification<T> or(final Specification<T> specification) {
        return new OrSpecification<>(this, specification);
    }

    @Override
    public Specification<T> not(final Specification<T> specification) {
        return new NotSpecification<>(specification);
    }

    /*
    
    可选 - 懒加载避免全数据遍历
    
    //  未采用CQRS的资源库可通过规约列出聚合
    public abstract Optional<List<T>> listBySpecification(Specification specification);

     */
    private Object lazyExpression;

    protected void setLazyExpression(Object lazyExpression) {
        this.lazyExpression = lazyExpression;
    }

    public Object lazyExpression() {
        return this.lazyExpression;
    }
}
