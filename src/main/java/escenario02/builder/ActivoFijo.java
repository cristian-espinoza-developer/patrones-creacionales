package escenario02.builder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Product: activo fijo institucional. Es inmutable y solo puede crearse mediante
 * su Builder, que exige los datos obligatorios y permite agregar únicamente
 * los datos opcionales que correspondan a cada activo.
 */
public final class ActivoFijo {

    // Obligatorios
    private final String codigo;
    private final String nombre;
    private final double precioAdquisicion;

    // Opcionales
    private final String marca;
    private final String modelo;
    private final String numeroSerie;
    private final String ubicacion;
    private final String custodio;
    private final LocalDate fechaAdquisicion;
    private final Integer garantiaMeses;
    private final String proveedor;
    private final Map<String, String> caracteristicas;
    private final String observaciones;

    private ActivoFijo(Builder builder) {
        this.codigo = builder.codigo;
        this.nombre = builder.nombre;
        this.precioAdquisicion = builder.precioAdquisicion;
        this.marca = builder.marca;
        this.modelo = builder.modelo;
        this.numeroSerie = builder.numeroSerie;
        this.ubicacion = builder.ubicacion;
        this.custodio = builder.custodio;
        this.fechaAdquisicion = builder.fechaAdquisicion;
        this.garantiaMeses = builder.garantiaMeses;
        this.proveedor = builder.proveedor;
        this.caracteristicas = Collections.unmodifiableMap(new LinkedHashMap<>(builder.caracteristicas));
        this.observaciones = builder.observaciones;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioAdquisicion() {
        return precioAdquisicion;
    }

    public Optional<String> getMarca() {
        return Optional.ofNullable(marca);
    }

    public Optional<String> getModelo() {
        return Optional.ofNullable(modelo);
    }

    public Optional<String> getNumeroSerie() {
        return Optional.ofNullable(numeroSerie);
    }

    public Optional<String> getUbicacion() {
        return Optional.ofNullable(ubicacion);
    }

    public Optional<String> getCustodio() {
        return Optional.ofNullable(custodio);
    }

    public Optional<LocalDate> getFechaAdquisicion() {
        return Optional.ofNullable(fechaAdquisicion);
    }

    public Optional<Integer> getGarantiaMeses() {
        return Optional.ofNullable(garantiaMeses);
    }

    public Optional<String> getProveedor() {
        return Optional.ofNullable(proveedor);
    }

    public Map<String, String> getCaracteristicas() {
        return caracteristicas;
    }

    public Optional<String> getObservaciones() {
        return Optional.ofNullable(observaciones);
    }

    /** Fecha de fin de garantía, disponible solo si se registraron la fecha de adquisición y la garantía. */
    public Optional<LocalDate> getFinGarantia() {
        if (fechaAdquisicion == null || garantiaMeses == null) {
            return Optional.empty();
        }
        return Optional.of(fechaAdquisicion.plusMonths(garantiaMeses));
    }

    /** Ficha del activo con únicamente la información registrada. */
    public String describir() {
        List<String> lineas = new ArrayList<>();
        lineas.add(String.format("Activo %s — %s", codigo, nombre));
        lineas.add(String.format("  Precio de adquisición: $%,.2f", precioAdquisicion));
        getMarca().ifPresent(v -> lineas.add("  Marca: " + v));
        getModelo().ifPresent(v -> lineas.add("  Modelo: " + v));
        getNumeroSerie().ifPresent(v -> lineas.add("  Número de serie: " + v));
        getUbicacion().ifPresent(v -> lineas.add("  Ubicación: " + v));
        getCustodio().ifPresent(v -> lineas.add("  Custodio: " + v));
        getFechaAdquisicion().ifPresent(v -> lineas.add("  Fecha de adquisición: " + v));
        getGarantiaMeses().ifPresent(v -> lineas.add("  Garantía: " + v + " meses"));
        getFinGarantia().ifPresent(v -> lineas.add("  Fin de garantía: " + v));
        getProveedor().ifPresent(v -> lineas.add("  Proveedor: " + v));
        caracteristicas.forEach((k, v) -> lineas.add("  " + k + ": " + v));
        getObservaciones().ifPresent(v -> lineas.add("  Observaciones: " + v));
        return String.join(System.lineSeparator(), lineas);
    }

    @Override
    public String toString() {
        return String.format("%s — %s ($%,.2f)", codigo, nombre, precioAdquisicion);
    }

    /**
     * Builder: recibe los datos obligatorios en su constructor, permite agregar
     * los opcionales mediante métodos encadenados y entrega el producto con build().
     */
    public static final class Builder {

        private final String codigo;
        private final String nombre;
        private final double precioAdquisicion;

        private String marca;
        private String modelo;
        private String numeroSerie;
        private String ubicacion;
        private String custodio;
        private LocalDate fechaAdquisicion;
        private Integer garantiaMeses;
        private String proveedor;
        private final Map<String, String> caracteristicas = new LinkedHashMap<>();
        private String observaciones;

        public Builder(String codigo, String nombre, double precioAdquisicion) {
            this.codigo = codigo == null ? null : codigo.trim();
            this.nombre = nombre == null ? null : nombre.trim();
            this.precioAdquisicion = precioAdquisicion;
        }

        public Builder marca(String marca) {
            this.marca = texto(marca, "La marca");
            return this;
        }

        public Builder modelo(String modelo) {
            this.modelo = texto(modelo, "El modelo");
            return this;
        }

        public Builder numeroSerie(String numeroSerie) {
            this.numeroSerie = texto(numeroSerie, "El número de serie");
            return this;
        }

        public Builder ubicacion(String ubicacion) {
            this.ubicacion = texto(ubicacion, "La ubicación");
            return this;
        }

        public Builder custodio(String custodio) {
            this.custodio = texto(custodio, "El custodio");
            return this;
        }

        public Builder fechaAdquisicion(LocalDate fechaAdquisicion) {
            if (fechaAdquisicion == null) {
                throw new IllegalArgumentException("La fecha de adquisición no puede ser nula");
            }
            this.fechaAdquisicion = fechaAdquisicion;
            return this;
        }

        public Builder garantiaMeses(int garantiaMeses) {
            this.garantiaMeses = garantiaMeses;
            return this;
        }

        public Builder proveedor(String proveedor) {
            this.proveedor = texto(proveedor, "El proveedor");
            return this;
        }

        public Builder caracteristica(String nombre, String valor) {
            caracteristicas.put(texto(nombre, "El nombre de la característica"),
                    texto(valor, "El valor de la característica"));
            return this;
        }

        public Builder observaciones(String observaciones) {
            this.observaciones = texto(observaciones, "Las observaciones");
            return this;
        }

        /** Valida la información incorporada y entrega el activo configurado. */
        public ActivoFijo build() {
            List<String> errores = new ArrayList<>();
            if (codigo == null || codigo.isEmpty()) {
                errores.add("El código institucional es obligatorio.");
            }
            if (nombre == null || nombre.isEmpty()) {
                errores.add("El nombre o descripción es obligatorio.");
            }
            if (!(precioAdquisicion > 0)) {
                errores.add("El precio de adquisición debe ser mayor que cero.");
            }
            if (fechaAdquisicion != null && fechaAdquisicion.isAfter(LocalDate.now())) {
                errores.add("La fecha de adquisición no puede ser futura.");
            }
            if (garantiaMeses != null && garantiaMeses < 0) {
                errores.add("La garantía no puede ser negativa.");
            }
            if (!errores.isEmpty()) {
                throw new IllegalStateException(String.join(System.lineSeparator(), errores));
            }
            return new ActivoFijo(this);
        }

        private static String texto(String valor, String campo) {
            if (valor == null || valor.isBlank()) {
                throw new IllegalArgumentException(campo + " no puede estar vacío.");
            }
            return valor.trim();
        }
    }
}
