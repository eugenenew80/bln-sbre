package bln.sbre.repo;

import bln.sbre.entity.PlanError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

@Repository
@Transactional
public interface PlanErrorRepo extends JpaRepository<PlanError, String> { }
