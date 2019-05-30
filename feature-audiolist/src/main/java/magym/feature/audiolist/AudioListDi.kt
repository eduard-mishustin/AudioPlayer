package magym.feature.audiolist

import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val audioListModule = module {
	single { SearchViewProvider() }
	viewModel { (isSearchMode: Boolean) -> AudioListViewModel(get(), get(), isSearchMode) }
}