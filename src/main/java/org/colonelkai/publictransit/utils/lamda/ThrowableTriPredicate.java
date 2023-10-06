package org.colonelkai.publictransit.utils.lamda;

public interface ThrowableTriPredicate<A, E, I, T extends Throwable> {

    boolean accept(A a, E e, I i) throws T;
}
