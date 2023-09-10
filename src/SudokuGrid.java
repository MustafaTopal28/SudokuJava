import java.util.*;
import java.util.Scanner;

public class SudokuGrid {
    private int[][] grid;

    public SudokuGrid() {
        grid = new int[9][9];
        generateValidSudokuGrid();
        removeNumbers(50); // Vous pouvez définir le nombre de chiffres à supprimer ici
    }

    public void initializeGrid(int[][] initialValues) {
        if (initialValues != null && initialValues.length == 9 && initialValues[0].length == 9) {
            // Copiez les valeurs initiales dans la grille
            for (int i = 0; i < 9; i++) {
                System.arraycopy(initialValues[i], 0, grid[i], 0, 9);
            }
        }
    }

    public boolean isValidMove(int row, int col, int num) {
        if (num < 1 || num > 9 || grid[row][col] != 0) {
            return false;
        }

        // Vérification de la ligne et de la colonne
        for (int i = 0; i < 9; i++) {
            if (grid[row][i] == num || grid[i][col] == num) {
                return false;
            }
        }

        // Vérification de la région 3x3
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (grid[i][j] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isSolved() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (grid[row][col] == 0) {
                    return false; // Il reste des cases vides
                }
            }
        }
        return true;
    }

    public int[][] getGrid() {
        return grid;
    }

    public void setCell(int row, int col, int num) {
        if (isValidMove(row, col, num)) {
            grid[row][col] = num;
        }
    }

    public void clearCell(int row, int col) {
        if (grid[row][col] != 0) {
            grid[row][col] = 0;
        }
    }

    public void generateValidSudokuGrid() {
        Random random = new Random();

        // Réinitialiser la grille avec des zéros
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j] = 0;
            }
        }

        // Remplir la grille avec des valeurs valides
        fillValidSudoku(grid, random);
    }

    private boolean fillValidSudoku(int[][] board, Random random) {
        // Trouver la prochaine case vide
        int[] nextEmpty = findEmptyCell(board);
        int row = nextEmpty[0];
        int col = nextEmpty[1];

        // Si aucune case vide n'a été trouvée, la grille est remplie
        if (row == -1 && col == -1) {
            return true;
        }

        // Générer un ordre aléatoire pour les nombres de 1 à 9
        List<Integer> randomOrder = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        Collections.shuffle(randomOrder);

        for (int num : randomOrder) {
            if (isValidMove(row, col, num)) {
                board[row][col] = num;

                // Récursivement, tenter de remplir le reste de la grille
                if (fillValidSudoku(board, random)) {
                    return true;
                }

                // Si la tentative échoue, réinitialisez la case et essayez le prochain nombre
                board[row][col] = 0;
            }
        }

        // Aucun nombre valide n'a été trouvé pour cette case, retournez false.
        return false;
    }

    private int[] findEmptyCell(int[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{-1, -1}; // Aucune case vide
    }

    private boolean isValidSudoku() {
        // Vérification des lignes
        for (int row = 0; row < 9; row++) {
            boolean[] used = new boolean[10];
            for (int col = 0; col < 9; col++) {
                int num = grid[row][col];
                if (num != 0) {
                    if (used[num]) {
                        return false; // Le même nombre apparaît plus d'une fois dans la ligne
                    }
                    used[num] = true;
                }
            }
        }

        // Vérification des colonnes
        for (int col = 0; col < 9; col++) {
            boolean[] used = new boolean[10];
            for (int row = 0; row < 9; row++) {
                int num = grid[row][col];
                if (num != 0) {
                    if (used[num]) {
                        return false; // Le même nombre apparaît plus d'une fois dans la colonne
                    }
                    used[num] = true;
                }
            }
        }

        // Vérification des sous-grilles 3x3
        for (int row = 0; row < 9; row += 3) {
            for (int col = 0; col < 9; col += 3) {
                boolean[] used = new boolean[10];
                for (int i = row; i < row + 3; i++) {
                    for (int j = col; j < col + 3; j++) {
                        int num = grid[i][j];
                        if (num != 0) {
                            if (used[num]) {
                                return false; // Le même nombre apparaît plus d'une fois dans la sous-grille 3x3
                            }
                            used[num] = true;
                        }
                    }
                }
            }
        }

        return true; // La grille est valide
    }

    public void printGrid() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println(); // Nouvelle ligne pour chaque ligne de la grille
        }
    }

    // Méthode pour retirer un certain nombre de chiffres de la grille
    public void removeNumbers(int numToRemove) {
        if (numToRemove < 0 || numToRemove > 81) {
            System.out.println("Le nombre de chiffres à supprimer doit être entre 0 et 81.");
            return;
        }

        Random random = new Random();
        int count = 0;

        while (count < numToRemove) {
            int row = random.nextInt(9);
            int col = random.nextInt(9);

            if (grid[row][col] != 0) {
                grid[row][col] = 0;
                count++;
            }
        }
    }

    public static void main(String[] args) {
        SudokuGrid sudokuGrid = new SudokuGrid();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bienvenue dans le Sudoku!");

        while (!sudokuGrid.isSolved()) {
            System.out.println("\nGrille actuelle :");
            sudokuGrid.displayGrid();

            System.out.print("\nVoulez-vous remplir une cellule? (Oui/Non): ");
            String response = scanner.next();

            if (response.equalsIgnoreCase("non")) {
                break;
            }

            if (response.equalsIgnoreCase("oui")) {
                System.out.print("Entrez la ligne (1-9) : ");
                int row = scanner.nextInt() - 1; // Convertir de 1-9 à 0-8

                System.out.print("Entrez la colonne (1-9) : ");
                int col = scanner.nextInt() - 1; // Convertir de 1-9 à 0-8

                System.out.print("Entrez le nombre (1-9) : ");
                int num = scanner.nextInt();

                sudokuGrid.fillCell(row, col, num);

                // Vérifier si le Sudoku est valide après chaque coup de l'utilisateur
                if (!sudokuGrid.isValidSudoku()) {
                    System.out.println("\nLe Sudoku n'est pas valide.");
                    break; // Sortir de la boucle si la grille n'est pas valide
                }
            }
        }

        // Afficher le résultat final
        System.out.println("\nGrille finale :");
        sudokuGrid.displayGrid();

        if (sudokuGrid.isSolved() && sudokuGrid.isValidSudoku()) {
            System.out.println("\nFélicitations, vous avez résolu le Sudoku !");
        } else {
            System.out.println("\nDommage, la grille n'est pas résolue ou n'est pas valide.");
        }
    }

    public void displayGrid() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(grid[i][j] + " ");
                if ((j + 1) % 3 == 0) {
                    System.out.print(" "); // Ajoute un espace supplémentaire chaque 3 colonnes
                }
            }
            System.out.println(); // Nouvelle ligne pour chaque ligne de la grille
            if ((i + 1) % 3 == 0 && i != 8) {
                System.out.println(); // Ajoute une ligne vide chaque 3 lignes
            }
        }
    }



    public void fillCell(int row, int col, int num) {
        if (isValidMove(row, col, num)) {
            grid[row][col] = num;
        } else {
            System.out.println("Mouvement invalide. Veuillez réessayer.");
        }
    }

    public static int getUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Entrez un nombre (1-9) : ");
        int num = scanner.nextInt();
        return num;
    }


}

