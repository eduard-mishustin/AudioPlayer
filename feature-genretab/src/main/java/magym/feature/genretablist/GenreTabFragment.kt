package magym.feature.genretablist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.viewpager2.widget.ViewPager2
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_genres_tab.*
import magym.core.common.KEY_GENRE_ID
import magym.core.common.extention.argumentInt
import magym.core.common.extention.findSearchView
import magym.core.common.extention.onTextChanged
import magym.core.common.mvi.MviFragment
import magym.feature.audiolist.SearchViewProvider
import magym.feature.genretablist.mvi.GenreListIntent
import magym.feature.genretablist.mvi.GenreListSubscription
import magym.feature.genretablist.mvi.GenreListViewState
import magym.feature.genretablist.recycler.GenreFragmentTabAdapter
import org.koin.android.ext.android.get


internal class GenreTabFragment :
	MviFragment<GenreListIntent, GenreListViewState, GenreListSubscription>() {
	
	// fixme Падение при перевороте
	
	override val layoutId = R.layout.fragment_genres_tab
	
	override val menuResource = R.menu.audiolist_menu
	
	private val genreId by argumentInt(KEY_GENRE_ID)
	
	private val adapter by lazy { GenreFragmentTabAdapter(supportFragmentManager, lifecycle) }
	
	private lateinit var searchView: SearchView
	private val searchViewProvider: SearchViewProvider = get()
	private val isSearchMode get() = currentState?.isSearchMode ?: false
	
	
	override fun provideViewModel(): GenreTabViewModel = get()
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		checkArguments(KEY_GENRE_ID)
		toolbar.init(enableArrowUp = true)
		
		if (savedInstanceState == null) postIntent(GenreListIntent.LoadData)
		view_pager.adapter = adapter
		view_pager.onPageSelected { postIntent(GenreListIntent.ChangeCurrentGenre(it)) }
	}
	
	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		super.onCreateOptionsMenu(menu, inflater)
		
		searchView = menu.findSearchView(R.id.search)
		
		disposable += Observable.create(ObservableOnSubscribe<String> { subscriber ->
			searchView.onTextChanged { subscriber.onNext(it) }
		}).subscribe {
			postIntent(GenreListIntent.ChangeSearchMode(it.isNotEmpty(), view_pager.currentItem))
			searchViewProvider.textChanges.onNext(it)
		}
	}
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home -> {
				if (!searchView.isIconified) {
					searchView.isIconified = true
				} else {
					activity.onBackPressed()
				}
			}
			
			else -> return super.onOptionsItemSelected(item)
		}
		
		return true
	}
	
	override fun render(state: GenreListViewState) {
		if (state.currentGenre.title != "") titleToolbar = state.currentGenre.title
		
		val isFirstGetList = adapter.items.isEmpty() && state.genres.isNotEmpty()
		adapter.items = state.genres
		if (isFirstGetList) view_pager.setCurrentItem(adapter.getItemPosition(genreId), false)
		
		if (state.isSearchMode == view_pager.isUserInputEnabled) {
			changeSearchMode(state)
		}
	}
	
	override fun onSubscriptionReceived(subscription: GenreListSubscription) {
		when (subscription) {
			is GenreListSubscription.RemoteRequestError -> {
				layout.showErrorSnackBarWithAction { postIntent(GenreListIntent.LoadData) }
			}
		}
	}
	
	
	private fun changeSearchMode(state: GenreListViewState) {
		// Создание отдельного таба для поиска и последующая навигация
		if (state.isSearchMode) {
			adapter.addFragmentToEnd()
			// Если smoothScroll == false, то RecyclerView внутри фрагмента не сохраняет своё местоположение -_-
			view_pager.setCurrentItem(adapter.lastFragmentIndex, true)
		} else {
			view_pager.setCurrentItem(state.currentPageItem, true)
			adapter.removeLastFragment()
		}
		
		view_pager.isUserInputEnabled = !state.isSearchMode
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