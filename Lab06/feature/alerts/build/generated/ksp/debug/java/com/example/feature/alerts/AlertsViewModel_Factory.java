package com.example.feature.alerts;

import com.example.domain.usecase.FilterAlertsBySeverityUseCase;
import com.example.domain.usecase.ResolveAlertUseCase;
import com.example.domain.usecase.SyncAlertsUseCase;
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
public final class AlertsViewModel_Factory implements Factory<AlertsViewModel> {
  private final Provider<SyncAlertsUseCase> syncAlertsUseCaseProvider;

  private final Provider<FilterAlertsBySeverityUseCase> filterAlertsBySeverityUseCaseProvider;

  private final Provider<ResolveAlertUseCase> resolveAlertUseCaseProvider;

  private AlertsViewModel_Factory(Provider<SyncAlertsUseCase> syncAlertsUseCaseProvider,
      Provider<FilterAlertsBySeverityUseCase> filterAlertsBySeverityUseCaseProvider,
      Provider<ResolveAlertUseCase> resolveAlertUseCaseProvider) {
    this.syncAlertsUseCaseProvider = syncAlertsUseCaseProvider;
    this.filterAlertsBySeverityUseCaseProvider = filterAlertsBySeverityUseCaseProvider;
    this.resolveAlertUseCaseProvider = resolveAlertUseCaseProvider;
  }

  @Override
  public AlertsViewModel get() {
    return newInstance(syncAlertsUseCaseProvider.get(), filterAlertsBySeverityUseCaseProvider.get(), resolveAlertUseCaseProvider.get());
  }

  public static AlertsViewModel_Factory create(
      Provider<SyncAlertsUseCase> syncAlertsUseCaseProvider,
      Provider<FilterAlertsBySeverityUseCase> filterAlertsBySeverityUseCaseProvider,
      Provider<ResolveAlertUseCase> resolveAlertUseCaseProvider) {
    return new AlertsViewModel_Factory(syncAlertsUseCaseProvider, filterAlertsBySeverityUseCaseProvider, resolveAlertUseCaseProvider);
  }

  public static AlertsViewModel newInstance(SyncAlertsUseCase syncAlertsUseCase,
      FilterAlertsBySeverityUseCase filterAlertsBySeverityUseCase,
      ResolveAlertUseCase resolveAlertUseCase) {
    return new AlertsViewModel(syncAlertsUseCase, filterAlertsBySeverityUseCase, resolveAlertUseCase);
  }
}
