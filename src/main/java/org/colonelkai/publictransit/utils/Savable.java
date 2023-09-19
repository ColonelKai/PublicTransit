package org.colonelkai.publictransit.utils;

import java.io.File;

public interface Savable {

    File defaultFile();
    void save(File file) throws Exception;

    default void save() throws Exception{
        this.save(this.defaultFile());
    }

}
