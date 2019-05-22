package magym.feature.genrelist

import org.koin.dsl.module.module

val genreListModule = module {
	single { GenreListViewModel(get()) }
}