import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Clase encargada de generar archivos de prueba con datos pseudoaleatorios
 * para el sistema de ventas.
 * 
 * @author Code Raiders
 * @version 1.0
 */
public class GenerateInfoFiles {

    private static final String[] FIRST_NAMES = { "Juan", "Maria", "Carlos", "Ana", "Luis", "Laura", "Pedro", "Sofia" };
    private static final String[] LAST_NAMES = { "Gomez", "Rodriguez", "Perez", "López", "Martinez", "Garcia",
            "Hernandez" };
    private static final String[] DOCUMENT_TYPES = { "CC", "CE", "TI" };
    private static final String[] PRODUCT_NAMES = { "Laptop", "Mouse", "Teclado", "Monitor", "Audifonos", "Impresora",
            "Silla Gaming", "Pad Mouse" };

    private static final Random RANDOM = new Random();for(
    int i = 0;i<100;i++)
    {
        String firstName = FIRST_NAMES[RANDOM.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[RANDOM.nextInt(LAST_NAMES.length)];
        String documentType = DOCUMENT_TYPES[RANDOM.nextInt(DOCUMENT_TYPES.length)];
        String documentNumber = String.valueOf(RANDOM.nextInt(90000000) + 10000000);
        String productName = PRODUCT_NAMES[RANDOM.nextInt(PRODUCT_NAMES.length)];
        int quantity = RANDOM.nextInt(10) + 1;
        double price = RANDOM.nextDouble() * 1000;

        String line = firstName + "," + lastName + "," + documentType + "," + documentNumber + "," + productName
                + "," + quantity + "," + price;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("test_data.txt", true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
