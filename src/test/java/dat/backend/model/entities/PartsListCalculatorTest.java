package dat.backend.model.entities;

import org.junit.jupiter.api.Test;

import javax.servlet.http.Part;

import static org.junit.jupiter.api.Assertions.*;

class PartsListCalculatorTest
{

    @Test
    void poleCalc()
    {
        int actual = PartsListCalculator.poleCalc(500, 420);
        int expected = 8;
        assertEquals(actual, expected);
    }
}