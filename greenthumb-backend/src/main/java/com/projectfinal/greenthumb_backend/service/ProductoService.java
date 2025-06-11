package com.projectfinal.greenthumb_backend.service;

import com.projectfinal.greenthumb_backend.dto.*;
import com.projectfinal.greenthumb_backend.entities.*;
import com.projectfinal.greenthumb_backend.repositories.*;
import com.projectfinal.greenthumb_backend.service.FileStorageService;
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
    public ProductoDetalleDTO createProducto(ProductoCreacionRequestDTO requestDTO, Usuario adminUsuario) {
        // 1. Validamos que el usuario es un administrador
        if (!(adminUsuario instanceof Administrador)) {
            throw new IllegalStateException("Solo los administradores pueden crear productos.");
        }
        Administrador admin = (Administrador) adminUsuario; // Hacemos el cast para usarlo después
        if (requestDTO.getCategoriaId() == null || requestDTO.getTipoProductoId() == null) {
            throw new IllegalArgumentException("Los IDs de Categoría y Tipo de Producto son obligatorios.");
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

        // 3. Usamos el objeto 'admin' que obtuvimos del usuario autenticado.
        PrecioProductoActual ppa = new PrecioProductoActual(productoGuardado, requestDTO.getPrecioVenta(), admin);
        precioProductoActualRepository.save(ppa);

        CostoProductoActual cpa = new CostoProductoActual(productoGuardado, requestDTO.getCosto(), admin);
        costoProductoActualRepository.save(cpa);

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

        // 4. Usamos el objeto 'admin' también para el movimiento de stock.
        TiposMovimientoStock ingresoInicial = tiposMovimientoStockRepository.findByDescripcionTipoMovimiento("Ingreso Inicial")
                .orElseThrow(() -> new RuntimeException("Tipo de movimiento 'Ingreso Inicial' no encontrado."));

        MovimientosStock movimiento = new MovimientosStock(
                productoGuardado, ingresoInicial, productoGuardado.getStockActual(),
                0, productoGuardado.getStockActual(), admin, "Stock inicial por creación de producto.");
        movimientosStockRepository.save(movimiento);

        return convertToDetailDTO(productoGuardado);
    }

    // Dentro de la clase ProductoService

    @Transactional
    public Optional<ProductoDetalleDTO> updateProductoDTO(Integer id, ProductoActualizacionRequestDTO requestDTO, Usuario adminUsuario) {
        // 1. Buscamos el producto y validamos que exista y esté activo.
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isEmpty() || registroBajaRepository.findByNombreTablaAndRegistroId("productos", id).isPresent()) {
            return Optional.empty();
        }

        // 2. Validamos que el usuario sea un administrador.
        if (!(adminUsuario instanceof Administrador)) {
            throw new IllegalStateException("Solo los administradores pueden actualizar productos.");
        }
        Administrador admin = (Administrador) adminUsuario;
        Producto productoExistente = productoOpt.get();

        // 3. Llamamos a los métodos privados para cada bloque de lógica.
        actualizarCamposBasicos(productoExistente, requestDTO);
        actualizarStock(productoExistente, requestDTO.getStockActual(), admin);
        actualizarPreciosYCostos(productoExistente, requestDTO, admin);

        // Lógica para actualizar detalles específicos (si es necesario)
        if (requestDTO.getDetallesPlanta() != null || requestDTO.getDetallesHerramienta() != null || requestDTO.getDetallesSemilla() != null) {
            actualizarDetallesEspecificos(productoExistente, requestDTO);
        }

        // 4. Guardamos la entidad principal y devolvemos el DTO actualizado.
        Producto productoActualizado = productoRepository.save(productoExistente);
        return Optional.of(convertToDetailDTO(productoActualizado));
    }


    @Transactional
    public boolean softDeleteProducto(Integer id, String motivoBaja, Usuario adminUsuario) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            if (registroBajaRepository.findByNombreTablaAndRegistroId("productos", id).isPresent()) {
                return true; // Ya está de baja
            }
            // 1. Validamos y obtenemos el administrador
            if (!(adminUsuario instanceof Administrador)) {
                throw new IllegalStateException("Solo los administradores pueden dar de baja productos.");
            }
            Administrador admin = (Administrador) adminUsuario;

            // 2. Eliminamos la búsqueda por ID
            // Administrador admin = administradorRepository.findById(adminId).orElseThrow(...); // -- ESTA LÍNEA SE ELIMINA --

            RegistroBaja registro = new RegistroBaja("productos", id, motivoBaja, admin);
            registroBajaRepository.save(registro);
            return true;
        }
        return false;
    }

    @Transactional
    public ImagenProductoDTO addImagenToProducto(Integer productoId, MultipartFile file) {
        // 1. Guardar el archivo en el disco y obtener su nombre único
        String filename = fileStorageService.store(file);

        // 2. Obtener el producto de la base de datos
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId));

        // 3. Crear la URL pública para acceder a la imagen
        String urlImagen = "/uploads/" + filename;

        // 4. Crear y guardar la nueva entidad ImagenesProducto
        ImagenesProducto nuevaImagen = new ImagenesProducto(producto, urlImagen, producto.getNombreProducto());
        imagenesProductoRepository.save(nuevaImagen);

        // 5. Devolver un DTO con la información de la imagen recién creada
        return new ImagenProductoDTO(nuevaImagen.getImagenId(), nuevaImagen.getUrlImagen(), nuevaImagen.getTextoAlternativo());
    }

    private void actualizarCamposBasicos(Producto producto, ProductoActualizacionRequestDTO dto) {
        if (dto.getNombreProducto() != null && !dto.getNombreProducto().equalsIgnoreCase(producto.getNombreProducto())) {
            // Validar nombre único
            productoRepository.findByNombreProducto(dto.getNombreProducto()).ifPresent(existente -> {
                if (!existente.getProductoId().equals(producto.getProductoId())) {
                    throw new IllegalArgumentException("Ya existe otro producto con el nombre: " + dto.getNombreProducto());
                }
            });
            producto.setNombreProducto(dto.getNombreProducto());
        }
        if (dto.getDescripcionGeneral() != null) {
            producto.setDescripcionGeneral(dto.getDescripcionGeneral());
        }
        if (dto.getPuntoDeReorden() != null) {
            producto.setPuntoDeReorden(dto.getPuntoDeReorden());
        }
        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + dto.getCategoriaId()));
            producto.setCategoria(categoria);
        }
    }

    private void actualizarStock(Producto producto, Integer nuevoStock, Administrador admin) {
        if (nuevoStock != null && !producto.getStockActual().equals(nuevoStock)) {
            int stockPrevio = producto.getStockActual();
            int cantidadAfectada = nuevoStock - stockPrevio;

            String descMovimiento = cantidadAfectada > 0 ? "Ajuste Positivo" : "Ajuste Negativo";
            TiposMovimientoStock tipoAjuste = tiposMovimientoStockRepository.findByDescripcionTipoMovimiento(descMovimiento)
                    .orElseThrow(() -> new RuntimeException("Tipo de movimiento no encontrado: " + descMovimiento));

            MovimientosStock movimiento = new MovimientosStock(
                    producto, tipoAjuste, cantidadAfectada,
                    stockPrevio, nuevoStock, admin, "Ajuste de stock por actualización de producto.");
            movimientosStockRepository.save(movimiento);

            producto.setStockActual(nuevoStock);
        }
    }

    private void actualizarPreciosYCostos(Producto producto, ProductoActualizacionRequestDTO dto, Administrador admin) {
        // Actualizar Precio
        if (dto.getNuevoPrecioVenta() != null) {
            PrecioProductoActual ppa = precioProductoActualRepository.findById(producto.getProductoId()).orElseGet(() -> new PrecioProductoActual(producto, dto.getNuevoPrecioVenta(), admin));
            if (!ppa.getPrecioVenta().equals(dto.getNuevoPrecioVenta())) {
                PrecioProductoHistorial pph = new PrecioProductoHistorial(producto, ppa.getPrecioVenta(), ppa.getFechaInicioVigencia(), LocalDateTime.now(), admin);
                precioProductoHistorialRepository.save(pph);
                ppa.setPrecioVenta(dto.getNuevoPrecioVenta());
                ppa.setFechaInicioVigencia(LocalDateTime.now());
                ppa.setAdministrador(admin);
                precioProductoActualRepository.save(ppa);
            }
        }
        // Actualizar Costo
        if (dto.getNuevoCosto() != null) {
            CostoProductoActual cpa = costoProductoActualRepository.findById(producto.getProductoId()).orElseGet(() -> new CostoProductoActual(producto, dto.getNuevoCosto(), admin));
            if (!cpa.getPrecioCosto().equals(dto.getNuevoCosto())) {
                CostoProductoHistorial cph = new CostoProductoHistorial(producto, cpa.getPrecioCosto(), cpa.getFechaInicioVigencia(), LocalDateTime.now(), admin);
                costoProductoHistorialRepository.save(cph);
                cpa.setPrecioCosto(dto.getNuevoCosto());
                cpa.setFechaInicioVigencia(LocalDateTime.now());
                cpa.setAdministrador(admin);
                costoProductoActualRepository.save(cpa);
            }
        }
    }
// Dentro de la clase ProductoService.java

    private void actualizarDetallesEspecificos(Producto producto, ProductoActualizacionRequestDTO dto) {
        String tablaDetalle = producto.getTipoProducto().getTablaDetalleAsociada();

        if ("detallesplanta".equalsIgnoreCase(tablaDetalle) && dto.getDetallesPlanta() != null) {
            DetallesPlantaDTO dpDTO = dto.getDetallesPlanta();
            NivelesLuz nivelLuz = nivelesLuzRepository.findByDescripcionNivelLuz(dpDTO.getNivelLuzDescripcion()).orElseThrow(() -> new RuntimeException("Nivel de luz no encontrado"));
            FrecuenciasRiego frecRiego = frecuenciasRiegoRepository.findByDescripcionFrecuenciaRiego(dpDTO.getFrecuenciaRiegoDescripcion()).orElseThrow(() -> new RuntimeException("Frecuencia de riego no encontrada"));

            // --- LÍNEA CORREGIDA ---
            DetallesPlanta dp = detallesPlantaRepository.findById(producto.getProductoId())
                    .orElseGet(DetallesPlanta::new); // Usamos el constructor por defecto

            dp.setProducto(producto); // Y luego asignamos el producto
            dp.setNombreCientifico(dpDTO.getNombreCientifico());
            dp.setTipoAmbiente(dpDTO.getTipoAmbiente());
            dp.setNivelLuz(nivelLuz);
            dp.setFrecuenciaRiego(frecRiego);
            dp.setEsVenenosa(dpDTO.isEsVenenosa());
            dp.setCuidadosEspeciales(dpDTO.getCuidadosEspeciales());
            detallesPlantaRepository.save(dp);
        }
        // Añadir bloques "else if" para detallesherramienta y detallessemilla con una lógica similar
        else if ("detallesherramienta".equalsIgnoreCase(tablaDetalle) && dto.getDetallesHerramienta() != null) {
            DetallesHerramientaDTO dhDTO = dto.getDetallesHerramienta();
            DetallesHerramienta dh = detallesHerramientaRepository.findById(producto.getProductoId())
                    .orElseGet(DetallesHerramienta::new); // Misma corrección

            dh.setProducto(producto);
            dh.setMaterialPrincipal(dhDTO.getMaterialPrincipal());
            // ... setear los demás campos de herramienta ...
            detallesHerramientaRepository.save(dh);
        }
        else if ("detallessemilla".equalsIgnoreCase(tablaDetalle) && dto.getDetallesSemilla() != null) {
            DetallesSemillaDTO dsDTO = dto.getDetallesSemilla();
            DetallesSemilla ds = detallesSemillaRepository.findById(producto.getProductoId())
                    .orElseGet(DetallesSemilla::new); // Misma corrección

            ds.setProducto(producto);
            ds.setEspecieVariedad(dsDTO.getEspecieVariedad());
            // ... setear los demás campos de semilla ...
            detallesSemillaRepository.save(ds);
        }
    }

}
