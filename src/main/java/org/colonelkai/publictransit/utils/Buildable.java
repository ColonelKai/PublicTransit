package org.colonelkai.publictransit.utils;

public interface Buildable<Build extends Builder<Build, Self>, Self extends Buildable<Build, Self>> {

    Build toBuilder();
}
