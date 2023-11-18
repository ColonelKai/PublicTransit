package org.colonelkai.publictransit.utils.lamda;

import org.core.exceptions.NotEnoughArguments;

public interface ThrowableBiFunction<A, B, C, T extends Throwable> {

    C apply(A a, B b) throws NotEnoughArguments;

}
