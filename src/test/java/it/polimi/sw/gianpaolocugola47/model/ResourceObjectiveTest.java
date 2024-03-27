package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceObjectiveTest {
    @Test
    public void testConstructor(){
        ResourceObjective ro = new ResourceObjective("imgPathFront", "imgPathBack", Resources.ANIMAL);
        assertNotNull(ro);
    }

    @Test
    public void getResource() {
        ResourceObjective resource = new ResourceObjective("immpathFront", "imPathBack", Resources.FUNGI);
        assertNotEquals(Resources.ANIMAL, resource.getResource());
    }
    @Test
    public void checkPatternAndComputePoints() {

    }
}