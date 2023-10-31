import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Main {
    static List<Estudiante> estudiantes;

    public static void main(String[] args) throws IOException {
        cargarArchivo();
        mostrarEstudiantesPorCarrera();
        cantEstudiantesPorCarrera();
        cantMujeresPorCarrera();
        cantHombresPorCarrera();
        estudianteMayorPuntajePorCarrera();
        estudianteMayorPuntaje();
        promPuntajePorCarrera();
    }

    static void cargarArchivo() throws IOException {
        Pattern pattern = Pattern.compile(",");
        String filename = "student-scores.csv";

        try (Stream<String> lines = Files.lines(Path.of(filename))) {
            estudiantes = lines.skip(1).map(line -> {
                String[] arr = pattern.split(line);
                return new Estudiante(Integer.parseInt(arr[0]), arr[1], arr[2], arr[4], arr[9], Integer.parseInt(arr[10]));
            }).collect(Collectors.toList());
        }
    }
    static void mostrarEstudiantesPorCarrera(){
        System.out.printf("%nEstudiantes por carrera:%n");
        Map<String, List<Estudiante>> agrupadoPorCarrera =
                estudiantes.stream()
                        .collect(Collectors.groupingBy(Estudiante::getCarrera));
        agrupadoPorCarrera.forEach(
                (carrera, estudiantesEnCarrera) ->
                {
                    System.out.println(carrera);
                    estudiantesEnCarrera.forEach(
                            estudiante -> System.out.printf(" %s%n", estudiante));
                }
        );
    }
    static void cantEstudiantesPorCarrera(){
        // cuenta el número de estudiantes por carrera
        System.out.printf("%nConteo de estudiantes por carrera:%n");

        Map<String, Long> conteoEstudiantesPorCarrera =
                estudiantes.stream()
                        .collect(Collectors.groupingBy(Estudiante::getCarrera,
                                TreeMap::new, Collectors.counting()));
        conteoEstudiantesPorCarrera.forEach(
                (carrera, conteo) -> System.out.printf(
                        "%s tiene %d estudiante(s)%n", carrera, conteo));
    }

    static void cantMujeresPorCarrera(){
        // cuenta el número de mujeres por carrera
        System.out.printf("%nConteo de mujeres por carrera:%n");
        Map<String, Long> conteoEstudiantesPorCarrera =
                estudiantes.stream().filter(Estudiante -> Estudiante.getGenero().equals("female"))
                        .collect(Collectors.groupingBy(Estudiante::getCarrera,
                                TreeMap::new, Collectors.counting()));
        conteoEstudiantesPorCarrera.forEach(
                (carrera, conteo) -> System.out.printf(
                        "%s hay %d mujer(es)%n", carrera, conteo));
    }

    static void cantHombresPorCarrera(){
        // cuenta el número de estudiantes por carrera
        System.out.printf("%nConteo de hombres por carrera:%n");
        Map<String, Long> conteoEstudiantesPorCarrera =
                estudiantes.stream().filter(Estudiante -> Estudiante.getGenero().equals("male"))
                        .collect(Collectors.groupingBy(Estudiante::getCarrera,
                                TreeMap::new, Collectors.counting()));
        conteoEstudiantesPorCarrera.forEach(
                (carrera, conteo) -> System.out.printf(
                        "%s hay %d hombre(s)%n", carrera, conteo));
    }
    static void estudianteMayorPuntajePorCarrera(){
        Function<Estudiante, Integer> porPuntaje = Estudiante::getPuntaje;
        Comparator<Estudiante> puntajeDescendente =
                Comparator.comparing(porPuntaje);
        System.out.printf("%nMayor puntaje: %n");
        Map<String, List<Estudiante>> agrupadoPorCarrera =
                estudiantes.stream()
                        .collect(Collectors.groupingBy(Estudiante::getCarrera));
        agrupadoPorCarrera.forEach(
                (carrera, estudiantesEnCarrera) ->
                {
                    System.out.print(carrera+": ");
                    Estudiante estudianteMas=estudiantesEnCarrera.stream().sorted(puntajeDescendente.reversed())
                            .findFirst()
                            .get();
                    System.out.println(estudianteMas.getNombre()+" "+estudianteMas.getApellido()+
                            " Puntaje: "+estudianteMas.getPuntaje());
                }
        );
    }
    static void estudianteMayorPuntaje(){
        Function<Estudiante, Integer> porPuntaje = Estudiante::getPuntaje;
        Comparator<Estudiante> puntajeDescendente =
                Comparator.comparing(porPuntaje);
        Estudiante estudianteMas=estudiantes.stream()
                .sorted(puntajeDescendente.reversed())
                .findFirst()
                .get();
        System.out.printf(
                "%nEstudiante con el mayor puntaje: %s %s %s %s%n",
                estudianteMas
                        .getNombre(),
                estudianteMas
                        .getApellido()," Puntaje: ",
                estudianteMas
                        .getPuntaje());
    }
    static void promPuntajePorCarrera(){
        Map<String, List<Estudiante>> agrupadoPorCarrera =
                estudiantes.stream()
                        .collect(Collectors.groupingBy(Estudiante::getCarrera));
        System.out.println("\nPromedio de puntajes de los estudiantes por carrera:");
        agrupadoPorCarrera.forEach((carrera, estudiantesPorCarrera)-> {
            System.out.print(carrera+": ");
            System.out.println(estudiantesPorCarrera
                    .stream()
                    .mapToDouble(Estudiante::getPuntaje)
                    .average()
                    .getAsDouble());
        });


    }
}