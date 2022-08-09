package com.caseyjbrooks.arkham.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.utils.theme.bulma.Banner
import com.caseyjbrooks.arkham.utils.theme.bulma.Breadcrumbs
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSection
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSize
import com.caseyjbrooks.arkham.utils.theme.bulma.Card
import com.caseyjbrooks.arkham.utils.theme.bulma.Column
import com.caseyjbrooks.arkham.utils.theme.bulma.Hero
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.bulma.Row
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

object HomeUi {
    @Composable
    fun Content(injector: ArkhamInjector) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector) { injector.homeViewModel(coroutineScope) }
        val vmState by vm.observeStates().collectAsState()
        Content(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Content(state: HomeContract.State, postInput: (HomeContract.Inputs) -> Unit) {
        MainLayout(state.layout) {
            Hero(
                title = { Text("Arkham Explorer") },
                subtitle = { Text("A comprehensive archive for resources, assets, and structured data for Arkham Horror: The Card Game") },
                size = BulmaSize.Medium,
            )
            Banner {
                Span({ classes("tag", "is-primary") }) { Text("New") }
                Text("Fantasy Flight Games has announced a new expansion, The Scarlet Keys!")
            }
            BulmaSection {
                Breadcrumbs(
                    NavigationRoute("Home", null, ArkhamApp.Home),
                )
            }
            BulmaSection {
                Row("features") {
                    Column {
                        Card(
                            imageUrl = "https://cf.geekdo-images.com/B5F5ulz0UivNgrI9Ky0euA__imagepage/img/tgpLRvv6AIsClnegErNpAoieeMo=/fit-in/900x600/filters:no_upscale():strip_icc()/pic3122349.jpg",
                            title = "Core Set",
                            description = "The boundaries between worlds have drawn perilously thin. Dark forces work in the shadows and call upon unspeakable horrors, strange happenings are discovered all throughout the city of Arkham, Massachusetts, and behind it all an Ancient One manipulates everything from beyond the veil. It is time to revisit that which started it all…"
                        )
                    }
                    Column {
                        Card(
                            imageUrl = "https://cf.geekdo-images.com/K-Gm9TAbPmHhA0E7wWvZ8g__imagepage/img/sZnnuH4fsKna5hgrf_SckvBXzec=/fit-in/900x600/filters:no_upscale():strip_icc()/pic3610420.jpg",
                            title = "The Dunwich Horror",
                            description = "The boundaries between worlds have drawn perilously thin. Dark forces work in the shadows and call upon unspeakable horrors, strange happenings are discovered all throughout the city of Arkham, Massachusetts, and behind it all an Ancient One manipulates everything from beyond the veil. It is time to revisit that which started it all…"
                        )
                    }
                    Column {
                        Card(
                            imageUrl = "https://cf.geekdo-images.com/6rxB9hdtoi0IVLdCXI1IRA__imagepage/img/ok3FIIGnxgWPEbLbBlIC81B4Wn0=/fit-in/900x600/filters:no_upscale():strip_icc()/pic3564115.jpg",
                            title = "The Path to Carcosa",
                            description = "The boundaries between worlds have drawn perilously thin. Dark forces work in the shadows and call upon unspeakable horrors, strange happenings are discovered all throughout the city of Arkham, Massachusetts, and behind it all an Ancient One manipulates everything from beyond the veil. It is time to revisit that which started it all…"
                        )
                    }
                }
                Row("features") {
                    Column {
                        Card(
                            imageUrl = "https://cf.geekdo-images.com/wvpEUh94mIusI1ymG_6NVQ__imagepage/img/ADN3DQvm3oGEdn7JaERCauxFa0k=/fit-in/900x600/filters:no_upscale():strip_icc()/pic4025299.jpg",
                            title = "The Forgotten Age",
                            description = "The boundaries between worlds have drawn perilously thin. Dark forces work in the shadows and call upon unspeakable horrors, strange happenings are discovered all throughout the city of Arkham, Massachusetts, and behind it all an Ancient One manipulates everything from beyond the veil. It is time to revisit that which started it all…"
                        )
                    }
                    Column {
                        Card(
                            imageUrl = "https://cf.geekdo-images.com/a-yinPN6joGCv4mJ9Wg05w__imagepage/img/zZxPC36PgJhRiZpJ7ZFvpcb8_y0=/fit-in/900x600/filters:no_upscale():strip_icc()/pic4409506.jpg",
                            title = "The Circle Undone",
                            description = "The boundaries between worlds have drawn perilously thin. Dark forces work in the shadows and call upon unspeakable horrors, strange happenings are discovered all throughout the city of Arkham, Massachusetts, and behind it all an Ancient One manipulates everything from beyond the veil. It is time to revisit that which started it all…"
                        )
                    }
                    Column {
                        Card(
                            imageUrl = "https://cf.geekdo-images.com/noZq6s4KuN8JkiykQI4ToA__imagepage/img/rg_qiJWkcbZXqg6upge9KTQY9xI=/fit-in/900x600/filters:no_upscale():strip_icc()/pic4819000.jpg",
                            title = "The Dream Eaters",
                            description = "The boundaries between worlds have drawn perilously thin. Dark forces work in the shadows and call upon unspeakable horrors, strange happenings are discovered all throughout the city of Arkham, Massachusetts, and behind it all an Ancient One manipulates everything from beyond the veil. It is time to revisit that which started it all…"
                        )
                    }
                }
                Row("features") {
                    Column {
                        Card(
                            imageUrl = "https://cf.geekdo-images.com/Y7engtH3jULhy3X5-R8YEQ__imagepage/img/Z524fsb02xB4KRFa8QQCk5BMjMo=/fit-in/900x600/filters:no_upscale():strip_icc()/pic5384304.png",
                            title = "The Innsmouth Conspiracy",
                            description = "The boundaries between worlds have drawn perilously thin. Dark forces work in the shadows and call upon unspeakable horrors, strange happenings are discovered all throughout the city of Arkham, Massachusetts, and behind it all an Ancient One manipulates everything from beyond the veil. It is time to revisit that which started it all…"
                        )
                    }
                    Column {
                        Card(
                            imageUrl = "https://cf.geekdo-images.com/zCBeaFMFckzNCwJihcOXLQ__imagepage/img/CquFtTjbCUqBXbJfosqMmF_g6YM=/fit-in/900x600/filters:no_upscale():strip_icc()/pic7003509.png",
                            title = "Edge of the Earth",
                            description = "The boundaries between worlds have drawn perilously thin. Dark forces work in the shadows and call upon unspeakable horrors, strange happenings are discovered all throughout the city of Arkham, Massachusetts, and behind it all an Ancient One manipulates everything from beyond the veil. It is time to revisit that which started it all…"
                        )
                    }
                    Column {
                        Card(
                            imageUrl = "https://cf.geekdo-images.com/cf9j3UR9GrKL_LK6WExYQA__imagepage/img/xXqnYCWjRcNfBqGR-T-KOHClFtc=/fit-in/900x600/filters:no_upscale():strip_icc()/pic6944629.jpg",
                            title = "The Scarlet Keys",
                            description = "The boundaries between worlds have drawn perilously thin. Dark forces work in the shadows and call upon unspeakable horrors, strange happenings are discovered all throughout the city of Arkham, Massachusetts, and behind it all an Ancient One manipulates everything from beyond the veil. It is time to revisit that which started it all…"
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun NotFound(path: String) {
        Text("${path} not found")
    }

    @Composable
    fun UnknownError() {
        Text("Unknown error")
    }
}
