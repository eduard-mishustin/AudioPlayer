package magym.feature.audiosearch

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_audiosearch.*
import magym.core.common.extention.findSearchView
import magym.core.common.extention.init
import magym.core.common.mvi.MviFragment
import magym.core.data.data.entity.Audio
import magym.feature.audiosearch.mvi.AudioSearchIntent
import magym.feature.audiosearch.mvi.AudioSearchSubscription
import magym.feature.audiosearch.mvi.AudioSearchViewState
import magym.feature.featureaudioadapter.AudioAdapter
import org.koin.android.ext.android.get
import java.util.concurrent.TimeUnit

class AudioSearchFragment : MviFragment<AudioSearchIntent, AudioSearchViewState, AudioSearchSubscription>() {
	
	override val layoutId = R.layout.fragment_audiosearch
	
	override val menuResource = R.menu.audiosearch_menu
	
	private val adapter: AudioAdapter = AudioAdapter(::onItemClick)
	
	private lateinit var searchView: SearchView
	
	
	override fun provideViewModel(): AudioSearchViewModel = get()
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		toolbar.init()
		
		recycler_view.init(adapter)
	}
	
	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		super.onCreateOptionsMenu(menu, inflater)
		
		searchView = menu.findSearchView(R.id.search)
		searchView.isIconified = false
		
		disposable += searchView.queryTextChanges()
			.debounce(500, TimeUnit.MILLISECONDS)
			.subscribe { postIntent(AudioSearchIntent.LoadData(it.toString())) }
	}
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home -> {
				if (!searchView.isIconified) {
					searchView.isIconified = true
				}
				else return super.onOptionsItemSelected(item)
			}
			
			else -> return super.onOptionsItemSelected(item)
		}
		
		return true
	}
	
	
	override fun render(state: AudioSearchViewState) {
		if (activityProvider.isLoading != state.isLoading) activityProvider.isLoading = state.isLoading
		adapter.submitList(state.audios)
	}
	
	override fun onSubscriptionReceived(subscription: AudioSearchSubscription) {
		when (subscription) {
			is AudioSearchSubscription.RemoteRequestError -> {
				layout.showErrorSnackBarWithAction { postIntent(AudioSearchIntent.LoadData()) } // todo filterQuery
			}
		}
	}
	
	
	private fun onItemClick(audio: Audio) {
		postIntent(AudioSearchIntent.PlayAudio(audio))
	}
	
}