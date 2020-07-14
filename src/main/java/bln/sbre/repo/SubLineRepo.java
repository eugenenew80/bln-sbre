package bln.sbre.repo;

import bln.sbre.entity.Line;
import bln.sbre.entity.SubLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface SubLineRepo extends JpaRepository<SubLine, Long> {
    List<SubLine> findAllByLine(Line line);
}
