import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Class in charge of generating test files with pseudo-random data
 * for the sales system.
 * 
 * @author Code Raiders
 * @version 1.0
 */
public class GenerateInfoFiles {

    private static final String[] FIRST_NAMES = { "Juan", "Maria", "Carlos", "Ana", "Luis", "Laura", "Pedro", "Sofia" };
    private static final String[] LAST_NAMES = { "Gomez", "Rodriguez", "Perez", "Lopez", "Martinez", "Garcia",
            "Hernandez" };
    private static final String[] DOCUMENT_TYPES = { "CC", "CE", "TI" };
    private static final String[] PRODUCT_NAMES = { "Laptop", "Mouse", "Teclado", "Monitor", "Audifonos", "Impresora",
            "Silla Gaming", "Pad Mouse" };

    private static final Random RANDOM = new Random();

    /**
     * Generates the products info file.
     * Format: IDProduct;ProductName;PricePerUnit
     * 
     * @param productsCount Number of products to generate
     */
    public static void createProductsFile(int productsCount) {
        String fileName = "productos.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 1; i <= productsCount; i++) {
                String productId = String.format("P%03d", i);
                String productName = PRODUCT_NAMES[RANDOM.nextInt(PRODUCT_NAMES.length)];
                int price = 1000 + RANDOM.nextInt(49001);
                writer.write(productId + ";" + productName + ";" + price);
                writer.newLine();
            }
            System.out.println("✓ Archivo de productos generado exitosamente: " + fileName);
        } catch (IOException e) {
            System.err.println("✗ Error al generar archivo de productos: " + e.getMessage());
        }
    }

    /**
     * Generates the salesmen info file.
     * Format: DocumentType;DocumentNumber;FirstNames;LastNames
     * 
     * @param salesmanCount Number of salesmen to generate
     */
    public static void createSalesManInfoFile(int salesmanCount) {
        String fileName = "vendedores.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 1; i <= salesmanCount; i++) {
                String documentType = DOCUMENT_TYPES[RANDOM.nextInt(DOCUMENT_TYPES.length)];
                long documentNumber = 10000000L + RANDOM.nextInt(90000000);
                String firstNames = FIRST_NAMES[RANDOM.nextInt(FIRST_NAMES.length)];
                String lastNames = LAST_NAMES[RANDOM.nextInt(LAST_NAMES.length)];
                writer.write(documentType + ";" + documentNumber + ";" + firstNames + ";" + lastNames);
                writer.newLine();
            }
            System.out.println("✓ Archivo de vendedores generado exitosamente: " + fileName);
        } catch (IOException e) {
            System.err.println("✗ Error al generar archivo de vendedores: " + e.getMessage());
        }
    }

    /**
     * Generates the sales file for a single salesman.
     * First line: DocumentType;DocumentNumber
     * Remaining lines: IDProduct;SoldQuantity;
     * 
     * @param randomSalesCount Number of sales to generate
     * @param name             Salesman name (used to name the file)
     * @param id               Salesman document number
     */
    public static void createSalesMenFile(int randomSalesCount, String name, long id) {
        String fileName = "ventas_" + name.toLowerCase() + "_" + id + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            String documentType = DOCUMENT_TYPES[RANDOM.nextInt(DOCUMENT_TYPES.length)];
            writer.write(documentType + ";" + id);
            writer.newLine();

            for (int i = 0; i < randomSalesCount; i++) {
                String productId = String.format("P%03d", 1 + RANDOM.nextInt(PRODUCT_NAMES.length));
                int quantity = 1 + RANDOM.nextInt(20);
                writer.write(productId + ";" + quantity + ";");
                writer.newLine();
            }
            System.out.println("✓ Archivo de ventas generado exitosamente: " + fileName);
        } catch (IOException e) {
            System.err.println("✗ Error al generar archivo de ventas: " + e.getMessage());
        }
    }

    /**
     * Main method. Generates all test files.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        createSalesManInfoFile(5);
        createProductsFile(8);

        createSalesMenFile(5, "Juan", 12345678);
        createSalesMenFile(3, "Maria", 23456789);
        createSalesMenFile(8, "Carlos", 34567890);
        createSalesMenFile(4, "Ana", 45678901);
        createSalesMenFile(6, "Luis", 56789012);

        System.out.println("\n¡Todos los archivos de prueba fueron generados exitosamente!");
    }
}
