package org.dt2.abrinimal.repositories;

import org.dt2.abrinimal.entities.Animais;
import org.springframework.data.jpa.repository.JpaRepository;
//Nicolas Massuda<@massudesu>
public interface AnimaisRepository extends JpaRepository<Animais, Long> {
}
