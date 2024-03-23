package it.polimi.sw.gianpaolocugola47.model;
/**
 * It looks for a specific pattern of cards to fulfill an objective and includes the method to return the points
 */
public class TopLeftL extends Pattern {

    public TopLeftL(Resources mainResource, Resources secondaryResource) {
        super(mainResource, secondaryResource);
    }

    @Override
    public int checkPattern(PlayerTable playerTable) {
        int points = 3;
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
                    i++;
                    j++;
                }
                if(isMainResourceMatched(matrix[i][j])){
                    /*todo*/
                }
            }
        }
        return points*patternOccurrencies;
    }
}
