package magym.feature.audiolist

import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val audioListModule = module {
	viewModel { AudioListViewModel(get(), get()) }
}