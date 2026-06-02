package com.example.data.repository;

import com.example.core.database.dao.UserTechnologyDao;
import com.example.core.database.dao.VulnerabilityDao;
import com.example.core.network.api.CveApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class AlertRepositoryImpl_Factory implements Factory<AlertRepositoryImpl> {
  private final Provider<CveApiService> cveApiServiceProvider;

  private final Provider<VulnerabilityDao> vulnerabilityDaoProvider;

  private final Provider<UserTechnologyDao> userTechnologyDaoProvider;

  private AlertRepositoryImpl_Factory(Provider<CveApiService> cveApiServiceProvider,
      Provider<VulnerabilityDao> vulnerabilityDaoProvider,
      Provider<UserTechnologyDao> userTechnologyDaoProvider) {
    this.cveApiServiceProvider = cveApiServiceProvider;
    this.vulnerabilityDaoProvider = vulnerabilityDaoProvider;
    this.userTechnologyDaoProvider = userTechnologyDaoProvider;
  }

  @Override
  public AlertRepositoryImpl get() {
    return newInstance(cveApiServiceProvider.get(), vulnerabilityDaoProvider.get(), userTechnologyDaoProvider.get());
  }

  public static AlertRepositoryImpl_Factory create(Provider<CveApiService> cveApiServiceProvider,
      Provider<VulnerabilityDao> vulnerabilityDaoProvider,
      Provider<UserTechnologyDao> userTechnologyDaoProvider) {
    return new AlertRepositoryImpl_Factory(cveApiServiceProvider, vulnerabilityDaoProvider, userTechnologyDaoProvider);
  }

  public static AlertRepositoryImpl newInstance(CveApiService cveApiService,
      VulnerabilityDao vulnerabilityDao, UserTechnologyDao userTechnologyDao) {
    return new AlertRepositoryImpl(cveApiService, vulnerabilityDao, userTechnologyDao);
  }
}
