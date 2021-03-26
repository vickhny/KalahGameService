package io.assignment.kalah.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameNotFoundExceptionTest {

    @Test
    public void testInitialization() {
        final GameNotFoundException givenGameNotFoundException = new GameNotFoundException("123");
        Assert.assertEquals("Could not find game 123", givenGameNotFoundException.getMessage());
    }
}
