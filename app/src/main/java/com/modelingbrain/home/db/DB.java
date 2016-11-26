package com.modelingbrain.home.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * License: LGPL ver.3
 *
 * @author Izyumov Konstantin
 */

class DB {
    static final List<String> listDb = new ArrayList<>(Arrays.asList
            (
                    DBHelperModel.DATABASE_MODEL,
                    "locale",
                    "android_metadata",
                    "sqlite_sequence",
                    "TABLE"+"OF"+"TEST"));
}
