package com.openle.our.core.specification;

public class NotSpecification<T> extends AbstractSpecification<T> {

    private final Specification<T> spec1;

    public NotSpecification(final Specification<T> spec1) {
        this.spec1 = spec1;
    }

    @Override
    public boolean isSatisfiedBy(final T t) {
        return !spec1.isSatisfiedBy(t);
    }

}
