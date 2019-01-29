package com.honestwalker.androidutils.IO;

import java.lang.reflect.Field;

/**
 * Created by lanzhe on 17-1-16.
 */
public interface ObjectReflectCoptListener {

    void onCopyField(Object src, Object desc , Field srcField, Field descField, String fieldName, Object value);

}
