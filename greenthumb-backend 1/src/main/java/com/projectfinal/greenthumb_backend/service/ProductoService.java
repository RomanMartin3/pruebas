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
import org.springframework.web.multipart.MultipartFile;

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
    private final FileStorageService fileStorageService;

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
                           CostoProductoHistorialRepository costoProductoHistorialRepository,
                           FileStorageService fileStorageService) {
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
        this.fileStorageService = fileStorageService;
    }

    private ProductoListadoDTO convertToListadoDTO(Producto producto) {
        String categoriaNombre = producto.getCategoria() != null ? producto.getCategoria().getNombreCategoria() : "N/A";
        String tipoProductoNombre = producto.getTipoProducto() != null ? producto.getTipoProducto().getNombreTipoProducto() : "N/A";

        BigDecimal precioVenta = BigDecimal.ZERO;
        if (producto.getPrecioActual() != null) {
            precioVenta = producto.getPrecioActual().getPrecioVenta();
        } else {
            Optional<PrecioProductoActual> ppaOpt = precioProductoActualRepository.findById(producto.getProductoId());
            if (ppaOpt.isPresent()) {
                precioVenta = ppaOpt.get().getPrecioVenta();
            }
        }

        String imagenUrl = imagenesProductoRepository.findByProducto(producto)
                .stream()
                .findFirst()
                .map(ImagenesProducto::getUrlImagen)
                .orElse(null);

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

    @Transactional(readOnly = true)
    public ProductoDetalleDTO convertToDetailDTO(Producto producto) {
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
            return Optional.of(convertToDetailDTO(producto));
        }
        return Optional.empty();
    }

    @Transactional
    public ProductoDetalleDTO createProducto(
            ProductoCreacionRequestDTO requestDTO,
            MultipartFile imagen,
            DetallesPlantaDTO detallesPlantaDTO,         // <-- NUEVO PARAMETRO
            DetallesHerramientaDTO detallesHerramientaDTO, // <-- NUEVO PARAMETRO
            DetallesSemillaDTO detallesSemillaDTO,       // <-- NUEVO PARAMETRO
            Integer adminId) {

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

        Producto productoGuardado = productoRepository.save(nuevoProducto);

        if (imagen != null && !imagen.isEmpty()) {
            try {
                String imageUrl = fileStorageService.store(imagen);
                ImagenesProducto imagenProducto = new ImagenesProducto(productoGuardado, imageUrl, productoGuardado.getNombreProducto() + " principal");
                imagenesProductoRepository.save(imagenProducto);
            } catch (Exception e) {
                System.err.println("Error al guardar la imagen para el producto " + productoGuardado.getNombreProducto() + ": " + e.getMessage());
            }
        }

        if (requestDTO.getPrecioVenta() == null || requestDTO.getCosto() == null) {
            throw new IllegalArgumentException("El precio de venta y el costo son obligatorios.");
        }
        PrecioProductoActual ppa = new PrecioProductoActual(productoGuardado, requestDTO.getPrecioVenta(), admin);
        precioProductoActualRepository.save(ppa);

        CostoProductoActual cpa = new CostoProductoActual(productoGuardado, requestDTO.getCosto(), admin);
        costoProductoActualRepository.save(cpa);

        String tablaDetalleAsociada = tipoProducto.getTablaDetalleAsociada();

        // --- MANEJO DE DETALLES ESPECIFICOS AHORA CON PARAMETROS DIRECTOS ---
        if ("detallesplanta".equalsIgnoreCase(tablaDetalleAsociada)) {
            if (detallesPlantaDTO == null) { // <-- Se verifica el nuevo parámetro
                throw new IllegalArgumentException("Detalles de planta son requeridos para este tipo de producto.");
            }
            NivelesLuz nivelLuz = nivelesLuzRepository.findByDescripcionNivelLuz(detallesPlantaDTO.getNivelLuzDescripcion())
                    .orElseThrow(() -> new RuntimeException("Nivel de luz no encontrado: " + detallesPlantaDTO.getNivelLuzDescripcion()));
            FrecuenciasRiego frecuenciaRiego = frecuenciasRiegoRepository.findByDescripcionFrecuenciaRiego(detallesPlantaDTO.getFrecuenciaRiegoDescripcion())
                    .orElseThrow(() -> new RuntimeException("Frecuencia de riego no encontrada: " + detallesPlantaDTO.getFrecuenciaRiegoDescripcion()));

            DetallesPlanta dp = new DetallesPlanta(productoGuardado, detallesPlantaDTO.getNombreCientifico(), detallesPlantaDTO.getTipoAmbiente(),
                    nivelLuz, frecuenciaRiego, detallesPlantaDTO.isEsVenenosa(), detallesPlantaDTO.getCuidadosEspeciales());
            detallesPlantaRepository.save(dp);
        } else if ("detallesherramienta".equalsIgnoreCase(tablaDetalleAsociada)) {
            if (detallesHerramientaDTO == null) { // <-- Se verifica el nuevo parámetro
                throw new IllegalArgumentException("Detalles de herramienta son requeridos para este tipo de producto.");
            }
            DetallesHerramienta dh = new DetallesHerramienta(productoGuardado, detallesHerramientaDTO.getMaterialPrincipal(), detallesHerramientaDTO.getDimensiones(),
                    detallesHerramientaDTO.getPesoKG(), detallesHerramientaDTO.getUsoRecomendado(), detallesHerramientaDTO.isRequiereMantenimiento());
            detallesHerramientaRepository.save(dh);
        } else if ("detallessemilla".equalsIgnoreCase(tablaDetalleAsociada)) {
            if (detallesSemillaDTO == null) { // <-- Se verifica el nuevo parámetro
                throw new IllegalArgumentException("Detalles de semilla son requeridos para este tipo de producto.");
            }
            DetallesSemilla ds = new DetallesSemilla(productoGuardado, detallesSemillaDTO.getEspecieVariedad(), detallesSemillaDTO.getEpocaSiembraIdeal(),
                    detallesSemillaDTO.getProfundidadSiembraCM(), detallesSemillaDTO.getTiempoGerminacionDias(), detallesSemillaDTO.getInstruccionesSiembra());
            detallesSemillaRepository.save(ds);
        }

        TiposMovimientoStock ingresoInicial = tiposMovimientoStockRepository.findByDescripcionTipoMovimiento("Ingreso Inicial")
                .orElseThrow(() -> new RuntimeException("Tipo de movimiento 'Ingreso Inicial' no encontrado."));

        MovimientosStock movimiento = new MovimientosStock(
                productoGuardado, ingresoInicial, productoGuardado.getStockActual(),
                0, productoGuardado.getStockActual(), admin, "Stock inicial por creación de producto.");
        movimientosStockRepository.save(movimiento);

        return convertToDetailDTO(productoGuardado);
    }

    @Transactional
    public Optional<ProductoDetalleDTO> updateProductoDTO(
            Integer id,
            ProductoActualizacionRequestDTO requestDTO,
            MultipartFile imagen,
            DetallesPlantaDTO detallesPlantaDTO,         // <-- NUEVO PARAMETRO
            DetallesHerramientaDTO detallesHerramientaDTO, // <-- NUEVO PARAMETRO
            DetallesSemillaDTO detallesSemillaDTO,       // <-- NUEVO PARAMETRO
            Integer adminId) {

        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isEmpty() || registroBajaRepository.findByNombreTablaAndRegistroId("productos", id).isPresent()) {
            return Optional.empty();
        }

        Administrador admin = administradorRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado con ID: " + adminId));

        Producto productoExistente = productoOpt.get();

        if (requestDTO.getNombreProducto() != null) {
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

        if (imagen != null && !imagen.isEmpty()) {
            try {
                Optional<ImagenesProducto> existingImageOpt = imagenesProductoRepository.findByProducto(productoExistente).stream().findFirst();

                String newImageUrl = fileStorageService.store(imagen);

                if(existingImageOpt.isPresent()){
                    ImagenesProducto existingImage = existingImageOpt.get();
                    existingImage.setUrlImagen(newImageUrl);
                    existingImage.setTextoAlternativo(productoExistente.getNombreProducto() + " actualizada");
                    imagenesProductoRepository.save(existingImage);
                } else {
                    ImagenesProducto newImage = new ImagenesProducto(productoExistente, newImageUrl, productoExistente.getNombreProducto() + " principal");
                    imagenesProductoRepository.save(newImage);
                }
            } catch (Exception e) {
                System.err.println("Error al actualizar la imagen para el producto " + productoExistente.getNombreProducto() + ": " + e.getMessage());
            }
        }

        if (requestDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(requestDTO.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + requestDTO.getCategoriaId()));
            productoExistente.setCategoria(categoria);
        }

        if (requestDTO.getTipoProductoId() != null &&
                (productoExistente.getTipoProducto() == null || !productoExistente.getTipoProducto().getTipoProductoId().equals(requestDTO.getTipoProductoId()))) {

            TipoProducto nuevoTipo = tipoProductoRepository.findById(requestDTO.getTipoProductoId())
                    .orElseThrow(() -> new RuntimeException("Tipo de Producto no encontrado con ID: " + requestDTO.getTipoProductoId()));

            detallesPlantaRepository.deleteById(productoExistente.getProductoId());
            detallesHerramientaRepository.deleteById(productoExistente.getProductoId());
            detallesSemillaRepository.deleteById(productoExistente.getProductoId());

            productoExistente.setTipoProducto(nuevoTipo);
        }

        if (requestDTO.getNuevoPrecioVenta() != null) {
            PrecioProductoActual ppa = productoExistente.getPrecioActual();
            if (ppa == null) {
                ppa = precioProductoActualRepository.findById(productoExistente.getProductoId()).orElse(new PrecioProductoActual(productoExistente, requestDTO.getNuevoPrecioVenta(), admin));
                ppa.setPrecioVenta(requestDTO.getNuevoPrecioVenta());
                ppa.setAdministrador(admin);
                ppa.setFechaInicioVigencia(LocalDateTime.now());
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
            productoExistente.setPrecioActual(ppa);
        }

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
            productoExistente.setCostoActual(cpa);
        }

        String tablaDetalleAsociadaActual = productoExistente.getTipoProducto() != null ? productoExistente.getTipoProducto().getTablaDetalleAsociada() : null;

        // --- MANEJO DE DETALLES ESPECIFICOS AHORA CON PARAMETROS DIRECTOS ---
        if ("detallesplanta".equalsIgnoreCase(tablaDetalleAsociadaActual) && detallesPlantaDTO != null) { // <-- Se verifica el nuevo parámetro
            NivelesLuz nivelLuz = nivelesLuzRepository.findByDescripcionNivelLuz(detallesPlantaDTO.getNivelLuzDescripcion())
                    .orElseThrow(() -> new RuntimeException("Nivel de luz no encontrado: " + detallesPlantaDTO.getNivelLuzDescripcion()));
            FrecuenciasRiego frecuenciaRiego = frecuenciasRiegoRepository.findByDescripcionFrecuenciaRiego(detallesPlantaDTO.getFrecuenciaRiegoDescripcion())
                    .orElseThrow(() -> new RuntimeException("Frecuencia de riego no encontrada: " + detallesPlantaDTO.getFrecuenciaRiegoDescripcion()));

            DetallesPlanta dp = detallesPlantaRepository.findById(productoExistente.getProductoId()).orElse(new DetallesPlanta());
            dp.setProducto(productoExistente);
            dp.setNombreCientifico(detallesPlantaDTO.getNombreCientifico());
            dp.setTipoAmbiente(detallesPlantaDTO.getTipoAmbiente());
            dp.setNivelLuz(nivelLuz);
            dp.setFrecuenciaRiego(frecuenciaRiego);
            dp.setEsVenenosa(detallesPlantaDTO.isEsVenenosa());
            dp.setCuidadosEspeciales(detallesPlantaDTO.getCuidadosEspeciales());
            detallesPlantaRepository.save(dp);
        } else if ("detallesherramienta".equalsIgnoreCase(tablaDetalleAsociadaActual) && detallesHerramientaDTO != null) { // <-- Se verifica el nuevo parámetro
            DetallesHerramienta dh = detallesHerramientaRepository.findById(productoExistente.getProductoId()).orElse(new DetallesHerramienta());
            dh.setProducto(productoExistente);
            dh.setMaterialPrincipal(detallesHerramientaDTO.getMaterialPrincipal());
            dh.setDimensiones(detallesHerramientaDTO.getDimensiones());
            dh.setPesoKG(detallesHerramientaDTO.getPesoKG());
            dh.setUsoRecomendado(detallesHerramientaDTO.getUsoRecomendado());
            dh.setRequiereMantenimiento(detallesHerramientaDTO.isRequiereMantenimiento());
            detallesHerramientaRepository.save(dh);
        } else if ("detallessemilla".equalsIgnoreCase(tablaDetalleAsociadaActual) && detallesSemillaDTO != null) { // <-- Se verifica el nuevo parámetro
            DetallesSemilla ds = detallesSemillaRepository.findById(productoExistente.getProductoId()).orElse(new DetallesSemilla());
            ds.setProducto(productoExistente);
            ds.setEspecieVariedad(detallesSemillaDTO.getEspecieVariedad());
            ds.setEpocaSiembraIdeal(detallesSemillaDTO.getEpocaSiembraIdeal());
            ds.setProfundidadSiembraCM(detallesSemillaDTO.getProfundidadSiembraCM());
            ds.setTiempoGerminacionDias(detallesSemillaDTO.getTiempoGerminacionDias());
            ds.setInstruccionesSiembra(detallesSemillaDTO.getInstruccionesSiembra());
            detallesSemillaRepository.save(ds);
        }

        Producto productoActualizado = productoRepository.save(productoExistente);
        return Optional.of(convertToDetailDTO(productoActualizado));
    }


    @Transactional
    public boolean softDeleteProducto(Integer id, String motivoBaja, Integer adminId) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            if (registroBajaRepository.findByNombreTablaAndRegistroId("productos", id).isPresent()) {
                return true;
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