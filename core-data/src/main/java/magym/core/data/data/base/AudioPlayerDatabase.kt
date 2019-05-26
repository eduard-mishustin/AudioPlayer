package magym.core.data.data.base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import magym.core.data.data.base.dao.AudioDao
import magym.core.data.data.base.dao.GenreDao
import magym.core.data.data.entity.Audio
import magym.core.data.data.entity.Genre

@Database(
	version = 1,
	entities = [Audio::class, Genre::class]
)
internal abstract class AudioPlayerDatabase : RoomDatabase() {
	
	abstract fun getAudioDao(): AudioDao
	
	abstract fun getGenreDao(): GenreDao
	
	companion object {
		
		@Synchronized
		fun getInstance(
			appContext: Context,
			databaseName: String = "database"
		): AudioPlayerDatabase {
			return Room.databaseBuilder(appContext, AudioPlayerDatabase::class.java, databaseName)
				.fallbackToDestructiveMigration() // Attention
				.build()
		}
		
	}
	
}