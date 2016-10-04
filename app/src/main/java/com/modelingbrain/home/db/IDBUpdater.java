package com.modelingbrain.home.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.modelingbrain.home.model.Model;

import java.util.List;

interface IDBUpdater {
    List<Model> update(Context context, SQLiteDatabase db);
}
