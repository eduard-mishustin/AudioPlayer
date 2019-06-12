package magym.feature.genrelist

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.fragment_genres.*
import magym.core.common.extention.init
import magym.core.common.mvi.MviFragment
import magym.core.data.data.entity.Genre
import magym.feature.genrelist.mvi.GenreListIntent
import magym.feature.genrelist.mvi.GenreListSubscription
import magym.feature.genrelist.mvi.GenreListViewState
import magym.feature.genrelist.recycler.GenreAdapter
import org.koin.androidx.viewmodel.ext.android.getViewModel

internal class GenreListFragment :
	MviFragment<GenreListIntent, GenreListViewState, GenreListSubscription>() {
	
	override val layoutId = R.layout.fragment_genres
	
	override val menuResource = R.menu.genrelist_menu
	
	private val adapter: GenreAdapter = GenreAdapter(::onItemClick)
	
	
	override fun provideViewModel(): GenreListViewModel = getViewModel()
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		toolbar.init(enableArrowUp = false)
		
		if (savedInstanceState == null) postIntent(GenreListIntent.LoadData())
		recycler_view.init(adapter)
	}
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.search -> navigator.toAudioSearch()
			else -> return super.onOptionsItemSelected(item)
		}
		
		return true
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
		navigator.toGenreTab(genre.id)
	}
	
}