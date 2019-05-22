package magym.feature.genretablist

import org.koin.dsl.module.module

val genreTabListModule = module {
	single { GenreTabViewModel(get()) }
}