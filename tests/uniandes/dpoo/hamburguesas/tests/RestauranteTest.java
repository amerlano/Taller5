package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.excepciones.IngredienteRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoFaltanteException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

public class RestauranteTest
{
    private static final String DATA = "./data/";
    private static final String PRUEBAS = "./data/pruebas/";
    private static final String CARPETA_FACTURAS = "./facturas/";

    private Restaurante restaurante;

    private ArrayList<File> facturasCreadas;

    @BeforeEach
    void setUp( ) throws Exception
    {
        restaurante = new Restaurante( );
        facturasCreadas = new ArrayList<File>( );
    }

    @AfterEach
    void tearDown( ) throws Exception
    {
        for( File f : facturasCreadas )
        {
            f.delete( );
        }
    }

    private void cargarArchivos( String ingredientes, String menu, String combos ) throws Exception
    {
        restaurante.cargarInformacionRestaurante( new File( ingredientes ), new File( menu ), new File( combos ) );
    }

    private void cargarDatosPrueba( ) throws Exception
    {
        cargarArchivos( PRUEBAS + "ingredientes_ok.txt", PRUEBAS + "menu_ok.txt", PRUEBAS + "combos_ok.txt" );
    }

    private File archivoFactura( Pedido p )
    {
        File f = new File( CARPETA_FACTURAS + "factura_" + p.getIdPedido( ) + ".txt" );
        facturasCreadas.add( f );
        return f;
    }

    @Test
    void testRestauranteVacio( )
    {
        assertNull( restaurante.getPedidoEnCurso( ), "Un restaurante nuevo no debe tener pedido en curso." );
        assertEquals( 0, restaurante.getPedidos( ).size( ), "Un restaurante nuevo no debe tener pedidos." );
        assertEquals( 0, restaurante.getIngredientes( ).size( ), "Un restaurante nuevo no debe tener ingredientes." );
        assertEquals( 0, restaurante.getMenuBase( ).size( ), "Un restaurante nuevo no debe tener productos." );
        assertEquals( 0, restaurante.getMenuCombos( ).size( ), "Un restaurante nuevo no debe tener combos." );
    }

    @Test
    void testCargarArchivosReales( ) throws Exception
    {
        cargarArchivos( DATA + "ingredientes.txt", DATA + "menu.txt", DATA + "combos.txt" );
        assertEquals( 15, restaurante.getIngredientes( ).size( ), "No se cargaron todos los ingredientes." );
        assertEquals( 22, restaurante.getMenuBase( ).size( ), "No se cargaron todos los productos." );
        assertEquals( 4, restaurante.getMenuCombos( ).size( ), "No se cargaron todos los combos." );
    }

    @Test
    void testCargarIngredientes( ) throws Exception
    {
        cargarDatosPrueba( );
        ArrayList<Ingrediente> ingredientes = restaurante.getIngredientes( );
        assertEquals( 3, ingredientes.size( ), "Las líneas vacías no deben generar ingredientes." );
        assertEquals( "lechuga", ingredientes.get( 0 ).getNombre( ) );
        assertEquals( 1000, ingredientes.get( 0 ).getCostoAdicional( ) );
        assertEquals( "queso americano", ingredientes.get( 2 ).getNombre( ) );
        assertEquals( 2500, ingredientes.get( 2 ).getCostoAdicional( ) );
    }

    @Test
    void testCargarMenu( ) throws Exception
    {
        cargarDatosPrueba( );
        ArrayList<ProductoMenu> menu = restaurante.getMenuBase( );
        assertEquals( 4, menu.size( ), "Las líneas vacías no deben generar productos." );
        assertEquals( "corral", menu.get( 0 ).getNombre( ) );
        assertEquals( 14000, menu.get( 0 ).getPrecio( ) );
        assertEquals( "especial", menu.get( 3 ).getNombre( ) );
        assertEquals( 24000, menu.get( 3 ).getPrecio( ) );
    }

    @Test
    void testCargarCombos( ) throws Exception
    {
        cargarDatosPrueba( );
        ArrayList<Combo> combos = restaurante.getMenuCombos( );
        assertEquals( 2, combos.size( ), "Las líneas vacías no deben generar combos." );
        assertEquals( "combo corral", combos.get( 0 ).getNombre( ) );
        assertEquals( 22050, combos.get( 0 ).getPrecio( ), "El descuento del combo no se cargó bien." );
        assertEquals( "combo especial", combos.get( 1 ).getNombre( ) );
        assertEquals( 31222, combos.get( 1 ).getPrecio( ), "El descuento decimal del combo no se cargó bien." );
    }

    @Test
    void testIngredienteRepetido( )
    {
        assertThrows( IngredienteRepetidoException.class,
                ( ) -> cargarArchivos( PRUEBAS + "ingredientes_repetidos.txt", PRUEBAS + "menu_ok.txt", PRUEBAS + "combos_ok.txt" ),
                "Debe lanzar IngredienteRepetidoException." );
    }

    @Test
    void testProductoRepetido( )
    {
        assertThrows( ProductoRepetidoException.class, ( ) -> cargarArchivos( PRUEBAS + "ingredientes_ok.txt", PRUEBAS + "menu_repetido.txt", PRUEBAS + "combos_ok.txt" ),
                "Debe lanzar ProductoRepetidoException si un producto está repetido." );
    }

    @Test
    void testComboRepetido( )
    {
        assertThrows( ProductoRepetidoException.class, ( ) -> cargarArchivos( PRUEBAS + "ingredientes_ok.txt", PRUEBAS + "menu_ok.txt", PRUEBAS + "combos_repetidos.txt" ),
                "Debe lanzar ProductoRepetidoException si un combo está repetido." );
    }

    @Test
    void testComboProductoFaltante( )
    {
        assertThrows( ProductoFaltanteException.class,
                ( ) -> cargarArchivos( PRUEBAS + "ingredientes_ok.txt", PRUEBAS + "menu_ok.txt", PRUEBAS + "combos_producto_faltante.txt" ),
                "Debe lanzar ProductoFaltanteException si un combo usa un producto que no está en el menú." );
    }

    @Test
    void testIngredientePrecioInvalido( )
    {
        assertThrows( NumberFormatException.class,
                ( ) -> cargarArchivos( PRUEBAS + "ingredientes_precio_invalido.txt", PRUEBAS + "menu_ok.txt", PRUEBAS + "combos_ok.txt" ),
                "Debe lanzar NumberFormatException si el costo no es un número." );
    }

    @Test
    void testMenuPrecioInvalido( )
    {
        assertThrows( NumberFormatException.class,
                ( ) -> cargarArchivos( PRUEBAS + "ingredientes_ok.txt", PRUEBAS + "menu_precio_invalido.txt", PRUEBAS + "combos_ok.txt" ),
                "Debe lanzar NumberFormatException si el precio no es un número." );
    }

    @Test
    void testComboDescuentoInvalido( )
    {
        assertThrows( NumberFormatException.class,
                ( ) -> cargarArchivos( PRUEBAS + "ingredientes_ok.txt", PRUEBAS + "menu_ok.txt", PRUEBAS + "combos_descuento_invalido.txt" ),
                "Debe lanzar NumberFormatException si el descuento no es un número." );
    }

    @Test
    void testArchivoIngredientesInexistente( )
    {
        assertThrows( IOException.class, ( ) -> cargarArchivos( PRUEBAS + "no_existe.txt", PRUEBAS + "menu_ok.txt", PRUEBAS + "combos_ok.txt" ),
                "Debe lanzar IOException si el archivo no existe." );
    }

    @Test
    void testArchivoMenuInexistente( )
    {
        assertThrows( IOException.class, ( ) -> cargarArchivos( PRUEBAS + "ingredientes_ok.txt", PRUEBAS + "no_existe.txt", PRUEBAS + "combos_ok.txt" ),
                "Debe lanzar IOException si el archivo no existe." );
    }

    @Test
    void testArchivoCombosInexistente( )
    {
        assertThrows( IOException.class, ( ) -> cargarArchivos( PRUEBAS + "ingredientes_ok.txt", PRUEBAS + "menu_ok.txt", PRUEBAS + "no_existe.txt" ),
                "Debe lanzar IOException si el archivo no existe." );
    }

    @Test
    void testIniciarPedido( ) throws Exception
    {
        restaurante.iniciarPedido( "Antonio", "Calle 19 # 1-10" );
        Pedido enCurso = restaurante.getPedidoEnCurso( );
        assertNotNull( enCurso, "Debe haber un pedido en curso." );
        assertEquals( "Antonio", enCurso.getNombreCliente( ), "El pedido en curso no es del cliente esperado." );
    }

    @Test
    void testIniciarPedidoConPedidoEnCurso( ) throws Exception
    {
        restaurante.iniciarPedido( "Antonio", "Calle 19 # 1-10" );
        YaHayUnPedidoEnCursoException e = assertThrows( YaHayUnPedidoEnCursoException.class, ( ) -> restaurante.iniciarPedido( "Pablo", "Carrera 7" ),
                "Debe lanzar YaHayUnPedidoEnCursoException." );
        assertTrue( e.getMessage( ).contains( "Antonio" ) && e.getMessage( ).contains( "Pablo" ), "El mensaje debe mencionar a ambos clientes." );
        assertEquals( "Antonio", restaurante.getPedidoEnCurso( ).getNombreCliente( ), "El pedido en curso no debe cambiar." );
    }

    @Test
    void testCerrarSinPedidoEnCurso( )
    {
        assertThrows( NoHayPedidoEnCursoException.class, ( ) -> restaurante.cerrarYGuardarPedido( ), "Debe lanzar NoHayPedidoEnCursoException." );
    }

    @Test
    void testCerrarYGuardarPedido( ) throws Exception
    {
        cargarDatosPrueba( );
        restaurante.iniciarPedido( "Antonio", "Calle 19 # 1-10" );
        Pedido pedido = restaurante.getPedidoEnCurso( );
        pedido.agregarProducto( restaurante.getMenuBase( ).get( 0 ) );
        pedido.agregarProducto( restaurante.getMenuCombos( ).get( 0 ) );
        String facturaEsperada = pedido.generarTextoFactura( );

        File archivo = archivoFactura( pedido );
        restaurante.cerrarYGuardarPedido( );

        assertNull( restaurante.getPedidoEnCurso( ), "Después de cerrar no debe haber pedido en curso." );
        assertTrue( archivo.exists( ), "Se debe crear el archivo factura_<id>.txt en la carpeta de facturas." );
        String contenido = new String( Files.readAllBytes( archivo.toPath( ) ) );
        assertEquals( facturaEsperada, contenido, "El archivo debe contener la factura del pedido." );
    }

    @Test
    void testPedidoCerradoQuedaEnHistorico( ) throws Exception
    {
        restaurante.iniciarPedido( "Antonio", "Calle 19 # 1-10" );
        Pedido pedido = restaurante.getPedidoEnCurso( );
        archivoFactura( pedido );
        restaurante.cerrarYGuardarPedido( );

        assertEquals( 1, restaurante.getPedidos( ).size( ), "El pedido cerrado debe quedar en la lista de pedidos." );
        assertSame( pedido, restaurante.getPedidos( ).get( 0 ), "El pedido guardado no es el que se cerró." );
    }

    @Test
    void testVariosPedidosSeguidos( ) throws Exception
    {
        restaurante.iniciarPedido( "Antonio", "Calle 19" );
        Pedido p1 = restaurante.getPedidoEnCurso( );
        archivoFactura( p1 );
        restaurante.cerrarYGuardarPedido( );

        restaurante.iniciarPedido( "Pablo", "Carrera 7" );
        Pedido p2 = restaurante.getPedidoEnCurso( );
        archivoFactura( p2 );
        restaurante.cerrarYGuardarPedido( );

        assertEquals( 2, restaurante.getPedidos( ).size( ), "Deben quedar dos pedidos en el histórico." );
        assertNotEquals( p1.getIdPedido( ), p2.getIdPedido( ), "Los pedidos deben tener identificadores distintos." );
        assertTrue( archivoFactura( p1 ).exists( ) && archivoFactura( p2 ).exists( ), "Cada pedido debe tener su propia factura." );
    }
}
