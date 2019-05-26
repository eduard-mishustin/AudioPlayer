package magym.core.data.data.base.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Observable
import io.reactivex.Single
import magym.core.data.data.base.dao.base.BaseDao
import magym.core.data.data.entity.Genre

@Dao
internal interface GenreDao : BaseDao<Genre> {
	
	@Query("SELECT * FROM Genre WHERE title LIKE :filterQuery ORDER BY title")
	fun getGenres(filterQuery: String): Observable<List<Genre>>
	
	@Query("SELECT * FROM Genre WHERE id = :id")
	fun getGenre(id: String): Single<Genre>
	
}