package bln.sbre.repo;

import bln.sbre.entity.LineInterface;
import bln.sbre.entity.enums.BatchStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface LineInterfaceRepo extends JpaRepository<LineInterface, Long> {
    @Query("select t from LineInterface  t where t.status = :status and t.line is not null order by t.id")
    List<LineInterface> findAllByStatus(
        @Param("status") BatchStatusEnum status
    );

    @Procedure(name = "LineInterface.updateStatuses")
    void updateStatuses(
        @Param("p_header_id") Long headerId
    );
}
