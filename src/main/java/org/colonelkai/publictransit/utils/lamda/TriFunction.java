package org.colonelkai.publictransit.utils.lamda;

public interface TriFunction<First, Second, Third, Ret> {

    Ret apply(First x, Second y, Third z);

}
