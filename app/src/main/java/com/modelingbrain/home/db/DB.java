package com.modelingbrain.home.db;

import java.util.ArrayList;
import java.util.List;

public class DB {

    public static final String MODEL_TABLE = "MODEL_TABLE";

    public static final List<String> listDb = new ArrayList<>();
    {
        listDb.add(MODEL_TABLE);
        listDb.add("locale");
    }
}
