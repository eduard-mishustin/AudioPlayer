package magym.feature.genrelist

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_genres.*
import magym.core.common.extention.init
import magym.core.common.mvi.MviFragment
import magym.core.data.data.entity.Genre
import magym.feature.genrelist.mvi.GenreListIntent
import magym.feature.genrelist.mvi.GenreListSubscription
import magym.feature.genrelist.mvi.GenreListViewState
import magym.feature.genrelist.recycler.GenreAdapter
import org.koin.android.ext.android.get

internal class GenreListFragment :
	MviFragment<GenreListIntent, GenreListViewState, GenreListSubscription>() {
	
	override val layoutId = R.layout.fragment_genres
	
	private val adapter: GenreAdapter = GenreAdapter(::onItemClick)
	
	
	override fun provideViewModel(): GenreListViewModel = get()
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		if (savedInstanceState == null) postIntent(GenreListIntent.LoadData())
		activityProvider.titleToolbar = "Жанры"
		recycler_view.init(adapter)
	}
	
	override fun render(state: GenreListViewState) {
		activityProvider.isLoading = state.isLoading
		adapter.items = state.genres
	}
	
	override fun onSubscriptionReceived(subscription: GenreListSubscription) {
		when (subscription) {
			is GenreListSubscription.RemoteRequestError -> {
				layout.showErrorSnackBarWithAction { postIntent(GenreListIntent.LoadData()) }
			}
		}
	}
	
	
	private fun onItemClick(genre: Genre) {
		navigation.toAudioList(genre.id)
	}
	
}