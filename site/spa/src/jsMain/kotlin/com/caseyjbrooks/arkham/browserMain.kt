package com.caseyjbrooks.arkham

import androidx.compose.runtime.DisposableEffect
import com.caseyjbrooks.arkham.config.ArkhamConfigImpl
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.di.ArkhamInjectorImpl
import com.caseyjbrooks.arkham.ui.MainApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.jetbrains.compose.web.renderComposableInBody

fun browserMain(isPwa: Boolean) {
    println("browser main")

    try {
        val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        val injector: ArkhamInjector = ArkhamInjectorImpl(
            applicationScope,
            ArkhamConfigImpl(
                isPwa = isPwa,
            )
        )

        renderComposableInBody {
            DisposableEffect(Unit) {
                onDispose { applicationScope.cancel() }
            }

            MainApplication(injector)
        }
    } catch (e: Throwable) {
        println(e.message)
        console.error(e)
//        e.printStackTrace()
    }
}
