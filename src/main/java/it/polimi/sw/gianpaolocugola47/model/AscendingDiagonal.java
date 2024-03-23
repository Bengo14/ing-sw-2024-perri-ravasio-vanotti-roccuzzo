package it.polimi.sw.gianpaolocugola47.model;
/**
 * It looks for a specific pattern of cards to fulfill an objective and includes the method to return the points
 */
public class AscendingDiagonal extends Pattern {

    public AscendingDiagonal(Resources mainResource, Resources secondaryResource) {
        super(mainResource, secondaryResource);
    }

    @Override
    public int checkPattern(PlayerTable playerTable) {
        int points = 2;
        //creating a copy of the matrix
        PlaceableCard[][] matrix = new PlaceableCard[PlayerTable.getMatrixDimension()][PlayerTable.getMatrixDimension()];
        for (int i = 0; i < PlayerTable.getMatrixDimension(); i++) {
            for (int j = 0; j < PlayerTable.getMatrixDimension(); j++) {
                matrix[i][j] = playerTable.getElement(i, j);
            }
        }
        //looking for pattern occurrencies
        int patternOccurrencies=0;
        for (int i = 0; i < PlayerTable.getMatrixDimension(); i++) {
            for (int j = 0; j < PlayerTable.getMatrixDimension(); j++) {
                if (playerTable.isStartingCard(i,j)){
                    j++;
                }
                if(isMainResourceMatched(matrix[i][j]) && i<(playerTable.getMatrixDimension()-2) && j>1){
                    if(isMainResourceMatched(matrix[i-1][j-1]) && !playerTable.isStartingCard(i-1, j-1)){
                        if(isMainResourceMatched(matrix[i-2][j-2]) && !playerTable.isStartingCard(i-2,j-2)){
                            matrix[i][j].setFlaggedForObjective(true);
                            matrix[i-1][j-1].setFlaggedForObjective(true);
                            matrix[i-2][j-2].setFlaggedForObjective(true);
                            patternOccurrencies++;
                        }
                    }
                }
            }
        }
        return points*patternOccurrencies;
    }
}