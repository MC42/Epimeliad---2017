package util;

/**
 * A general matrix class
 * @author Connor_Hofenbitzer
 *
 */
public class NemesisMatrix {

  private int rows;
  private int columns;
  private double matrix[][];
  
  public NemesisMatrix(int rows, int columns) {
    this.rows = rows;
    this.columns = columns;
    matrix = new double[rows][columns];
  }
  
  public int getRows() {
    return rows;
  }
  
  public int getColumns() {
    return columns;
  }

  public double[][] getMatrix() {
    return matrix;
  }
  
  public void update(double newData[][]) {
    this.rows    = newData.length;
    this.columns = newData[0].length;
    this.matrix  = newData;
  }

  /**
   * Multiplies one matrix by another
   * @param matrixTwo : the other matrix to multiply the current one by
   * @return the resulting matrix
   */
  public NemesisMatrix multiplyBy(NemesisMatrix matrixTwo) {
    
    //checks if the operation is legal
    if(this.columns != matrixTwo.getRows()) {
      throw new IllegalArgumentException("the columns of matrix one must equal the rows of matrix two!");
    }
    
    //creates a new matrix instance
    NemesisMatrix returner = new NemesisMatrix(this.rows, matrixTwo.getColumns());
    double updatedArray[][] = new double[rows][matrixTwo.getColumns()];

    //uses dot multiplication
    for(int i = 0; i < this.rows; i++) {
      for(int f = 0; f < matrixTwo.getColumns(); f++) {
        for(int j = 0; j < this.columns; j++) {
          updatedArray[i][f] += (matrix[i][j] * matrixTwo.getMatrix()[j][f]);
        }
      }
    }
    
    //updates the matrix
    returner.update(updatedArray);
    return returner;
  }
  
  /**
   * Adds two matrixes
   * @param matrixTwo : the matrix to add to the current matrix
   * @return the resulting matrix
   */
  public NemesisMatrix add(NemesisMatrix matrixTwo) {
    
    //checks if the operation is legal
    if( ( this.columns != matrixTwo.getColumns() ) || 
        ( this.rows    != matrixTwo.getRows() ) ) {
      throw new IllegalArgumentException("the dimensions must be equal");
    }
    
    //creates a new matrix instance
    NemesisMatrix returner = new NemesisMatrix(rows, columns);
    double updatedArray[][] = new double[rows][columns];
    
    //performs the operation
    for(int i = 0; i < rows; i++) {
      for(int f = 0; f < columns; f++) {
        updatedArray[i][f] = matrix[i][f] + matrixTwo.getMatrix()[i][f];
      }
    }
    
    //updates the matrix and returns
    returner.update(updatedArray);
    return returner;
  }
  
  /**
   * Checks if two matrixes are equal
   * @param matrixTwo : the matrix to check it against
   * @return if the matrixes are exactly equal
   */
  public boolean matrixesAreEqual(NemesisMatrix matrixTwo) {
    
    //checks if the operation is legal
    if( ( this.columns != matrixTwo.getColumns() ) || 
        ( this.rows    != matrixTwo.getRows() ) ) {
      return false;
    }
    
    //checks every individual cell
    for(int i = 0; i < rows; i++) {
      for(int f = 0; f < columns; f++) {
        if(matrix[i][f] != matrixTwo.getMatrix()[i][f]) {
          return false;
        }
      }
    }
    
    return true;
  }
  
  public void transpose() {
    
    //create an array to store swapped values
    double arrayAfterTranspose[][] = new double[matrix[0].length][matrix.length];
    
    //loop through the arrays
    for(int i = 0; i < rows; i++) {
      for(int f = 0; f < columns; f++){
        arrayAfterTranspose[f][i] = this.matrix[i][f];
      }
    }
    
    //set the matrixes
    this.matrix = arrayAfterTranspose;
  }
  
}
