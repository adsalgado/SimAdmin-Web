/*
 * Copyright 2017 JoinFaces.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mx.sharkit.web.repository;

import mx.sharkit.web.dao.BaseRepository;
import mx.sharkit.web.model.Zona;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author malcantara
 */
public interface ZonaRepository extends BaseRepository<Zona, Integer> {
    @Query("select z from Zona z where z.id = ?1")
    Zona findById(int zonaId);
    
}
