package my.simple.keykeeper.export;

/**
 * Created by Alex on 07.10.2014.
 */
public class ExporterFactory {
    public static Exporter createExporter() {
        return new ExporterImpl();
    }
}
