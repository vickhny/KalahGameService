package io.assignment.kalah.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.assignment.kalah.model.KalahGame;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameRepositoryTest {

    @Autowired
    private GameRepository repository;

    @Value("${kalah.pit.stones:6}")
    private int pitStoneCount;

    @Test
    public void testFind() {
        final KalahGame given = this.repository.save(new KalahGame(pitStoneCount));
        final KalahGame when = this.repository.findById(given.getId());

        Assert.assertNotNull(given);
        Assert.assertEquals(given, when);
    }

    @Test
    public void testSave() {
        final KalahGame game = this.repository.save(new KalahGame(pitStoneCount));
        Assert.assertNotNull(game);
    }
}
