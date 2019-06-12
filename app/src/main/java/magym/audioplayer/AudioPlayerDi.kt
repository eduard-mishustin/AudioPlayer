package magym.audioplayer

import magym.audioplayer.navigation.Navigator
import magym.core.common.AudioPlayerNavigation
import magym.core.data.dataModule
import magym.feature.audiolist.audioListModule
import magym.feature.audiosearch.audioSearchModule
import magym.feature.genrelist.genreListModule
import magym.feature.genretablist.genreTabListModule
import magym.feature.service.serviceModule
import org.koin.dsl.module.module

val appModule = module {
	single { Navigator() } bind AudioPlayerNavigation::class
}

val koinModules = listOf(
	appModule,
	dataModule,
	serviceModule,
	genreListModule,
	genreTabListModule,
	audioListModule,
	audioSearchModule
)