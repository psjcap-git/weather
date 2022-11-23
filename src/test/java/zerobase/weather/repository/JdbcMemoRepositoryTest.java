package zerobase.weather.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import zerobase.weather.domain.Memo;

@SpringBootTest
@Transactional
public class JdbcMemoRepositoryTest {
    @Autowired
    private JdbcMemoRepository jdbcMemoRepository;

    @Test
    void insertMemoTest() {
        // given
        Memo memo = new Memo(2, "insert memo test");

        // when        
        jdbcMemoRepository.save(memo);

        // then    
        Optional<Memo> memoOptional = jdbcMemoRepository.findById(2);    
        assertEquals(memoOptional.get().getText(), "insert memo test");
    }

    @Test
    void findAllMemoTest() {
        List<Memo> memoList = jdbcMemoRepository.findAll();
        System.out.println("============================================================================================================");
        System.out.println(memoList.toString());
        System.out.println("============================================================================================================");
        assertNotNull(memoList);        
    }
}
