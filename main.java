import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main class that processes the input files and generates the sales reports
 * by salesman and by product.
 * 
 * @author Code Raiders
 * @version 1.0
 */
public class main {

    /** Products map: productId -> {name, price} */
    private static Map<String, String[]> products = new HashMap<>();

    /** Salesmen map: documentType;documentNumber -> fullName */
    private static Map<String, String> salesmen = new HashMap<>();

    /**
     * Loads the products file into memory.
     * Format: IDProduct;ProductName;PricePerUnit
     * 
     * @param path Path of the products file
     */
    public static void loadProducts(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(";");
                if (parts.length >= 3) {
                    products.put(parts[0].trim(), new String[] { parts[1].trim(), parts[2].trim() });
                }
            }
            System.out.println("✓ Productos cargados: " + products.size());
        } catch (IOException e) {
            System.err.println("✗ Error al leer el archivo de productos: " + e.getMessage());
        }
    }

    /**
     * Loads the salesmen info file into memory.
     * Format: DocumentType;DocumentNumber;FirstNames;LastNames
     * 
     * @param path Path of the salesmen file
     */
    public static void loadSalesmen(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(";");
                if (parts.length >= 4) {
                    String salesManId = parts[0].trim() + ";" + parts[1].trim();
                    String fullName = parts[2].trim() + " " + parts[3].trim();
                    salesmen.put(salesManId, fullName);
                }
            }
            System.out.println("✓ Vendedores cargados: " + salesmen.size());
        } catch (IOException e) {
            System.err.println("✗ Error al leer el archivo de vendedores: " + e.getMessage());
        }
    }

    /**
     * Processes all sales files in the current folder.
     * Returns a map: salesmanId -> total revenue
     * and updates a map: productId -> total sold quantity
     * 
     * @param quantitiesByProduct Map where quantities per product are accumulated
     * @return Map of salesmanId -> total revenue
     */
    public static Map<String, Double> processSales(Map<String, Integer> quantitiesByProduct) {
        Map<String, Double> totalBySalesman = new HashMap<>();
        File folder = new File(".");
        File[] files = folder.listFiles((dir, name) -> name.startsWith("ventas_") && name.endsWith(".txt"));

        if (files == null) {
            System.err.println("✗ Error: no se encontró la carpeta de ventas.");
            return totalBySalesman;
        }

        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String firstLine = reader.readLine();
                if (firstLine == null) {
                    continue;
                }

                String[] salesManParts = firstLine.split(";");
                if (salesManParts.length < 2) {
                    continue;
                }
                String salesManId = salesManParts[0].trim() + ";" + salesManParts[1].trim();

                double total = 0.0;
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    String[] parts = line.split(";");
                    if (parts.length < 2) {
                        continue;
                    }

                    String productId = parts[0].trim();
                    int quantity;
                    try {
                        quantity = Integer.parseInt(parts[1].trim());
                    } catch (NumberFormatException e) {
                        System.err.println("✗ Cantidad inválida en " + file.getName());
                        continue;
                    }

                    if (quantity < 0) {
                        System.err.println("✗ Cantidad negativa en " + file.getName());
                        continue;
                    }

                    String[] productInfo = products.get(productId);
                    if (productInfo == null) {
                        System.err.println("✗ Producto inexistente: " + productId);
                        continue;
                    }

                    double price = Double.parseDouble(productInfo[1]);
                    total += quantity * price;

                    quantitiesByProduct.merge(productId, quantity, Integer::sum);
                }

                totalBySalesman.merge(salesManId, total, Double::sum);

            } catch (IOException e) {
                System.err.println("✗ Error al leer " + file.getName() + ": " + e.getMessage());
            }
        }
        return totalBySalesman;
    }

    /**
     * Generates the salesman sales report file, sorted from highest to lowest
     * by revenue.
     * 
     * @param totalBySalesman Map of salesmanId -> total revenue
     */
    public static void generateSalesmenReport(Map<String, Double> totalBySalesman) {
        List<Map.Entry<String, Double>> list = new ArrayList<>(totalBySalesman.entrySet());
        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("reporte_vendedores.csv"))) {
            for (Map.Entry<String, Double> entry : list) {
                String name = salesmen.getOrDefault(entry.getKey(), entry.getKey());
                writer.write(name + ";" + entry.getValue());
                writer.newLine();
            }
            System.out.println("✓ Reporte de vendedores generado: reporte_vendedores.csv");
        } catch (IOException e) {
            System.err.println("✗ Error al escribir el reporte de vendedores: " + e.getMessage());
        }
    }

    /**
     * Generates the products report file with sold products sorted by
     * quantity (descending). Includes name and price.
     * 
     * @param quantitiesByProduct Map of productId -> total sold quantity
     */
    public static void generateProductsReport(Map<String, Integer> quantitiesByProduct) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(quantitiesByProduct.entrySet());
        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("reporte_productos.csv"))) {
            for (Map.Entry<String, Integer> entry : list) {
                String[] info = products.get(entry.getKey());
                if (info == null) {
                    continue;
                }
                writer.write(info[0] + ";" + info[1]);
                writer.newLine();
            }
            System.out.println("✓ Reporte de productos generado: reporte_productos.csv");
        } catch (IOException e) {
            System.err.println("✗ Error al escribir el reporte de productos: " + e.getMessage());
        }
    }

    /**
     * Main method of the program.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        loadProducts("productos.csv");
        loadSalesmen("vendedores.csv");

        Map<String, Integer> quantitiesByProduct = new HashMap<>();
        Map<String, Double> totalBySalesman = processSales(quantitiesByProduct);

        generateSalesmenReport(totalBySalesman);
        generateProductsReport(quantitiesByProduct);

        System.out.println("\nProceso finalizado exitosamente.");
    }
}