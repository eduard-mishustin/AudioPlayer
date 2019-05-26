package magym.feature.genretablist

import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val genreTabListModule = module {
	viewModel { GenreTabViewModel(get()) }
}