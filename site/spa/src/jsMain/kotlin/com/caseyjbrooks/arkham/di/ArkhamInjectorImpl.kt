package com.caseyjbrooks.arkham.di

import com.caseyjbrooks.arkham.config.ArkhamConfig
import kotlinx.coroutines.CoroutineScope

class ArkhamInjectorImpl(
    applicationCoroutineScope: CoroutineScope,
    config: ArkhamConfig,
    singletonScope: SingletonScope = SingletonScopeImpl(applicationCoroutineScope, config),
    repositoryScope: RepositoryScope = RepositoryScopeImpl(singletonScope),
    viewModelScope: ViewModelScope = ViewModelScopeImpl(repositoryScope),
) : ArkhamInjector,
    SingletonScope by singletonScope,
    RepositoryScope by repositoryScope,
    ViewModelScope by viewModelScope

