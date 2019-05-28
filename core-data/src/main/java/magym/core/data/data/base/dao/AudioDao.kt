package magym.core.data.data.base.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Single
import magym.core.data.data.base.dao.base.BaseDao
import magym.core.data.data.entity.Audio

@Dao
internal interface AudioDao : BaseDao<Audio> {
	
	@Query("SELECT * FROM Audio JOIN AudioGenre WHERE AudioGenre.genreId = :genreId AND AudioGenre.audioId = Audio.id ORDER BY timeAdded")
	fun getAudios(genreId: Int): DataSource.Factory<Int, Audio>
	
	@Query("SELECT * FROM Audio WHERE (title LIKE :filterQuery) OR (artist LIKE :filterQuery) GROUP BY title ORDER BY timeAdded")
	fun getAudios(filterQuery: String): DataSource.Factory<Int, Audio>
	
	@Query("SELECT * FROM Audio WHERE id = (:id)")
	fun getAudio(id: String): Single<Audio>
	
}