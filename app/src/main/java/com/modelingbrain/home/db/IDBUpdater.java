package com.modelingbrain.home.db;

import android.database.sqlite.SQLiteDatabase;

import com.modelingbrain.home.model.Model;

import java.util.List;

public interface IDBUpdater {
    List<Model> update(SQLiteDatabase db);
}
