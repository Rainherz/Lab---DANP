package com.example.feature.alerts;

import com.example.domain.usecase.AddUserTechnologyUseCase;
import com.example.domain.usecase.CheckTechnologyCvesUseCase;
import com.example.domain.usecase.DeleteUserTechnologyUseCase;
import com.example.domain.usecase.GetAlertsUseCase;
import com.example.domain.usecase.GetUserTechnologiesUseCase;
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
public final class TechnologiesViewModel_Factory implements Factory<TechnologiesViewModel> {
  private final Provider<GetUserTechnologiesUseCase> getUserTechnologiesUseCaseProvider;

  private final Provider<GetAlertsUseCase> getAlertsUseCaseProvider;

  private final Provider<AddUserTechnologyUseCase> addUserTechnologyUseCaseProvider;

  private final Provider<DeleteUserTechnologyUseCase> deleteUserTechnologyUseCaseProvider;

  private final Provider<CheckTechnologyCvesUseCase> checkTechnologyCvesUseCaseProvider;

  private TechnologiesViewModel_Factory(
      Provider<GetUserTechnologiesUseCase> getUserTechnologiesUseCaseProvider,
      Provider<GetAlertsUseCase> getAlertsUseCaseProvider,
      Provider<AddUserTechnologyUseCase> addUserTechnologyUseCaseProvider,
      Provider<DeleteUserTechnologyUseCase> deleteUserTechnologyUseCaseProvider,
      Provider<CheckTechnologyCvesUseCase> checkTechnologyCvesUseCaseProvider) {
    this.getUserTechnologiesUseCaseProvider = getUserTechnologiesUseCaseProvider;
    this.getAlertsUseCaseProvider = getAlertsUseCaseProvider;
    this.addUserTechnologyUseCaseProvider = addUserTechnologyUseCaseProvider;
    this.deleteUserTechnologyUseCaseProvider = deleteUserTechnologyUseCaseProvider;
    this.checkTechnologyCvesUseCaseProvider = checkTechnologyCvesUseCaseProvider;
  }

  @Override
  public TechnologiesViewModel get() {
    return newInstance(getUserTechnologiesUseCaseProvider.get(), getAlertsUseCaseProvider.get(), addUserTechnologyUseCaseProvider.get(), deleteUserTechnologyUseCaseProvider.get(), checkTechnologyCvesUseCaseProvider.get());
  }

  public static TechnologiesViewModel_Factory create(
      Provider<GetUserTechnologiesUseCase> getUserTechnologiesUseCaseProvider,
      Provider<GetAlertsUseCase> getAlertsUseCaseProvider,
      Provider<AddUserTechnologyUseCase> addUserTechnologyUseCaseProvider,
      Provider<DeleteUserTechnologyUseCase> deleteUserTechnologyUseCaseProvider,
      Provider<CheckTechnologyCvesUseCase> checkTechnologyCvesUseCaseProvider) {
    return new TechnologiesViewModel_Factory(getUserTechnologiesUseCaseProvider, getAlertsUseCaseProvider, addUserTechnologyUseCaseProvider, deleteUserTechnologyUseCaseProvider, checkTechnologyCvesUseCaseProvider);
  }

  public static TechnologiesViewModel newInstance(
      GetUserTechnologiesUseCase getUserTechnologiesUseCase, GetAlertsUseCase getAlertsUseCase,
      AddUserTechnologyUseCase addUserTechnologyUseCase,
      DeleteUserTechnologyUseCase deleteUserTechnologyUseCase,
      CheckTechnologyCvesUseCase checkTechnologyCvesUseCase) {
    return new TechnologiesViewModel(getUserTechnologiesUseCase, getAlertsUseCase, addUserTechnologyUseCase, deleteUserTechnologyUseCase, checkTechnologyCvesUseCase);
  }
}
