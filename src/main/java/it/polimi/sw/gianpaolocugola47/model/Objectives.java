package it.polimi.sw.gianpaolocugola47.model;

/**
 * This class represents all the objectives in a game with their points,the reference at the front and back image.
 */
public abstract class Objectives {
    private int points;
    private String imgPathFront;
    private String imgPathBack;
    /**
     * Corner constructor.
     *
     * @param points        the points given by the card.
     * @param imgPathFront  the reference to the front image's path.
     * @param imgPathBack   the reference to the back image's path.
     */
    public Objectives(int points, String imgPathFront, String imgPathBack) {
        this.points = points;
        this.imgPathFront = imgPathFront;
        this.imgPathBack = imgPathBack;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getImgPathFront() {
        return imgPathFront;
    }

    public void setImgPathFront(String imgPathFront) {
        this.imgPathFront = imgPathFront;
    }

    public String getImgPathBack() {
        return imgPathBack;
    }

    public void setImgPathBack(String imgPathBack) {
        this.imgPathBack = imgPathBack;
    }

}
