package magym.core.data.data.base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import magym.core.data.BuildConfig
import magym.core.data.data.base.dao.AudioDao
import magym.core.data.data.base.dao.AudioGenreDao
import magym.core.data.data.base.dao.GenreDao
import magym.core.data.data.entity.Audio
import magym.core.data.data.entity.AudioGenre
import magym.core.data.data.entity.Genre

@Database(
	version = 1,
	entities = [Audio::class, Genre::class, AudioGenre::class]
)
internal abstract class AudioPlayerDatabase : RoomDatabase() {
	
	abstract fun getAudioDao(): AudioDao
	
	abstract fun getGenreDao(): GenreDao
	
	abstract fun getAudioGenreDao(): AudioGenreDao
	
	companion object {
		
		@Synchronized
		fun createDatabase(
			appContext: Context,
			databaseName: String = "database"
		): AudioPlayerDatabase {
			if (BuildConfig.DEBUG) {
				/*Stetho.initialize( // Табличный максимум - 250 записей -_-
					Stetho.newInitializerBuilder(appContext)
						.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(appContext))
						.build()
				)*/
			}
			
			return Room.databaseBuilder(appContext, AudioPlayerDatabase::class.java, databaseName)
				.fallbackToDestructiveMigration() // Attention
				.build()
		}
		
	}
	
}