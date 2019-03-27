package org.mindtrails.persistence;

import org.mindtrails.domain.Participant;
import org.mindtrails.domain.tracking.GiftLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftLogRepository extends JpaRepository<GiftLog, Long> {

    GiftLog findByParticipantAndSessionName(Participant p,  String sessionName);
    Page<GiftLog> findByOrderIdIsNull(Pageable pageable);
    Long countGiftLogByOrderIdIsNotNull();

    @Query(value = "select sum(amount) from GiftLog where order_id is not null")
    Long totalAmountAwarded();

    @Query("select log from GiftLog as log LEFT JOIN log.participant p where " +
            "log.orderId is null and p.testAccount = false order by log.dateCreated desc")
    Page<GiftLog> awardableGiftLogs(Pageable pageable);

}
