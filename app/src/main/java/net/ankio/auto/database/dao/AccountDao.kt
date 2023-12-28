/*
 * Copyright (C) 2023 ankio(ankio@ankio.net)
 * Licensed under the Apache License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-3.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package net.ankio.auto.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import net.ankio.auto.database.table.Account

@Dao
interface AccountDao {

    @Query("DELETE FROM Account WHERE id=:id")
    suspend fun del(id: Int)


    @Insert
    suspend fun add(account: Account)


    @Query("UPDATE  Account set sort=:sort WHERE id=:id")
    suspend fun setSort(id: Int, sort: Int)
    @Query("SELECT * FROM  Account WHERE name=:account limit 1")
    suspend fun get(account: String):Account?
}
