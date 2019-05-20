package magym.feature.service

import magym.feature.service.provide.AudioServiceProvider
import magym.feature.service.provide.AudioServiceProxy
import magym.feature.service.provide.AudioServiceRepository
import org.koin.dsl.module.module

val serviceModule = module {
	single { AudioServiceProxy() }
	single { AudioServiceProvider(get()) } bind AudioServiceRepository::class
}