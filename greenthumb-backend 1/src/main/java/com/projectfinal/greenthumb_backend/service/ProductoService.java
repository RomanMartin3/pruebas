package com.projectfinal.greenthumb_backend.service;

import com.projectfinal.greenthumb_backend.dto.*;
import com.projectfinal.greenthumb_backend.entities.*;
import com.projectfinal.greenthumb_backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final RegistroBajaRepository registroBajaRepository;
    private final AdministradorRepository administradorRepository;
    private final CategoriaRepository categoriaRepository;
    private final TipoProductoRepository tipoProductoRepository;
    private final PrecioProductoActualRepository precioProductoActualRepository;
    private final CostoProductoActualRepository costoProductoActualRepository;
    private final ImagenesProductoRepository imagenesProductoRepository;
    private final TiposMovimientoStockRepository tiposMovimientoStockRepository;
    private final MovimientosStockRepository movimientosStockRepository;
    private final PrecioProductoHistorialRepository precioProductoHistorialRepository;
    private final CostoProductoHistorialRepository costoProductoHistorialRepository;

    // Repositorios para detalles específicos
    private final DetallesPlantaRepository detallesPlantaRepository;
    private final DetallesHerramientaRepository detallesHerramientaRepository;
    private final DetallesSemillaRepository detallesSemillaRepository;
    private final NivelesLuzRepository nivelesLuzRepository;
    private final FrecuenciasRiegoRepository frecuenciasRiegoRepository;


    @Autowired
    public ProductoService(ProductoRepository productoRepository,
                           RegistroBajaRepository registroBajaRepository,
                           AdministradorRepository administradorRepository,
                           CategoriaRepository categoriaRepository,
                           TipoProductoRepository tipoProductoRepository,
                           PrecioProductoActualRepository precioProductoActualRepository,
                           CostoProductoActualRepository costoProductoActualRepository,
                           ImagenesProductoRepository imagenesProductoRepository,
                           TiposMovimientoStockRepository tiposMovimientoStockRepository,
                           MovimientosStockRepository movimientosStockRepository,
                           DetallesPlantaRepository detallesPlantaRepository,
                           DetallesHerramientaRepository detallesHerramientaRepository,
                           DetallesSemillaRepository detallesSemillaRepository,
                           NivelesLuzRepository nivelesLuzRepository,
                           FrecuenciasRiegoRepository frecuenciasRiegoRepository,
                           PrecioProductoHistorialRepository precioProductoHistorialRepository,
                           CostoProductoHistorialRepository costoProductoHistorialRepository) {
        this.productoRepository = productoRepository;
        this.registroBajaRepository = registroBajaRepository;
        this.administradorRepository = administradorRepository;
        this.categoriaRepository = categoriaRepository;
        this.tipoProductoRepository = tipoProductoRepository;
        this.precioProductoActualRepository = precioProductoActualRepository;
        this.costoProductoActualRepository = costoProductoActualRepository;
        this.imagenesProductoRepository = imagenesProductoRepository;
        this.tiposMovimientoStockRepository = tiposMovimientoStockRepository;
        this.movimientosStockRepository = movimientosStockRepository;
        this.detallesPlantaRepository = detallesPlantaRepository;
        this.detallesHerramientaRepository = detallesHerramientaRepository;
        this.detallesSemillaRepository = detallesSemillaRepository;
        this.precioProductoHistorialRepository = precioProductoHistorialRepository;
        this.costoProductoHistorialRepository = costoProductoHistorialRepository;
        this.nivelesLuzRepository = nivelesLuzRepository;
        this.frecuenciasRiegoRepository = frecuenciasRiegoRepository;
    }

    // --- Helper para convertir a ProductoListadoDTO ---
    private ProductoListadoDTO convertToListadoDTO(Producto producto) {
        String categoriaNombre = producto.getCategoria() != null ? producto.getCategoria().getNombreCategoria() : "N/A";
        String tipoProductoNombre = producto.getTipoProducto() != null ? producto.getTipoProducto().getNombreTipoProducto() : "N/A";

        BigDecimal precioVenta = BigDecimal.ZERO;
        // Intentar obtener el precio desde la relación directa primero
        if (producto.getPrecioActual() != null) {
            precioVenta = producto.getPrecioActual().getPrecioVenta();
        } else { // Si es null, intentar buscarlo en el repositorio (podría ser por carga LAZY o si no se seteó bien)
            Optional<PrecioProductoActual> ppaOpt = precioProductoActualRepository.findById(producto.getProductoId());
            if (ppaOpt.isPresent()) {
                precioVenta = ppaOpt.get().getPrecioVenta();
            }
        }

        String imagenUrl = imagenesProductoRepository.findByProducto(producto)
                .stream()
                .findFirst()
                .map(ImagenesProducto::getUrlImagen)
                .orElse(null); // O una URL placeholder

        return new ProductoListadoDTO(
                producto.getProductoId(),
                producto.getNombreProducto(),
                categoriaNombre,
                tipoProductoNombre,
                precioVenta,
                imagenUrl,
                producto.getStockActual()
        );
    }

    // --- Helper para convertir a ProductoDetalleDTO ---
    @Transactional(readOnly = true)
    public ProductoDetalleDTO convertToDetailDTO(Producto producto) {
        // Asumiendo que CategoriaDTO tiene un constructor (id, nombre, descripcion)
        CategoriaDTO categoriaDTO = producto.getCategoria() != null ?
                new CategoriaDTO(
                        producto.getCategoria().getCategoriaId(),
                        producto.getCategoria().getNombreCategoria(),
                        producto.getCategoria().getDescripcionCategoria()
                ) : null;

        TipoProductoDTO tipoProductoDTO = producto.getTipoProducto() != null ?
                new TipoProductoDTO(
                        producto.getTipoProducto().getTipoProductoId(),
                        producto.getTipoProducto().getNombreTipoProducto()
                ) : null;

        PrecioProductoActualDTO precioActualDTO = null;
        if (producto.getPrecioActual() != null) {
            precioActualDTO = new PrecioProductoActualDTO(producto.getPrecioActual().getPrecioVenta(), producto.getPrecioActual().getFechaInicioVigencia());
        } else {
            Optional<PrecioProductoActual> ppaOpt = precioProductoActualRepository.findById(producto.getProductoId());
            if(ppaOpt.isPresent()){
                precioActualDTO = new PrecioProductoActualDTO(ppaOpt.get().getPrecioVenta(), ppaOpt.get().getFechaInicioVigencia());
            }
        }

        List<ImagenProductoDTO> imagenesDTO = imagenesProductoRepository.findByProducto(producto).stream()
                .map(img -> new ImagenProductoDTO(img.getImagenId(), img.getUrlImagen(), img.getTextoAlternativo()))
                .collect(Collectors.toList());

        ProductoDetalleDTO dto = new ProductoDetalleDTO(
                producto.getProductoId(),
                producto.getNombreProducto(),
                producto.getDescripcionGeneral(),
                producto.getStockActual(),
                producto.getPuntoDeReorden(),
                categoriaDTO,
                tipoProductoDTO,
                producto.getFechaAlta(),
                producto.getFechaUltimaModificacion(),
                precioActualDTO,
                imagenesDTO
        );

        if (producto.getTipoProducto() != null && producto.getTipoProducto().getTablaDetalleAsociada() != null) {
            String tablaDetalle = producto.getTipoProducto().getTablaDetalleAsociada();

            if ("detallesplanta".equalsIgnoreCase(tablaDetalle)) {
                detallesPlantaRepository.findById(producto.getProductoId()).ifPresent(dp ->
                        dto.setDetallesPlanta(new DetallesPlantaDTO(
                                dp.getNombreCientifico(), dp.getTipoAmbiente(),
                                dp.getNivelLuz() != null ? dp.getNivelLuz().getDescripcionNivelLuz() : null,
                                dp.getFrecuenciaRiego() != null ? dp.getFrecuenciaRiego().getDescripcionFrecuenciaRiego() : null,
                                dp.isEsVenenosa(), dp.getCuidadosEspeciales()
                        ))
                );
            } else if ("detallesherramienta".equalsIgnoreCase(tablaDetalle)) {
                detallesHerramientaRepository.findById(producto.getProductoId()).ifPresent(dh ->
                        dto.setDetallesHerramienta(new DetallesHerramientaDTO(
                                dh.getMaterialPrincipal(), dh.getDimensiones(), dh.getPesoKG(),
                                dh.getUsoRecomendado(), dh.isRequiereMantenimiento()
                        ))
                );
            } else if ("detallessemilla".equalsIgnoreCase(tablaDetalle)) {
                detallesSemillaRepository.findById(producto.getProductoId()).ifPresent(ds ->
                        dto.setDetallesSemilla(new DetallesSemillaDTO(
                                ds.getEspecieVariedad(), ds.getEpocaSiembraIdeal(), ds.getProfundidadSiembraCM(),
                                ds.getTiempoGerminacionDias(), ds.getInstruccionesSiembra()
                        ))
                );
            }
        }
        return dto;
    }

    @Transactional(readOnly = true)
    public Page<ProductoListadoDTO> getAllActiveProductos(Pageable pageable, Integer categoriaId, String nombre) {
        List<Producto> todosLosProductos = productoRepository.findAll();

        List<ProductoListadoDTO> dtoList = todosLosProductos.stream()
                .filter(p -> registroBajaRepository.findByNombreTablaAndRegistroId("productos", p.getProductoId()).isEmpty())
                .filter(p -> categoriaId == null || (p.getCategoria() != null && p.getCategoria().getCategoriaId().equals(categoriaId)))
                .filter(p -> nombre == null || nombre.trim().isEmpty() || p.getNombreProducto().toLowerCase().contains(nombre.toLowerCase()))
                .map(this::convertToListadoDTO)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtoList.size());
        List<ProductoListadoDTO> pageContent = (start <= end && start < dtoList.size()) ? dtoList.subList(start, end) : Collections.emptyList();

        return new PageImpl<>(pageContent, pageable, dtoList.size());
    }

    @Transactional(readOnly = true)
    public Optional<ProductoDetalleDTO> getActiveProductoByIdDTO(Integer id) {
        Optional<Producto> productoOpt = productoRepository.findById(id);

        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            if (registroBajaRepository.findByNombreTablaAndRegistroId("productos", producto.getProductoId()).isPresent()) {
                return Optional.empty();
            }
            return Optional.of(convertToDetailDTO(producto)); // Corregido el nombre del método
        }
        return Optional.empty();
    }

    @Transactional // Muy importante para asegurar la atomicidad
    public ProductoDetalleDTO createProducto(ProductoCreacionRequestDTO requestDTO, Integer adminId) {

        Optional<Administrador> adminOpt = administradorRepository.findById(adminId);
        if (adminOpt.isEmpty()) {
            throw new RuntimeException("Administrador no encontrado con ID: " + adminId);
        }
        Administrador admin = adminOpt.get();

        if (requestDTO.getCategoriaId() == null || requestDTO.getTipoProductoId() == null) {
            throw new IllegalArgumentException("Los IDs de Categoría y Tipo de Producto son obligatorios.");
        }
        if (requestDTO.getNombreProducto() == null || requestDTO.getNombreProducto().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
        // Validación de nombre de producto único (entre productos activos)
        Optional<Producto> productoExistente = productoRepository.findByNombreProducto(requestDTO.getNombreProducto());
        if (productoExistente.isPresent() &&
                registroBajaRepository.findByNombreTablaAndRegistroId("productos", productoExistente.get().getProductoId()).isEmpty()) {
            throw new IllegalArgumentException("Ya existe un producto activo con el nombre: " + requestDTO.getNombreProducto());
        }


        Categoria categoria = categoriaRepository.findById(requestDTO.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + requestDTO.getCategoriaId()));
        TipoProducto tipoProducto = tipoProductoRepository.findById(requestDTO.getTipoProductoId())
                .orElseThrow(() -> new RuntimeException("Tipo de Producto no encontrado con ID: " + requestDTO.getTipoProductoId()));

        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombreProducto(requestDTO.getNombreProducto());
        nuevoProducto.setDescripcionGeneral(requestDTO.getDescripcionGeneral() != null ? requestDTO.getDescripcionGeneral() : "");
        nuevoProducto.setStockActual(requestDTO.getStockActual() != null ? requestDTO.getStockActual() : 0);
        nuevoProducto.setPuntoDeReorden(requestDTO.getPuntoDeReorden() != null ? requestDTO.getPuntoDeReorden() : 0);
        nuevoProducto.setCategoria(categoria);
        nuevoProducto.setTipoProducto(tipoProducto);
        // fechaAlta y fechaUltimaModificacion se manejan por @PrePersist/@PreUpdate en la entidad Producto

        Producto productoGuardado = productoRepository.save(nuevoProducto);

        // Crear Precio y Costo Actual
        if (requestDTO.getPrecioVenta() == null || requestDTO.getCosto() == null) {
            throw new IllegalArgumentException("El precio de venta y el costo son obligatorios.");
        }
        PrecioProductoActual ppa = new PrecioProductoActual(productoGuardado, requestDTO.getPrecioVenta(), admin);
        precioProductoActualRepository.save(ppa);
        // La relación bidireccional se establece en el constructor de PrecioProductoActual o se puede setear aquí si es necesario
        // y si Producto es el dueño de la relación, pero con @MapsId en PrecioProductoActual, ya está vinculado.

        CostoProductoActual cpa = new CostoProductoActual(productoGuardado, requestDTO.getCosto(), admin);
        costoProductoActualRepository.save(cpa);
        // Igual para CostoProductoActual

        // Guardar Detalles Específicos
        // La entidad TipoProducto tiene el campo tablaDetalleAsociada que nos dice qué tipo de detalle es
        String tablaDetalleAsociada = tipoProducto.getTablaDetalleAsociada();

        if ("detallesplanta".equalsIgnoreCase(tablaDetalleAsociada)) {
            if (requestDTO.getDetallesPlanta() == null) {
                throw new IllegalArgumentException("Detalles de planta son requeridos para este tipo de producto.");
            }
            DetallesPlantaDTO dpDTO = requestDTO.getDetallesPlanta();
            // Necesitarás NivelesLuz y FrecuenciasRiego entidades, no solo descripciones.
            // Asumiendo que el DTO de detalle para creación trae IDs o descripciones que puedes buscar.
            // Por simplicidad, si el DTO trae las descripciones, necesitarías buscar las entidades:
            NivelesLuz nivelLuz = nivelesLuzRepository.findByDescripcionNivelLuz(dpDTO.getNivelLuzDescripcion())
                    .orElseThrow(() -> new RuntimeException("Nivel de luz no encontrado: " + dpDTO.getNivelLuzDescripcion()));
            FrecuenciasRiego frecuenciaRiego = frecuenciasRiegoRepository.findByDescripcionFrecuenciaRiego(dpDTO.getFrecuenciaRiegoDescripcion())
                    .orElseThrow(() -> new RuntimeException("Frecuencia de riego no encontrada: " + dpDTO.getFrecuenciaRiegoDescripcion()));

            DetallesPlanta dp = new DetallesPlanta(productoGuardado, dpDTO.getNombreCientifico(), dpDTO.getTipoAmbiente(),
                    nivelLuz, frecuenciaRiego, dpDTO.isEsVenenosa(), dpDTO.getCuidadosEspeciales());
            detallesPlantaRepository.save(dp);
        } else if ("detallesherramienta".equalsIgnoreCase(tablaDetalleAsociada)) {
            if (requestDTO.getDetallesHerramienta() == null) {
                throw new IllegalArgumentException("Detalles de herramienta son requeridos para este tipo de producto.");
            }
            DetallesHerramientaDTO dhDTO = requestDTO.getDetallesHerramienta();
            DetallesHerramienta dh = new DetallesHerramienta(productoGuardado, dhDTO.getMaterialPrincipal(), dhDTO.getDimensiones(),
                    dhDTO.getPesoKG(), dhDTO.getUsoRecomendado(), dhDTO.isRequiereMantenimiento());
            detallesHerramientaRepository.save(dh);
        } else if ("detallessemilla".equalsIgnoreCase(tablaDetalleAsociada)) {
            if (requestDTO.getDetallesSemilla() == null) {
                throw new IllegalArgumentException("Detalles de semilla son requeridos para este tipo de producto.");
            }
            DetallesSemillaDTO dsDTO = requestDTO.getDetallesSemilla();
            DetallesSemilla ds = new DetallesSemilla(productoGuardado, dsDTO.getEspecieVariedad(), dsDTO.getEpocaSiembraIdeal(),
                    dsDTO.getProfundidadSiembraCM(), dsDTO.getTiempoGerminacionDias(), dsDTO.getInstruccionesSiembra());
            detallesSemillaRepository.save(ds);
        }
        // ... (manejar otros tipos de producto si existen)

        // Crear movimiento de stock inicial
        TiposMovimientoStock ingresoInicial = tiposMovimientoStockRepository.findByDescripcionTipoMovimiento("Ingreso Inicial")
                .orElseThrow(() -> new RuntimeException("Tipo de movimiento 'Ingreso Inicial' no encontrado."));

        MovimientosStock movimiento = new MovimientosStock(
                productoGuardado, ingresoInicial, productoGuardado.getStockActual(),
                0, productoGuardado.getStockActual(), admin, "Stock inicial por creación de producto.");
        movimientosStockRepository.save(movimiento);

        // Convertir la entidad Producto completamente ensamblada (con sus detalles) a ProductoDetalleDTO
        return convertToDetailDTO(productoGuardado);
    }

    @Transactional
    public Optional<ProductoDetalleDTO> updateProductoDTO(Integer id, ProductoActualizacionRequestDTO requestDTO, Integer adminId) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isEmpty() || registroBajaRepository.findByNombreTablaAndRegistroId("productos", id).isPresent()) {
            return Optional.empty(); // No existe o está dado de baja
        }

        Administrador admin = administradorRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado con ID: " + adminId));

        Producto productoExistente = productoOpt.get();

        // Actualizar campos básicos si se proporcionan en el DTO
        if (requestDTO.getNombreProducto() != null) {
            // Validar nombre único si cambia
            if (!requestDTO.getNombreProducto().equalsIgnoreCase(productoExistente.getNombreProducto())) {
                Optional<Producto> otroConMismoNombre = productoRepository.findByNombreProducto(requestDTO.getNombreProducto());
                if (otroConMismoNombre.isPresent() && !otroConMismoNombre.get().getProductoId().equals(id) &&
                        registroBajaRepository.findByNombreTablaAndRegistroId("productos", otroConMismoNombre.get().getProductoId()).isEmpty()) {
                    throw new IllegalArgumentException("Ya existe otro producto activo con el nombre: " + requestDTO.getNombreProducto());
                }
            }
            productoExistente.setNombreProducto(requestDTO.getNombreProducto());
        }
        if (requestDTO.getDescripcionGeneral() != null) {
            productoExistente.setDescripcionGeneral(requestDTO.getDescripcionGeneral());
        }
        if (requestDTO.getStockActual() != null) {
            // Aquí deberías considerar si esta es la forma correcta de actualizar stock
            // o si debe ser a través de un MovimientoStock de ajuste.
            // Por ahora, lo permitimos directamente si se envía.
            // Considerar crear un movimiento de ajuste si el stock cambia aquí.
            if(!productoExistente.getStockActual().equals(requestDTO.getStockActual())) {
                TiposMovimientoStock tipoAjuste = requestDTO.getStockActual() > productoExistente.getStockActual() ?
                        tiposMovimientoStockRepository.findByDescripcionTipoMovimiento("Ajuste Positivo").orElse(null) :
                        tiposMovimientoStockRepository.findByDescripcionTipoMovimiento("Ajuste Negativo").orElse(null);

                if (tipoAjuste != null) {
                    int cantidadAfectada = requestDTO.getStockActual() - productoExistente.getStockActual();
                    MovimientosStock movimiento = new MovimientosStock(
                            productoExistente, tipoAjuste, cantidadAfectada,
                            productoExistente.getStockActual(), requestDTO.getStockActual(), admin, "Ajuste de stock por actualización de producto.");
                    movimientosStockRepository.save(movimiento);
                }
                productoExistente.setStockActual(requestDTO.getStockActual());
            }
        }
        if (requestDTO.getPuntoDeReorden() != null) {
            productoExistente.setPuntoDeReorden(requestDTO.getPuntoDeReorden());
        }

        // Actualizar Categoría si se proporciona
        if (requestDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(requestDTO.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + requestDTO.getCategoriaId()));
            productoExistente.setCategoria(categoria);
        }

        // Actualizar Tipo de Producto si se proporciona
        // ¡CUIDADO! Si se cambia el tipo de producto, la lógica para los detalles específicos se complica.
        // Habría que eliminar el detalle específico anterior y crear el nuevo.
        // Por ahora, asumimos que si se cambia el tipo, los detalles específicos también se envían completos para el nuevo tipo.
        if (requestDTO.getTipoProductoId() != null &&
                (productoExistente.getTipoProducto() == null || !productoExistente.getTipoProducto().getTipoProductoId().equals(requestDTO.getTipoProductoId()))) {

            TipoProducto nuevoTipo = tipoProductoRepository.findById(requestDTO.getTipoProductoId())
                    .orElseThrow(() -> new RuntimeException("Tipo de Producto no encontrado con ID: " + requestDTO.getTipoProductoId()));

            // Lógica para eliminar detalles antiguos (si existían y el tipo cambió)
            // Esto es simplificado. En un caso real, verificarías la tablaDetalleAsociada anterior.
            detallesPlantaRepository.deleteById(productoExistente.getProductoId()); // Intenta borrar, no falla si no existe
            detallesHerramientaRepository.deleteById(productoExistente.getProductoId());
            detallesSemillaRepository.deleteById(productoExistente.getProductoId());

            productoExistente.setTipoProducto(nuevoTipo);
            // Los nuevos detalles se guardarán más abajo.
        }

        // Actualizar Precio (si se proporciona)
        if (requestDTO.getNuevoPrecioVenta() != null) {
            PrecioProductoActual ppa = productoExistente.getPrecioActual();
            if (ppa == null) {
                ppa = precioProductoActualRepository.findById(productoExistente.getProductoId()).orElse(new PrecioProductoActual(productoExistente, requestDTO.getNuevoPrecioVenta(), admin));
                ppa.setPrecioVenta(requestDTO.getNuevoPrecioVenta()); // Asegurar el precio del DTO
                ppa.setAdministrador(admin); // Asegurar el admin
                ppa.setFechaInicioVigencia(LocalDateTime.now()); // Asegurar fecha de inicio
            } else {
                if (!ppa.getPrecioVenta().equals(requestDTO.getNuevoPrecioVenta())) {
                    PrecioProductoHistorial pph = new PrecioProductoHistorial(productoExistente, ppa.getPrecioVenta(), ppa.getFechaInicioVigencia(), LocalDateTime.now(), admin);
                    precioProductoHistorialRepository.save(pph);
                    ppa.setPrecioVenta(requestDTO.getNuevoPrecioVenta());
                    ppa.setFechaInicioVigencia(LocalDateTime.now());
                    ppa.setAdministrador(admin);
                }
            }
            precioProductoActualRepository.save(ppa);
            productoExistente.setPrecioActual(ppa); // Asegurar asociación
        }

        // Actualizar Costo (si se proporciona)
        if (requestDTO.getNuevoCosto() != null) {
            CostoProductoActual cpa = productoExistente.getCostoActual();
            if (cpa == null) {
                cpa = costoProductoActualRepository.findById(productoExistente.getProductoId()).orElse(new CostoProductoActual(productoExistente, requestDTO.getNuevoCosto(), admin));
                cpa.setPrecioCosto(requestDTO.getNuevoCosto());
                cpa.setAdministrador(admin);
                cpa.setFechaInicioVigencia(LocalDateTime.now());
            } else {
                if (!cpa.getPrecioCosto().equals(requestDTO.getNuevoCosto())) {
                    CostoProductoHistorial cph = new CostoProductoHistorial(productoExistente, cpa.getPrecioCosto(), cpa.getFechaInicioVigencia(), LocalDateTime.now(), admin);
                    costoProductoHistorialRepository.save(cph);
                    cpa.setPrecioCosto(requestDTO.getNuevoCosto());
                    cpa.setFechaInicioVigencia(LocalDateTime.now());
                    cpa.setAdministrador(admin);
                }
            }
            costoProductoActualRepository.save(cpa);
            productoExistente.setCostoActual(cpa); // Asegurar asociación
        }

        // Actualizar/Crear Detalles Específicos si se proporcionan en el DTO
        // Asumimos que si se cambia el tipo de producto, el DTO de detalle correcto se envía.
        String tablaDetalleAsociadaActual = productoExistente.getTipoProducto().getTablaDetalleAsociada();

        if ("detallesplanta".equalsIgnoreCase(tablaDetalleAsociadaActual) && requestDTO.getDetallesPlanta() != null) {
            DetallesPlantaDTO dpDTO = requestDTO.getDetallesPlanta();
            NivelesLuz nivelLuz = nivelesLuzRepository.findByDescripcionNivelLuz(dpDTO.getNivelLuzDescripcion())
                    .orElseThrow(() -> new RuntimeException("Nivel de luz no encontrado: " + dpDTO.getNivelLuzDescripcion()));
            FrecuenciasRiego frecuenciaRiego = frecuenciasRiegoRepository.findByDescripcionFrecuenciaRiego(dpDTO.getFrecuenciaRiegoDescripcion())
                    .orElseThrow(() -> new RuntimeException("Frecuencia de riego no encontrada: " + dpDTO.getFrecuenciaRiegoDescripcion()));

            DetallesPlanta dp = detallesPlantaRepository.findById(productoExistente.getProductoId()).orElse(new DetallesPlanta());
            dp.setProducto(productoExistente); // Asegurar asociación
            dp.setNombreCientifico(dpDTO.getNombreCientifico());
            dp.setTipoAmbiente(dpDTO.getTipoAmbiente());
            dp.setNivelLuz(nivelLuz);
            dp.setFrecuenciaRiego(frecuenciaRiego);
            dp.setEsVenenosa(dpDTO.isEsVenenosa());
            dp.setCuidadosEspeciales(dpDTO.getCuidadosEspeciales());
            detallesPlantaRepository.save(dp);
        } else if ("detallesherramienta".equalsIgnoreCase(tablaDetalleAsociadaActual) && requestDTO.getDetallesHerramienta() != null) {
            DetallesHerramientaDTO dhDTO = requestDTO.getDetallesHerramienta();
            DetallesHerramienta dh = detallesHerramientaRepository.findById(productoExistente.getProductoId()).orElse(new DetallesHerramienta());
            dh.setProducto(productoExistente);
            dh.setMaterialPrincipal(dhDTO.getMaterialPrincipal());
            dh.setDimensiones(dhDTO.getDimensiones());
            dh.setPesoKG(dhDTO.getPesoKG());
            dh.setUsoRecomendado(dhDTO.getUsoRecomendado());
            dh.setRequiereMantenimiento(dhDTO.isRequiereMantenimiento());
            detallesHerramientaRepository.save(dh);
        } else if ("detallessemilla".equalsIgnoreCase(tablaDetalleAsociadaActual) && requestDTO.getDetallesSemilla() != null) {
            DetallesSemillaDTO dsDTO = requestDTO.getDetallesSemilla();
            DetallesSemilla ds = detallesSemillaRepository.findById(productoExistente.getProductoId()).orElse(new DetallesSemilla());
            ds.setProducto(productoExistente);
            ds.setEspecieVariedad(dsDTO.getEspecieVariedad());
            ds.setEpocaSiembraIdeal(dsDTO.getEpocaSiembraIdeal());
            ds.setProfundidadSiembraCM(dsDTO.getProfundidadSiembraCM());
            ds.setTiempoGerminacionDias(dsDTO.getTiempoGerminacionDias());
            ds.setInstruccionesSiembra(dsDTO.getInstruccionesSiembra());
            detallesSemillaRepository.save(ds);
        }

        Producto productoActualizado = productoRepository.save(productoExistente); // Guardar cambios en Producto
        return Optional.of(convertToDetailDTO(productoActualizado));
    }


    @Transactional
    public boolean softDeleteProducto(Integer id, String motivoBaja, Integer adminId) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            if (registroBajaRepository.findByNombreTablaAndRegistroId("productos", id).isPresent()) {
                return true; // Ya está de baja
            }
            Administrador admin = administradorRepository.findById(adminId)
                    .orElseThrow(() -> new RuntimeException("Administrador no encontrado."));

            RegistroBaja registro = new RegistroBaja("productos", id, motivoBaja, admin);
            registroBajaRepository.save(registro);
            return true;
        }
        return false;
    }


}