package zerobase.weather.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import zerobase.weather.domain.Memo;

@SpringBootTest
@Transactional
public class JpaMemoRepositoryTest {
    @Autowired
    private JpaMemoRepository jpaMemoRepository;

    @Test
    void insertMemoTest() {
        Memo memo = new Memo(2, "this is jpa memo");
        jpaMemoRepository.save(memo);

        List<Memo> memoList = jpaMemoRepository.findAll();
        assertSame(memoList.size(), 2);
    }

    @Test
    void findByIdTest() {
        Memo newMemo = new Memo(11, "jpa");
        Memo memo = jpaMemoRepository.save(newMemo);

        Optional<Memo> memoOptional = jpaMemoRepository.findById(memo.getId());
        assertEquals(memoOptional.get().getText(), "jpa");
    }
}