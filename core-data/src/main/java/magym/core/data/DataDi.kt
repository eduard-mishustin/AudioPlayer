package magym.core.data

import magym.core.data.data.parser.AudioApi
import magym.core.data.data.parser.AudioParser
import magym.core.data.domain.AudioProvider
import magym.core.data.domain.AudioRepository
import magym.core.data.domain.GenreRepository
import org.koin.dsl.module.module

val dataModule = module {
	single { AudioParser() } bind AudioApi::class
	single<GenreRepository> { AudioProvider(get()) }
	single<AudioRepository> { AudioProvider(get()) }
}