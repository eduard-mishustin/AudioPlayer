package magym.feature.genretablist

import android.os.Bundle
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
import org.koin.android.ext.android.get

internal class GenreTabFragment :
	MviFragment<GenreListIntent, GenreListViewState, GenreListSubscription>() {
	
	// fixme Падение при перевороте
	
	override val layoutId = R.layout.fragment_genres_tab
	
	private val genreId by argumentInt(KEY_GENRE_ID)
	
	private val adapter by lazy { GenreFragmentTabAdapter(supportFragmentManager, lifecycle) }
	
	
	override fun provideViewModel(): GenreTabViewModel = get()
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		checkArguments(KEY_GENRE_ID)
		
		if (savedInstanceState == null) postIntent(GenreListIntent.LoadData)
		view_pager.adapter = adapter
		view_pager.onPageSelected { postIntent(GenreListIntent.ChangeCurrentGenre(it)) }
	}
	
	override fun render(state: GenreListViewState) {
		if (state.currentGenre.title != "") activityProvider.titleToolbar = state.currentGenre.title
		
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