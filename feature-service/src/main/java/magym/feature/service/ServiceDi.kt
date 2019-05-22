package magym.feature.service

import org.koin.dsl.module.module

val serviceModule = module {
	single { AudioPlayerState() }
}