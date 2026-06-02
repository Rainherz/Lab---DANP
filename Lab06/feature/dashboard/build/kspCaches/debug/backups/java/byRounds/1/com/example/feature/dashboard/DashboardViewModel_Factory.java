package com.example.feature.dashboard;

import com.example.domain.usecase.VulnerabilityStatsUseCase;
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<VulnerabilityStatsUseCase> vulnerabilityStatsUseCaseProvider;

  private DashboardViewModel_Factory(
      Provider<VulnerabilityStatsUseCase> vulnerabilityStatsUseCaseProvider) {
    this.vulnerabilityStatsUseCaseProvider = vulnerabilityStatsUseCaseProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(vulnerabilityStatsUseCaseProvider.get());
  }

  public static DashboardViewModel_Factory create(
      Provider<VulnerabilityStatsUseCase> vulnerabilityStatsUseCaseProvider) {
    return new DashboardViewModel_Factory(vulnerabilityStatsUseCaseProvider);
  }

  public static DashboardViewModel newInstance(
      VulnerabilityStatsUseCase vulnerabilityStatsUseCase) {
    return new DashboardViewModel(vulnerabilityStatsUseCase);
  }
}
