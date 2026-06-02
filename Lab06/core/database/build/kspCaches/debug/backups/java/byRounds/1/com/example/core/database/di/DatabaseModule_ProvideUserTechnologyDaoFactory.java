package com.example.core.database.di;

import com.example.core.database.SecAlertsDatabase;
import com.example.core.database.dao.UserTechnologyDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class DatabaseModule_ProvideUserTechnologyDaoFactory implements Factory<UserTechnologyDao> {
  private final Provider<SecAlertsDatabase> databaseProvider;

  private DatabaseModule_ProvideUserTechnologyDaoFactory(
      Provider<SecAlertsDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public UserTechnologyDao get() {
    return provideUserTechnologyDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideUserTechnologyDaoFactory create(
      Provider<SecAlertsDatabase> databaseProvider) {
    return new DatabaseModule_ProvideUserTechnologyDaoFactory(databaseProvider);
  }

  public static UserTechnologyDao provideUserTechnologyDao(SecAlertsDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideUserTechnologyDao(database));
  }
}
