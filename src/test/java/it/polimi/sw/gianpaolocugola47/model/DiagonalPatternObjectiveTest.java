package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class DiagonalPatternObjectiveTest {

    @Test
    public void testConstructor() {
        DiagonalPatternObjective d = new DiagonalPatternObjective( 0,"front", "back", true, Resources.FUNGI );
        DiagonalPatternObjective dc = new DiagonalPatternObjective( 0,"front","back" , true, Resources.FUNGI);
        assertNotNull(dc);
        assertEquals(d, dc);
    }
}