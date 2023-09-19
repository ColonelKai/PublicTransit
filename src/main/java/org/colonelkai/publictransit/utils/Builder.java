package org.colonelkai.publictransit.utils;

public interface Builder<Self extends Builder<Self, Built>, Built extends Buildable<Self, Built>> {

    Built build();

    Self from(Self self);

    default Self from(Built built) {
        return from(built.toBuilder());
    }


}
