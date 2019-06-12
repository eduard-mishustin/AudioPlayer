package magym.feature.genretablist

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_genres_tab.*
import magym.core.common.KEY_GENRE_ID
import magym.core.common.extention.argumentInt
import magym.core.common.mvi.MviFragment
import magym.feature.genretablist.mvi.GenreListIntent
import magym.feature.genretablist.mvi.GenreListSubscription
import magym.feature.genretablist.mvi.GenreListViewState
import magym.feature.genretablist.recycler.GenreFragmentTabAdapter
import org.koin.androidx.viewmodel.ext.android.getViewModel


internal class GenreTabFragment :
	MviFragment<GenreListIntent, GenreListViewState, GenreListSubscription>() {
	
	override val layoutId = R.layout.fragment_genres_tab
	
	override val menuResource = R.menu.audiolist_menu
	
	private val genreId by argumentInt(KEY_GENRE_ID)
	
	// fixme (ниже костыль): Падение при возвращении из backStack'a: "IllegalStateException: Expected the adapter to be 'fresh' while restoring state"
	//private val adapter by lazy { GenreFragmentTabAdapter(childFragmentManager, lifecycle) }
	private lateinit var adapter: GenreFragmentTabAdapter
	
	
	override fun provideViewModel(): GenreTabViewModel = getViewModel()
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		checkArguments(KEY_GENRE_ID)
		toolbar.init()
		
		postIntent(GenreListIntent.LoadData)
		adapter = GenreFragmentTabAdapter(childFragmentManager, lifecycle)
		view_pager.adapter = adapter
		view_pager.onPageSelected { postIntent(GenreListIntent.ChangeCurrentGenre(it)) }
	}
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.search -> navigator.toAudioSearch()
			else -> return super.onOptionsItemSelected(item)
		}
		
		return true
	}
	
	override fun render(state: GenreListViewState) {
		if (state.currentGenre.title != "") titleToolbar = state.currentGenre.title
		
		val isFirstGetList = adapter.items.isEmpty() && state.genres.isNotEmpty()
		adapter.items = state.genres
		if (isFirstGetList) view_pager.setCurrentItem(adapter.getItemPosition(genreId), false)
	}
	
	override fun onSubscriptionReceived(subscription: GenreListSubscription) {
		when (subscription) {
			is GenreListSubscription.RemoteRequestError -> {
				layout.showErrorSnackBarWithAction { postIntent(GenreListIntent.LoadData) }
			}
		}
	}
	
	
	companion object {
		
		private fun ViewPager2.onPageSelected(callback: (position: Int) -> Unit) {
			registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
				override fun onPageSelected(position: Int) {
					callback.invoke(position)
				}
			})
		}
		
	}
	
}