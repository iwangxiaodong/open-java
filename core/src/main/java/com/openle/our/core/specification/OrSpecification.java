package com.openle.our.core.specification;

public class OrSpecification<T> extends AbstractSpecification<T> {

    private final Specification<T> spec1;
    private final Specification<T> spec2;

    public OrSpecification(final Specification<T> spec1, final Specification<T> spec2) {
        this.spec1 = spec1;
        this.spec2 = spec2;
    }

    @Override
    public boolean isSatisfiedBy(final T t) {
        return spec1.isSatisfiedBy(t) || spec2.isSatisfiedBy(t);
    }
}
