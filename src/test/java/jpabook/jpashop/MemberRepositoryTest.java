package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    // 엔티티 변경은 반드시 트랜잭션 하에서 이루어져야 한다. 그래서 이 어노테이션이 없으면 오류가 난다.
    // 그리고 트랜잭셔널 옵션이 있으면 테스트 케이스의 경우에는 데이터를 넣어도 바로 롤백을 한다.
    @Rollback(false)
    // 이렇게 롤백 여부를 선택할 수도 있다.
    public void testMember() throws Exception {
        // given 조건 만들기
        Member member = new Member();
        member.setUsername("memberA");

        // when 테스트 수행
        Long savedId = memberRepository.save(member);
        Member findMember = memberRepository.find(savedId);

        //then 검증
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
        // 같은 영속성 컨텍스트에 있는 엔티티이므로 서로 같다.
    }

}