package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class PedidoTest
{
    private Pedido pedido;
    private ProductoMenu corral;
    private ProductoMenu papas;
    private ProductoMenu gaseosa;
    private File archivoTemporal;

    @BeforeEach
    void setUp( ) throws Exception
    {
        pedido = new Pedido( "Antonio", "Calle 19 # 1-10" );
        corral = new ProductoMenu( "corral", 14000 );
        papas = new ProductoMenu( "papas medianas", 5500 );
        gaseosa = new ProductoMenu( "gaseosa", 5000 );
        archivoTemporal = File.createTempFile( "factura_prueba", ".txt" );
    }

    @AfterEach
    void tearDown( ) throws Exception
    {
        archivoTemporal.delete( );
    }

    private void llenarPedido( )
    {
        pedido.agregarProducto( corral );

        ProductoAjustado ajustado = new ProductoAjustado( corral );
        ajustado.agregarIngrediente( new Ingrediente( "queso americano", 2500 ) );
        pedido.agregarProducto( ajustado );

        ArrayList<ProductoMenu> items = new ArrayList<ProductoMenu>( );
        items.add( corral );
        items.add( papas );
        items.add( gaseosa );
        pedido.agregarProducto( new Combo( "combo corral", 0.10, items ) );
    }

    @Test
    void testGetNombreCliente( )
    {
        assertEquals( "Antonio", pedido.getNombreCliente( ), "El nombre del cliente no es el esperado." );
    }

    @Test
    void testIdPedidoConsecutivo( )
    {
        Pedido otro = new Pedido( "Pablo", "Carrera 7" );
        assertEquals( pedido.getIdPedido( ) + 1, otro.getIdPedido( ), "Los identificadores deben ser consecutivos." );
    }

    @Test
    void testIdPedidoUnico( )
    {
        Pedido otro = new Pedido( "Pablo", "Carrera 7" );
        assertNotEquals( pedido.getIdPedido( ), otro.getIdPedido( ), "Dos pedidos no pueden tener el mismo identificador." );
    }

    @Test
    void testPrecioTotalPedidoVacio( )
    {
        assertEquals( 0, pedido.getPrecioTotalPedido( ), "Un pedido vacío debe costar 0." );
    }

    @Test
    void testPrecioTotalUnProducto( )
    {
        pedido.agregarProducto( corral );
        assertEquals( 16660, pedido.getPrecioTotalPedido( ), "El precio total debe incluir el IVA." );
    }

    @Test
    void testPrecioTotalVariosProductos( )
    {
        llenarPedido( );
        assertEquals( 62534, pedido.getPrecioTotalPedido( ), "El precio total del pedido no es el esperado." );
    }

    @Test
    void testGenerarTextoFacturaVacio( )
    {
        String esperado = "Cliente: Antonio\n" + "Dirección: Calle 19 # 1-10\n" + "----------------\n" + "----------------\n" + "Precio Neto:  0\n" + "IVA:          0\n"
                + "Precio Total: 0\n";
        assertEquals( esperado, pedido.generarTextoFactura( ), "La factura de un pedido vacío no es la esperada." );
    }

    @Test
    void testGenerarTextoFactura( )
    {
        llenarPedido( );
        String esperado = "Cliente: Antonio\n" + "Dirección: Calle 19 # 1-10\n" + "----------------\n"
                + "corral\n" + "            14000\n"
                + "corral\n" + "    +queso americano                2500\n" + "            16500\n"
                + "Combo combo corral\n" + " Descuento: 0.1\n" + "            22050\n"
                + "----------------\n" + "Precio Neto:  52550\n" + "IVA:          9984\n" + "Precio Total: 62534\n";
        assertEquals( esperado, pedido.generarTextoFactura( ), "La factura del pedido no es la esperada." );
    }

    @Test
    void testGuardarFactura( ) throws Exception
    {
        llenarPedido( );
        pedido.guardarFactura( archivoTemporal );

        assertTrue( archivoTemporal.exists( ), "El archivo de la factura debe existir." );
        String contenido = new String( Files.readAllBytes( archivoTemporal.toPath( ) ) );
        assertEquals( pedido.generarTextoFactura( ), contenido, "El archivo debe contener exactamente el texto de la factura." );
    }

    @Test
    void testGuardarFacturaSobrescribe( ) throws Exception
    {
        pedido.guardarFactura( archivoTemporal );
        pedido.agregarProducto( corral );
        pedido.guardarFactura( archivoTemporal );

        String contenido = new String( Files.readAllBytes( archivoTemporal.toPath( ) ) );
        assertEquals( pedido.generarTextoFactura( ), contenido, "Guardar de nuevo debe reemplazar el contenido anterior." );
    }

    @Test
    void testGuardarFacturaArchivoInvalido( )
    {
        File invalido = new File( "./carpeta_que_no_existe_xyz/factura.txt" );
        assertThrows( FileNotFoundException.class, ( ) -> pedido.guardarFactura( invalido ), "Debe lanzar FileNotFoundException si no se puede crear el archivo." );
    }
}
