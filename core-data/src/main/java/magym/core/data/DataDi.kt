package magym.core.data

import magym.core.data.data.base.AudioPlayerDatabase
import magym.core.data.data.parser.AudioParser
import magym.core.data.data.parser.AudioParserApi
import magym.core.data.domain.DataProvider
import magym.core.data.domain.boundary.AudioBoundaryCallback
import magym.core.data.domain.repository.AudioRepository
import magym.core.data.domain.repository.GenreRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val dataModule = module {
	single { AudioPlayerDatabase.getInstance(androidContext()) }
	single { get<AudioPlayerDatabase>().getAudioDao() }
	single { get<AudioPlayerDatabase>().getGenreDao() }
	
	single<AudioParserApi> { AudioParser() }
	
	single { DataProvider(get(), get(), get()) }
	single<GenreRepository> { get<DataProvider>() }
	single<AudioRepository> { get<DataProvider>() }
}