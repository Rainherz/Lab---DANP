package com.example.core.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.example.core.database.dao.UserTechnologyDao;
import com.example.core.database.dao.UserTechnologyDao_Impl;
import com.example.core.database.dao.VulnerabilityDao;
import com.example.core.database.dao.VulnerabilityDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SecAlertsDatabase_Impl extends SecAlertsDatabase {
  private volatile VulnerabilityDao _vulnerabilityDao;

  private volatile UserTechnologyDao _userTechnologyDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `vulnerability_alerts` (`id` TEXT NOT NULL, `cve` TEXT NOT NULL, `severity` TEXT NOT NULL, `description` TEXT NOT NULL, `date` TEXT NOT NULL, `affectedTechnology` TEXT NOT NULL, `userTechnologyId` INTEGER, `referenceUrl` TEXT, `isResolved` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `user_technologies` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `version` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'c45f951c59904dbc18863f9c2329904c')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `vulnerability_alerts`");
        db.execSQL("DROP TABLE IF EXISTS `user_technologies`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsVulnerabilityAlerts = new HashMap<String, TableInfo.Column>(9);
        _columnsVulnerabilityAlerts.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVulnerabilityAlerts.put("cve", new TableInfo.Column("cve", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVulnerabilityAlerts.put("severity", new TableInfo.Column("severity", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVulnerabilityAlerts.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVulnerabilityAlerts.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVulnerabilityAlerts.put("affectedTechnology", new TableInfo.Column("affectedTechnology", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVulnerabilityAlerts.put("userTechnologyId", new TableInfo.Column("userTechnologyId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVulnerabilityAlerts.put("referenceUrl", new TableInfo.Column("referenceUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVulnerabilityAlerts.put("isResolved", new TableInfo.Column("isResolved", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVulnerabilityAlerts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesVulnerabilityAlerts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoVulnerabilityAlerts = new TableInfo("vulnerability_alerts", _columnsVulnerabilityAlerts, _foreignKeysVulnerabilityAlerts, _indicesVulnerabilityAlerts);
        final TableInfo _existingVulnerabilityAlerts = TableInfo.read(db, "vulnerability_alerts");
        if (!_infoVulnerabilityAlerts.equals(_existingVulnerabilityAlerts)) {
          return new RoomOpenHelper.ValidationResult(false, "vulnerability_alerts(com.example.core.database.entity.VulnerabilityEntity).\n"
                  + " Expected:\n" + _infoVulnerabilityAlerts + "\n"
                  + " Found:\n" + _existingVulnerabilityAlerts);
        }
        final HashMap<String, TableInfo.Column> _columnsUserTechnologies = new HashMap<String, TableInfo.Column>(3);
        _columnsUserTechnologies.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTechnologies.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTechnologies.put("version", new TableInfo.Column("version", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserTechnologies = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserTechnologies = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserTechnologies = new TableInfo("user_technologies", _columnsUserTechnologies, _foreignKeysUserTechnologies, _indicesUserTechnologies);
        final TableInfo _existingUserTechnologies = TableInfo.read(db, "user_technologies");
        if (!_infoUserTechnologies.equals(_existingUserTechnologies)) {
          return new RoomOpenHelper.ValidationResult(false, "user_technologies(com.example.core.database.entity.UserTechnologyEntity).\n"
                  + " Expected:\n" + _infoUserTechnologies + "\n"
                  + " Found:\n" + _existingUserTechnologies);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "c45f951c59904dbc18863f9c2329904c", "b493c03e1171f4d1029651f6570f6122");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "vulnerability_alerts","user_technologies");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `vulnerability_alerts`");
      _db.execSQL("DELETE FROM `user_technologies`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(VulnerabilityDao.class, VulnerabilityDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserTechnologyDao.class, UserTechnologyDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public VulnerabilityDao vulnerabilityDao() {
    if (_vulnerabilityDao != null) {
      return _vulnerabilityDao;
    } else {
      synchronized(this) {
        if(_vulnerabilityDao == null) {
          _vulnerabilityDao = new VulnerabilityDao_Impl(this);
        }
        return _vulnerabilityDao;
      }
    }
  }

  @Override
  public UserTechnologyDao userTechnologyDao() {
    if (_userTechnologyDao != null) {
      return _userTechnologyDao;
    } else {
      synchronized(this) {
        if(_userTechnologyDao == null) {
          _userTechnologyDao = new UserTechnologyDao_Impl(this);
        }
        return _userTechnologyDao;
      }
    }
  }
}
