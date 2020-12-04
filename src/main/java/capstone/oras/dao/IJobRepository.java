package capstone.oras.dao;

import capstone.oras.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IJobRepository extends JpaRepository<JobEntity, Integer> {
    Optional<List<JobEntity>> findAllByStatus(String status);
    boolean existsByCreatorIdEqualsAndTitleEqualsAndStatusIsNot(int creatorId, String title, String status);
    Optional<List<JobEntity>> findJobEntitiesByCreatorIdEquals(int creatorId);
    Optional<List<JobEntity>> findJobEntitiesByCreatorIdEqualsAndStatusEquals(int creatorId, String status);
    Optional<List<JobEntity>> findJobEntitiesByCreatorIdEqualsAndStatusIn(int creatorId, List<String> status);

    @Query(value = "select j.id, " +
            "       (select count(*) " +
            "       from job_application ja " +
            "       where ja.job_id = j.id) as total_application " +
            "from job j " +
            "where j.status = 'Published'" +
            "group by j.id " +
            "order by j.id;",
            nativeQuery = true)
    List<Integer[]> findEntityAndTotalApplication();
    @Query(value = "select j.id, " +
            "       (select count(*) " +
            "       from job_application ja " +
            "       where ja.job_id = j.id) as total_application " +
            "from job j " +
            "where j.status = 'Published'" +
            "and j.creator_id = :creatorId " +
            "group by j.id " +
            "order by j.id;",
            nativeQuery = true)
    List<Integer[]> findEntityAndTotalApplication(@Param("creatorId") int creatorId);
}
