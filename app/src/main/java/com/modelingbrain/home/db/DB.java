package com.modelingbrain.home.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class DB {
    static final List<String> listDb = new ArrayList<>(Arrays.asList
            (
                    DBHelperModel.DATABASE_MODEL,
                    "locale",
                    "android_metadata",
                    "sqlite_sequence",
                    "TABLE"+"OF"+"TEST"));
}
