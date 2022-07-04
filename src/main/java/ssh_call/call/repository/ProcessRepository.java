package ssh_call.call.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ssh_call.call.domain.Cmds;
import ssh_call.call.domain.Process;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
//@RequiredArgsConstructor
public class ProcessRepository {

    //@PersistenceContext // 영속성 컨택스트
    //@Autowired //사용가능
    private final EntityManager em; // spring-boot-data-jpa 가 알아서 엔티티매니저를 생성해줌
    //JPA는 EntityManager와 영속성 컨텍스트를 통해 데이터의 상태 변화를 감지하고 필요한 쿼리를 자동으로 수행한다.

    @Autowired
    public ProcessRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Process ps){
        em.persist(ps); //command 분리
        //return member.getId(); // ID를 리턴하면 다시 조회 할 경우 사용이 가능하여, 넣는다.
    }

    public Process findOne(Long processSeq){
        return em.find(Process.class, processSeq);
    }

    public List<Process> findAll(){
        List<Process> processList = em.createQuery("select m from Process m", Process.class)
                .getResultList();
        return processList;
    }

    public List<Process> findByName(String name){
        return em.createQuery("select p from Process p where p.name= :name", Process.class)
                .setParameter("name", name).getResultList();

    }

}
