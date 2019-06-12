package magym.feature.audiosearch

import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val audioSearchModule = module {
	viewModel { AudioSearchViewModel(get(), get()) }
}