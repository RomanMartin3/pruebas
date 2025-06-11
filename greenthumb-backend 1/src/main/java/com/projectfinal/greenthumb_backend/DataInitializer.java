package com.projectfinal.greenthumb_backend;

import com.projectfinal.greenthumb_backend.entities.*;
import com.projectfinal.greenthumb_backend.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AdministradorRepository administradorRepository;
    private final EstadosPedidoRepository estadosPedidoRepository;
    private final CategoriaRepository categoriaRepository;
    private final TipoProductoRepository tipoProductoRepository;
    private final PasswordEncoder passwordEncoder;
    private final NivelesLuzRepository nivelesLuzRepository;
    private final FrecuenciasRiegoRepository frecuenciasRiegoRepository;
    private final TiposMovimientoStockRepository tiposMovimientoStockRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final CostoProductoActualRepository costoProductoActualRepository;
    private final PrecioProductoActualRepository precioProductoActualRepository;
    private final DetallesHerramientaRepository detallesHerramientaRepository;
    private final DetallesPlantaRepository detallesPlantaRepository;
    private final DetallesSemillaRepository detallesSemillaRepository;
    private final MovimientosStockRepository movimientosStockRepository;
    private final ImagenesProductoRepository imagenesProductoRepository;
    private final CarritoItemRepository carritoItemRepository;
    private final PedidoRepository pedidoRepository;
    private final DetallesPedidoRepository detallesPedidoRepository;
    private final HistorialEstadosPedidoRepository historialEstadosPedidoRepository;
    private final PrecioProductoHistorialRepository precioProductoHistorialRepository;
    private final CostoProductoHistorialRepository costoProductoHistorialRepository;
    private final RegistroBajaRepository registroBajaRepository;


    private Administrador adminGlobal;


    public DataInitializer(AdministradorRepository administradorRepository,
                           EstadosPedidoRepository estadosPedidoRepository,
                           CategoriaRepository categoriaRepository,
                           TipoProductoRepository tipoProductoRepository,
                           PasswordEncoder passwordEncoder,
                           NivelesLuzRepository nivelesLuzRepository,
                           FrecuenciasRiegoRepository frecuenciasRiegoRepository,
                           TiposMovimientoStockRepository tiposMovimientoStockRepository,
                           ClienteRepository clienteRepository,
                           ProductoRepository productoRepository,
                           CostoProductoActualRepository costoProductoActualRepository,
                           PrecioProductoActualRepository precioProductoActualRepository,
                           DetallesHerramientaRepository detallesHerramientaRepository,
                           DetallesPlantaRepository detallesPlantaRepository,
                           DetallesSemillaRepository detallesSemillaRepository,
                           MovimientosStockRepository movimientosStockRepository,
                           ImagenesProductoRepository imagenesProductoRepository,
                           CarritoItemRepository carritoItemRepository,
                           PedidoRepository pedidoRepository,
                           DetallesPedidoRepository detallesPedidoRepository,
                           HistorialEstadosPedidoRepository historialEstadosPedidoRepository,
                           PrecioProductoHistorialRepository precioProductoHistorialRepository,
                           CostoProductoHistorialRepository costoProductoHistorialRepository,
                           RegistroBajaRepository registroBajaRepository) {
        this.administradorRepository = administradorRepository;
        this.estadosPedidoRepository = estadosPedidoRepository;
        this.categoriaRepository = categoriaRepository;
        this.tipoProductoRepository = tipoProductoRepository;
        this.passwordEncoder = passwordEncoder;
        this.nivelesLuzRepository = nivelesLuzRepository;
        this.frecuenciasRiegoRepository = frecuenciasRiegoRepository;
        this.tiposMovimientoStockRepository = tiposMovimientoStockRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.costoProductoActualRepository = costoProductoActualRepository;
        this.precioProductoActualRepository = precioProductoActualRepository;
        this.detallesHerramientaRepository = detallesHerramientaRepository;
        this.detallesPlantaRepository = detallesPlantaRepository;
        this.detallesSemillaRepository = detallesSemillaRepository;
        this.movimientosStockRepository = movimientosStockRepository;
        this.imagenesProductoRepository = imagenesProductoRepository;
        this.carritoItemRepository = carritoItemRepository;
        this.pedidoRepository = pedidoRepository;
        this.detallesPedidoRepository = detallesPedidoRepository;
        this.historialEstadosPedidoRepository = historialEstadosPedidoRepository;
        this.precioProductoHistorialRepository = precioProductoHistorialRepository;
        this.costoProductoHistorialRepository = costoProductoHistorialRepository;
        this.registroBajaRepository = registroBajaRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("======================================================");
        System.out.println("INICIANDO CARGA DE DATOS PREDEFINIDOS (DataInitializer)");
        System.out.println("======================================================");

        crearAdminSiNoExiste();
        crearEstadosDePedido();
        crearCategoriasSimples();
        crearTiposDeProducto();
        crearNivelesDeLuz();
        crearFrecuenciasDeRiego();
        crearTiposDeMovimientoStock();
        crearClientesDeEjemplo();
        crearProductosDeEjemplo(); // Esto ya crea costos y precios actuales y movimientos de stock

        // Nuevas llamadas en orden:
        crearImagenesProductoDeEjemplo();
        crearCarritoItemsDeEjemplo();
        crearPedidosDeEjemplo(); // Esto crea Pedido, DetallesPedido y el primer HistorialEstadosPedido
        simularCambioDePrecioYCosto(); // Esto crea PrecioProductoHistorial y CostoProductoHistorial
        // simularRegistroBaja(); // Opcional, si se necesita probar

        System.out.println("======================================================");
        System.out.println("CARGA DE DATOS PREDEFINIDOS FINALIZADA");
        System.out.println("======================================================");
    }

    private void crearAdminSiNoExiste() {
        Optional<Administrador> optAdmin = administradorRepository.findByEmail("admin@greenthumb.com");
        if (optAdmin.isEmpty()) {
            Administrador admin = new Administrador();
            admin.setNombre("Admin");
            admin.setApellido("GreenThumb");
            admin.setEmail("admin@greenthumb.com");
            String contrasenaHasheada = passwordEncoder.encode("admin123");
            admin.setContrasena(contrasenaHasheada);
            admin.setTelefono("0000000000");
            admin.setNivelAcceso("AdminGeneral");
            this.adminGlobal = administradorRepository.save(admin);
            System.out.println("-> Administrador por defecto creado: admin@greenthumb.com");
        } else {
            this.adminGlobal = optAdmin.get();
            System.out.println("-> El administrador admin@greenthumb.com ya existe.");
        }
    }

    private void crearEstadosDePedido() {
        crearEstadoPedidoSiNoExiste("Pendiente", "El pedido ha sido recibido y está pendiente de procesamiento.", false);
        crearEstadoPedidoSiNoExiste("Confirmado", "El pedido ha sido confirmado y pasará a preparación.", false);
        crearEstadoPedidoSiNoExiste("En Preparación", "El pedido está siendo preparado en nuestro local.", false);
        crearEstadoPedidoSiNoExiste("Entregado", "El pedido ha sido entregado satisfactoriamente.", true);
        crearEstadoPedidoSiNoExiste("Cancelado", "El pedido ha sido cancelado.", true);
        crearEstadoPedidoSiNoExiste("Rechazado", "El pedido ha sido rechazado (ej. problema de pago).", true);
    }

    private void crearCategoriasSimples() {
        System.out.println("-> Creando categorías (estructura plana)...");
        crearCategoriaSiNoExiste("Plantas de Interior", "Variedad de plantas para decorar y purificar el interior de tu hogar.");
        crearCategoriaSiNoExiste("Plantas de Exterior", "Plantas resistentes para jardines, balcones y terrazas.");
        crearCategoriaSiNoExiste("Suculentas y Cactus", "Colección de plantas de bajo mantenimiento y formas exóticas.");
        crearCategoriaSiNoExiste("Herramientas de Jardinería", "Utensilios esenciales para el cuidado de tus plantas.");
        crearCategoriaSiNoExiste("Macetas y Soportes", "Recipientes y accesorios para tus plantas.");
        crearCategoriaSiNoExiste("Semillas", "Semillas para cultivar tus propias plantas, flores y hortalizas.");
        crearCategoriaSiNoExiste("Sustratos y Fertilizantes", "Tierras, abonos y nutrientes.");
        crearCategoriaSiNoExiste("Árboles y Arbustos", "Especies leñosas para paisajismo y sombra.");
        crearCategoriaSiNoExiste("Plantas Aromáticas y Medicinales", "Hierbas para cocinar, infusiones y remedios naturales.");
    }

    private void crearClientesDeEjemplo() {
        if (clienteRepository.findByEmail("juan.perez@example.com").isEmpty()) {
            Cliente cliente1 = new Cliente();
            cliente1.setNombre("Juan");
            cliente1.setApellido("Perez");
            cliente1.setEmail("juan.perez@example.com");
            cliente1.setContrasena(passwordEncoder.encode("cliente123"));
            cliente1.setTelefono("2611234567");
            cliente1.setCalle("Av. Siempre Viva");
            cliente1.setNumero("742");
            cliente1.setCiudad("Springfield");
            cliente1.setProvincia("Mendoza");
            cliente1.setCodigoPostal("5500");
            clienteRepository.save(cliente1);
            System.out.println("-> Cliente creado: juan.perez@example.com");
        }

        if (clienteRepository.findByEmail("ana.gomez@example.com").isEmpty()) {
            Cliente cliente2 = new Cliente();
            cliente2.setNombre("Ana");
            cliente2.setApellido("Gomez");
            cliente2.setEmail("ana.gomez@example.com");
            cliente2.setContrasena(passwordEncoder.encode("cliente456"));
            cliente2.setTelefono("2617654321");
            cliente2.setCalle("Calle Falsa");
            cliente2.setNumero("123");
            cliente2.setCiudad("Godoy Cruz");
            cliente2.setProvincia("Mendoza");
            cliente2.setCodigoPostal("5501");
            clienteRepository.save(cliente2);
            System.out.println("-> Cliente creado: ana.gomez@example.com");
        }
    }

    private void crearTiposDeProducto() {
        crearTipoProductoSiNoExiste("Planta Viva", "detallesplanta");
        crearTipoProductoSiNoExiste("Herramienta Manual", "detallesherramienta");
        crearTipoProductoSiNoExiste("Paquete de Semillas", "detallessemilla");
        crearTipoProductoSiNoExiste("Maceta", "detallesmaceta");
        crearTipoProductoSiNoExiste("Sustrato Preparado", "detallessustrato");
        crearTipoProductoSiNoExiste("Fertilizante Líquido", "detallesfertilizante");
        crearTipoProductoSiNoExiste("Fertilizante Granulado", "detallesfertilizante");
    }

    private void crearNivelesDeLuz() {
        crearNivelLuzSiNoExiste("Sol Directo (Pleno Sol)");
        crearNivelLuzSiNoExiste("Luz Brillante Indirecta (Semi-Sombra)");
        crearNivelLuzSiNoExiste("Luz Media");
        crearNivelLuzSiNoExiste("Baja Luz (Sombra)");
        crearNivelLuzSiNoExiste("Indiferente");
        crearNivelLuzSiNoExiste("_NO_ESPECIFICADO_");
    }

    private void crearFrecuenciasDeRiego() {
        crearFrecuenciaRiegoSiNoExiste("Alto (Mantener sustrato húmedo)");
        crearFrecuenciaRiegoSiNoExiste("Medio (Regar cuando la capa superior se seca)");
        crearFrecuenciaRiegoSiNoExiste("Bajo (Dejar secar completamente entre riegos)");
        crearFrecuenciaRiegoSiNoExiste("Muy Bajo (Riego esporádico)");
        crearFrecuenciaRiegoSiNoExiste("Indiferente");
        crearFrecuenciaRiegoSiNoExiste("_NO_ESPECIFICADO_");
    }

    private void crearTiposDeMovimientoStock() {
        crearTipoMovimientoStockSiNoExiste("Ingreso Inicial", 1);
        crearTipoMovimientoStockSiNoExiste("Compra a Proveedor", 1);
        crearTipoMovimientoStockSiNoExiste("Ajuste Positivo", 1);
        crearTipoMovimientoStockSiNoExiste("Venta Cliente", -1);
        crearTipoMovimientoStockSiNoExiste("Devolución Cliente (Buen Estado)", 1);
        crearTipoMovimientoStockSiNoExiste("Merma/Deterioro", -1);
        crearTipoMovimientoStockSiNoExiste("Ajuste Negativo", -1);
    }

    private void crearProductosDeEjemplo() {
        Categoria catPlantasInterior = categoriaRepository.findByNombreCategoria("Plantas de Interior").orElse(null);
        Categoria catHerramientas = categoriaRepository.findByNombreCategoria("Herramientas de Jardinería").orElse(null);
        Categoria catSemillas = categoriaRepository.findByNombreCategoria("Semillas").orElse(null);

        TipoProducto tipoPlanta = tipoProductoRepository.findByNombreTipoProducto("Planta Viva").orElse(null);
        TipoProducto tipoHerramienta = tipoProductoRepository.findByNombreTipoProducto("Herramienta Manual").orElse(null);
        TipoProducto tipoSemilla = tipoProductoRepository.findByNombreTipoProducto("Paquete de Semillas").orElse(null);

        NivelesLuz nivelLuzMedia = nivelesLuzRepository.findByDescripcionNivelLuz("Luz Media").orElse(null);
        NivelesLuz nivelSolDirecto = nivelesLuzRepository.findByDescripcionNivelLuz("Sol Directo (Pleno Sol)").orElse(null);
        FrecuenciasRiego riegoMedio = frecuenciasRiegoRepository.findByDescripcionFrecuenciaRiego("Medio (Regar cuando la capa superior se seca)").orElse(null);
        TiposMovimientoStock ingresoInicial = tiposMovimientoStockRepository.findByDescripcionTipoMovimiento("Ingreso Inicial").orElse(null);

        if (this.adminGlobal == null) {
            System.err.println("ERROR: El administrador global no fue inicializado. No se pueden crear productos.");
            Optional<Administrador> optAdmin = administradorRepository.findByEmail("admin@greenthumb.com");
            if(optAdmin.isPresent()){
                this.adminGlobal = optAdmin.get();
            } else {
                System.err.println("ERROR CRÍTICO: No se pudo recuperar el administrador global.");
                return;
            }
        }
        if (ingresoInicial == null) {
            System.err.println("ERROR: El tipo de movimiento 'Ingreso Inicial' no fue encontrado.");
            return;
        }

        String nombreP1 = "Helecho Culantrillo";
        if (productoRepository.findByNombreProducto(nombreP1).isEmpty() && catPlantasInterior != null && tipoPlanta != null && nivelLuzMedia != null && riegoMedio != null) {
            Producto p1 = new Producto(nombreP1, "Adiantum capillus-veneris, delicado helecho para interiores húmedos y sombreados.",
                    15, 5, catPlantasInterior, tipoPlanta);
            productoRepository.save(p1);

            DetallesPlanta dp1 = new DetallesPlanta(p1, "Adiantum capillus-veneris", "Interior Húmedo", nivelLuzMedia, riegoMedio, false, "Alta humedad ambiental, pulverizar. Evitar corrientes de aire.");
            detallesPlantaRepository.save(dp1);

            crearCostoYPrecioProducto(p1, new BigDecimal("7.50"), this.adminGlobal);
            crearMovimientoStock(p1, ingresoInicial, 15, 0, 15, this.adminGlobal, "Stock inicial Helecho Culantrillo");
            System.out.println("-> Producto creado: " + nombreP1);
        }

        String nombreP2 = "Pala de Jardín Pequeña";
        if (productoRepository.findByNombreProducto(nombreP2).isEmpty() && catHerramientas != null && tipoHerramienta != null) {
            Producto p2 = new Producto(nombreP2, "Pala de mano resistente con mango ergonómico, ideal para trasplantes y pequeñas excavaciones.",
                    30, 10, catHerramientas, tipoHerramienta);
            productoRepository.save(p2);

            DetallesHerramienta dh2 = new DetallesHerramienta(p2, "Acero Inoxidable y Plástico Reforzado", "28cm largo", new BigDecimal("0.30"), "Trasplantar, remover tierra, mezclar sustrato.", false);
            detallesHerramientaRepository.save(dh2);

            crearCostoYPrecioProducto(p2, new BigDecimal("5.00"), this.adminGlobal);
            crearMovimientoStock(p2, ingresoInicial, 30, 0, 30, this.adminGlobal, "Stock inicial Pala Pequeña");
            System.out.println("-> Producto creado: " + nombreP2);
        }

        String nombreP3 = "Semillas de Albahaca Genovesa";
        if (productoRepository.findByNombreProducto(nombreP3).isEmpty() && catSemillas != null && tipoSemilla != null && nivelSolDirecto != null) {
            Producto p3 = new Producto(nombreP3, "Paquete con aproximadamente 100 semillas de Albahaca Genovesa, ideal para huertos y macetas.",
                    100, 20, catSemillas, tipoSemilla);
            productoRepository.save(p3);

            DetallesSemilla ds3 = new DetallesSemilla(p3, "Ocimum basilicum 'Genovese'", "Primavera-Verano", new BigDecimal("0.5"), "5-10 días", "Sembrar en almácigo o directo al suelo bien drenado y soleado. Mantener húmedo.");
            detallesSemillaRepository.save(ds3);

            crearCostoYPrecioProducto(p3, new BigDecimal("1.50"), this.adminGlobal);
            crearMovimientoStock(p3, ingresoInicial, 100, 0, 100, this.adminGlobal, "Stock inicial Semillas de Albahaca");
            System.out.println("-> Producto creado: " + nombreP3);
        }

    }


    private void crearCostoYPrecioProducto(Producto producto, BigDecimal costo, Administrador admin) {
        if (costoProductoActualRepository.findById(producto.getProductoId()).isEmpty()) {
            CostoProductoActual costoActual = new CostoProductoActual(producto, costo, admin);
            costoProductoActualRepository.save(costoActual);
        }

        if (precioProductoActualRepository.findById(producto.getProductoId()).isEmpty()) {
            BigDecimal porcentajeGanancia = new BigDecimal("0.25");
            BigDecimal precioVenta = costo.multiply(BigDecimal.ONE.add(porcentajeGanancia));
            precioVenta = precioVenta.setScale(2, RoundingMode.HALF_UP);

            PrecioProductoActual precioActual = new PrecioProductoActual(producto, precioVenta, admin);
            precioProductoActualRepository.save(precioActual);
        }
    }

    private void crearMovimientoStock(Producto producto, TiposMovimientoStock tipoMov,
                                      int cantidadAfectada, int stockPrevio, int stockNuevo,
                                      Administrador admin, String notas) {
        MovimientosStock movimiento = new MovimientosStock(
                producto, tipoMov, cantidadAfectada, stockPrevio, stockNuevo, admin, notas);
        movimientosStockRepository.save(movimiento);
    }

    private void crearImagenesProductoDeEjemplo() {
        Optional<Producto> optProd1 = productoRepository.findByNombreProducto("Helecho Culantrillo");
        if (optProd1.isPresent()) {
            Producto prod1 = optProd1.get();
            // Evitar duplicados si se corre varias veces
            if (imagenesProductoRepository.findByProducto(prod1).isEmpty()) {
                ImagenesProducto imgP1 = new ImagenesProducto(prod1, "/uploads/helecho.jpg", "Helecho Culantrillo Principal");
                imagenesProductoRepository.save(imgP1);
                System.out.println("-> Imágenes creadas para producto: " + prod1.getNombreProducto());
            }
        }
        Optional<Producto> optProd2 = productoRepository.findByNombreProducto("Pala de Jardín Pequeña");
        if (optProd2.isPresent()) {
            Producto prod2 = optProd2.get();
            if (imagenesProductoRepository.findByProducto(prod2).isEmpty()) {
                ImagenesProducto imgP2 = new ImagenesProducto(prod2, "/uploads/paladejardin.jpg", "Pala de jardín");
                imagenesProductoRepository.save(imgP2);
                System.out.println("-> Imágenes creadas para producto: " + prod2.getNombreProducto());
            }
        }
        Optional<Producto> optProd3 = productoRepository.findByNombreProducto("Semillas de Albahaca Genovesa");
        if (optProd3.isPresent()) {
            Producto prod3 = optProd3.get();
            if (imagenesProductoRepository.findByProducto(prod3).isEmpty()) {
                ImagenesProducto imgP3 = new ImagenesProducto(prod3, "/uploads/SemillasdeAlbahacaGenovesa.jpg", "Semillas de Albahaca Genovesa");
                imagenesProductoRepository.save(imgP3);
                System.out.println("-> Imágenes creadas para producto: " + prod3.getNombreProducto());
            }
        }
    }

    private void crearCarritoItemsDeEjemplo() {
        Optional<Cliente> optCliente1 = clienteRepository.findByEmail("juan.perez@example.com");
        Optional<Producto> optProd1 = productoRepository.findByNombreProducto("Helecho Culantrillo");
        Optional<Producto> optProd2 = productoRepository.findByNombreProducto("Pala de Jardín Pequeña");

        if (optCliente1.isPresent() && optProd1.isPresent()) {
            Cliente cliente1 = optCliente1.get();
            Producto prod1 = optProd1.get();
            PrecioProductoActual precioProd1 = precioProductoActualRepository.findById(prod1.getProductoId()).orElse(null);

            if (precioProd1 != null && carritoItemRepository.findByClienteAndProducto(cliente1, prod1).isEmpty()) {
                CarritoItem item1 = new CarritoItem(cliente1, prod1, 2, precioProd1.getPrecioVenta());
                carritoItemRepository.save(item1);
                System.out.println("-> CarritoItem creado para " + cliente1.getEmail() + " - Producto: " + prod1.getNombreProducto());
            }
        }
        if (optCliente1.isPresent() && optProd2.isPresent()) {
            Cliente cliente1 = optCliente1.get();
            Producto prod2 = optProd2.get();
            PrecioProductoActual precioProd2 = precioProductoActualRepository.findById(prod2.getProductoId()).orElse(null);
            if (precioProd2 != null && carritoItemRepository.findByClienteAndProducto(cliente1, prod2).isEmpty()) {
                CarritoItem item2 = new CarritoItem(cliente1, prod2, 1, precioProd2.getPrecioVenta());
                carritoItemRepository.save(item2);
                System.out.println("-> CarritoItem creado para " + cliente1.getEmail() + " - Producto: " + prod2.getNombreProducto());
            }
        }
    }

    private void crearPedidosDeEjemplo() {
        Optional<Cliente> optCliente1 = clienteRepository.findByEmail("juan.perez@example.com");
        Optional<EstadosPedido> estadoPendiente = estadosPedidoRepository.findByNombreEstado("Pendiente");
        Optional<Producto> optProd1 = productoRepository.findByNombreProducto("Helecho Culantrillo");

        if (optCliente1.isPresent() && estadoPendiente.isPresent() && optProd1.isPresent() && adminGlobal != null) {
            Cliente cliente1 = optCliente1.get();
            Producto prod1 = optProd1.get();
            EstadosPedido pendiente = estadoPendiente.get();

            // Solo crear si no hay pedidos para este cliente (simplificación para DataInitializer)
            if (pedidoRepository.findByClienteOrderByFechaPedidoDesc(cliente1).isEmpty()) {
                Pedido pedido1 = new Pedido();
                pedido1.setCliente(cliente1);
                pedido1.setEstadoPedido(pendiente);
                pedido1.setMetodoPagoSimulado("Efectivo en Tienda");
                pedido1.setNotasCliente("Retiro el sábado por la mañana.");
                // pedido1.setNotasAdmin(""); // @PrePersist lo maneja

                PrecioProductoActual precioProd1 = precioProductoActualRepository.findById(prod1.getProductoId()).orElse(null);
                if (precioProd1 != null) {
                    DetallesPedido detalle1 = new DetallesPedido(pedido1, prod1, 1); // Cantidad 1
                    // El precio actual se tomaría del producto al momento de "facturar" o se podría añadir a DetallesPedido
                    pedido1.addDetalle(detalle1);
                }

                // Simular que toma otro producto
                Optional<Producto> optProd2 = productoRepository.findByNombreProducto("Pala de Jardín Pequeña");
                if(optProd2.isPresent()){
                    Producto prod2 = optProd2.get();
                    PrecioProductoActual precioProd2 = precioProductoActualRepository.findById(prod2.getProductoId()).orElse(null);
                    if (precioProd2 != null) {
                        DetallesPedido detalle2 = new DetallesPedido(pedido1, prod2, 1); // Cantidad 1
                        pedido1.addDetalle(detalle2);
                    }
                }

                pedidoRepository.save(pedido1); // Guarda Pedido y sus DetallesPedido
                System.out.println("-> Pedido creado ID: " + pedido1.getPedidoId() + " para cliente: " + cliente1.getEmail());

                // Crear historial de estado inicial
                if (historialEstadosPedidoRepository.findByPedidoOrderByFechaCambioAsc(pedido1).isEmpty()) {
                    HistorialEstadosPedido historialInicial = new HistorialEstadosPedido(pedido1, pendiente, adminGlobal, "Pedido generado desde el sistema.");
                    historialEstadosPedidoRepository.save(historialInicial);
                    System.out.println("-> Historial de estado inicial creado para Pedido ID: " + pedido1.getPedidoId());
                }
            }
        }
    }

    private void simularCambioDePrecioYCosto() {
        Optional<Producto> optProd = productoRepository.findByNombreProducto("Helecho Culantrillo");
        if (optProd.isEmpty()) { // Si el producto no existe, intentar con otro.
            optProd = productoRepository.findByNombreProducto("Pala de Jardín Pequeña");
        }

        if (optProd.isPresent() && adminGlobal != null) {
            Producto producto = optProd.get();

            // Simular cambio de precio de venta
            PrecioProductoActual precioActual = precioProductoActualRepository.findById(producto.getProductoId()).orElse(null);
            if (precioActual != null) {
                // Guardar el precio viejo en historial
                PrecioProductoHistorial precioHist = new PrecioProductoHistorial(
                        producto,
                        precioActual.getPrecioVenta(),
                        precioActual.getFechaInicioVigencia(),
                        LocalDateTime.now(), // Fin de vigencia del precio viejo
                        adminGlobal);
                precioProductoHistorialRepository.save(precioHist);
                System.out.println("-> PrecioProductoHistorial creado para producto ID: " + producto.getProductoId());

                // Actualizar precio actual
                BigDecimal nuevoPrecio = precioActual.getPrecioVenta().add(new BigDecimal("0.50")); // Pequeño aumento
                precioActual.setPrecioVenta(nuevoPrecio);
                precioActual.setFechaInicioVigencia(LocalDateTime.now());
                precioProductoActualRepository.save(precioActual);
                System.out.println("-> PrecioProductoActual actualizado para producto ID: " + producto.getProductoId() + " a " + nuevoPrecio);
            }

            // Simular cambio de costo
            CostoProductoActual costoActual = costoProductoActualRepository.findById(producto.getProductoId()).orElse(null);
            if (costoActual != null) {
                // Guardar costo viejo en historial
                CostoProductoHistorial costoHist = new CostoProductoHistorial(
                        producto,
                        costoActual.getPrecioCosto(),
                        costoActual.getFechaInicioVigencia(),
                        LocalDateTime.now(), // Fin de vigencia
                        adminGlobal);
                costoProductoHistorialRepository.save(costoHist);
                System.out.println("-> CostoProductoHistorial creado para producto ID: " + producto.getProductoId());

                // Actualizar costo actual
                BigDecimal nuevoCosto = costoActual.getPrecioCosto().add(new BigDecimal("0.20"));
                costoActual.setPrecioCosto(nuevoCosto);
                costoActual.setFechaInicioVigencia(LocalDateTime.now());
                costoProductoActualRepository.save(costoActual);
                System.out.println("-> CostoProductoActual actualizado para producto ID: " + producto.getProductoId() + " a " + nuevoCosto);
            }
        }
    }

    private void simularRegistroBaja() {
        // Solo como ejemplo, usualmente no se crean bajas en DataInitializer
        Optional<Categoria> optCategoria = categoriaRepository.findByNombreCategoria("Árboles y Arbustos"); // Una categoría que podría no usarse mucho
        if (optCategoria.isPresent() && adminGlobal != null) {
            Categoria categoriaABajar = optCategoria.get();
            // Verificar que no haya ya un registro de baja
            if (registroBajaRepository.findByNombreTablaAndRegistroId("categorias", categoriaABajar.getCategoriaId()).isEmpty()) {
                RegistroBaja baja = new RegistroBaja("categorias", categoriaABajar.getCategoriaId(), "Baja de prueba por DataInitializer.", adminGlobal);
                registroBajaRepository.save(baja);
                System.out.println("-> RegistroBaja simulado para categoría: " + categoriaABajar.getNombreCategoria());
            }
        }
    }


    // --- Métodos helper existentes ---
    private void crearEstadoPedidoSiNoExiste(String nombre, String descripcion, boolean esFinal) {
        if (estadosPedidoRepository.findByNombreEstado(nombre).isEmpty()) {
            EstadosPedido estado = new EstadosPedido(nombre, descripcion, esFinal);
            estadosPedidoRepository.save(estado);
            System.out.println("-> Estado de pedido creado: " + nombre);
        }
    }

    private void crearCategoriaSiNoExiste(String nombre, String descripcion) {
        Optional<Categoria> optCategoria = categoriaRepository.findByNombreCategoria(nombre);
        if (optCategoria.isEmpty()) {
            Categoria categoria = new Categoria(nombre, descripcion);
            categoriaRepository.save(categoria);
            System.out.println("-> Categoría creada: " + nombre);
        }
    }

    private void crearTipoProductoSiNoExiste(String nombre, String tablaDetalle) {
        if (tipoProductoRepository.findByNombreTipoProducto(nombre).isEmpty()) {
            TipoProducto tipo = new TipoProducto(nombre, tablaDetalle);
            tipoProductoRepository.save(tipo);
            System.out.println("-> Tipo de producto creado: " + nombre);
        }
    }

    private void crearNivelLuzSiNoExiste(String descripcion) {
        if (nivelesLuzRepository.findByDescripcionNivelLuz(descripcion).isEmpty()) {
            NivelesLuz nivel = new NivelesLuz(descripcion);
            nivelesLuzRepository.save(nivel);
            System.out.println("-> Nivel de luz creado: " + descripcion);
        }
    }

    private void crearFrecuenciaRiegoSiNoExiste(String descripcion) {
        if (frecuenciasRiegoRepository.findByDescripcionFrecuenciaRiego(descripcion).isEmpty()) {
            FrecuenciasRiego frecuencia = new FrecuenciasRiego(descripcion);
            frecuenciasRiegoRepository.save(frecuencia);
            System.out.println("-> Frecuencia de riego creada: " + descripcion);
        }
    }

    private void crearTipoMovimientoStockSiNoExiste(String descripcion, Integer afectaComo) {
        if (tiposMovimientoStockRepository.findByDescripcionTipoMovimiento(descripcion).isEmpty()) {
            TiposMovimientoStock tipoMov = new TiposMovimientoStock(descripcion, afectaComo);
            tiposMovimientoStockRepository.save(tipoMov);
            System.out.println("-> Tipo de movimiento de stock creado: " + descripcion);
        }
    }
}