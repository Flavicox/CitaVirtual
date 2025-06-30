package com.flavicox.citavirtual.bd.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.flavicox.citavirtual.bd.entity.DoctorEntidad
import kotlinx.coroutines.flow.Flow

@Dao
interface DoctorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(lista: List<DoctorEntidad>)

    @Query("SELECT * FROM doctor")
    fun observarDoctores(): Flow<List<DoctorEntidad>>
}