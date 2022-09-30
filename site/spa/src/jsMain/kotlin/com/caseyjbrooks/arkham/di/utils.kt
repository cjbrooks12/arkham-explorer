package com.caseyjbrooks.arkham.di

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BootstrapInterceptor
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.debugger.BallastDebuggerInterceptor
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.withViewModel

fun <Inputs : Any, Events : Any, State : Any> SingletonScope.defaultConfigBuilder(
    initialState: State,
    inputHandler: InputHandler<Inputs, Events, State>,
    useDebugger: Boolean = false,
    useLogger: Boolean = false,
    name: String? = null,
    additionalConfig: (BallastViewModelConfiguration.Builder.() -> Unit)? = null,
    getInitialInput: (suspend () -> Inputs)? = null,
): BallastViewModelConfiguration<Inputs, Events, State> {
    return BallastViewModelConfiguration.Builder()
        .withViewModel(
            initialState = initialState,
            inputHandler = inputHandler,
            name = name,
        )
        .apply {
            logger = { ballastLogger }
            if (config.debug && useLogger) {
                this += LoggingInterceptor()
            }

            if (config.debug && useDebugger) {
                this += BallastDebuggerInterceptor(debuggerConnection)
            }

            if (getInitialInput != null) {
                this += BootstrapInterceptor(getInitialInput)
            }

            if (additionalConfig != null) {
                additionalConfig()
            }
        }
        .build()
}
