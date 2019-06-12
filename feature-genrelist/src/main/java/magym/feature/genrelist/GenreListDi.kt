package magym.feature.genrelist

import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val genreListModule = module {
	viewModel { GenreListViewModel(get()) }
}