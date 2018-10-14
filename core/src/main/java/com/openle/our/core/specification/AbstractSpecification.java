package com.openle.our.core.specification;

@SuppressWarnings("unchecked")
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
}
