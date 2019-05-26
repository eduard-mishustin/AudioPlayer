package magym.core.data.data.base.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Single
import magym.core.data.data.base.dao.base.BaseDao
import magym.core.data.data.entity.Audio

@Dao
internal interface AudioDao : BaseDao<Audio> {
	
	@Query("SELECT * FROM Audio WHERE genreId = (:genreId) AND title LIKE :filterQuery ORDER BY `index`")
	fun getAudios(genreId: Int, filterQuery: String): DataSource.Factory<Int, Audio>
	
	@Query("SELECT * FROM Audio WHERE id = (:id)")
	fun getAudio(id: String): Single<Audio>
	
}