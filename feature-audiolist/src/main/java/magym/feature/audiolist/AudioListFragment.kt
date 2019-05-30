package magym.feature.audiolist

import android.os.Bundle
import android.view.View
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_audios.*
import magym.core.common.extention.argumentInt
import magym.core.common.extention.init
import magym.core.common.mvi.MviFragment
import magym.core.data.data.entity.Audio
import magym.feature.audiolist.mvi.AudioListIntent
import magym.feature.audiolist.mvi.AudioListSubscription
import magym.feature.audiolist.mvi.AudioListViewState
import magym.feature.audiolist.recycler.AudioAdapter
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf
import java.util.concurrent.TimeUnit

class AudioListFragment : MviFragment<AudioListIntent, AudioListViewState, AudioListSubscription>() {
	
	override val layoutId = R.layout.fragment_audios
	
	private val genreId by argumentInt(KEY_GENRE_ID)
	
	private val adapter: AudioAdapter = AudioAdapter(::onItemClick)
	
	private val searchViewProvider: SearchViewProvider = get()
	
	
	override fun provideViewModel(): AudioListViewModel = get { parametersOf(genreId == 0) }
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		checkArguments(KEY_GENRE_ID)
		
		postIntent(AudioListIntent.LoadData(genreId))
		recycler_view.init(adapter)
		
		disposable += searchViewProvider.textChanges
			.debounce(500, TimeUnit.MILLISECONDS)
			.subscribe { postIntent(AudioListIntent.LoadData(genreId, it.toString())) }
	}
	
	override fun render(state: AudioListViewState) {
		if (activityProvider.isLoading != state.isLoading) activityProvider.isLoading = state.isLoading
		adapter.submitList(state.audios)
	}
	
	override fun onSubscriptionReceived(subscription: AudioListSubscription) {
		when (subscription) {
			is AudioListSubscription.RemoteRequestError -> {
				layout.showErrorSnackBarWithAction { postIntent(AudioListIntent.LoadData(genreId)) }
			}
		}
	}
	
	
	private fun onItemClick(audio: Audio) {
		postIntent(AudioListIntent.PlayAudio(audio))
	}
	
	companion object {
		
		private const val KEY_GENRE_ID = "genreId"
		
		fun newInstance(genreId: Int) = AudioListFragment().withArguments {
			putInt(KEY_GENRE_ID, genreId)
		}
		
	}
	
}