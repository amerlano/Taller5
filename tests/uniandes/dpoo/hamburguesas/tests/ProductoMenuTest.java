package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoMenuTest
{
    private ProductoMenu corral;
    private ProductoMenu gaseosa;

    @BeforeEach
    void setUp( ) throws Exception
    {
        corral = new ProductoMenu( "corral", 14000 );
        gaseosa = new ProductoMenu( "gaseosa", 5000 );
    }

    @AfterEach
    void tearDown( ) throws Exception
    {
    }

    @Test
    void testGetNombre( )
    {
        assertEquals( "corral", corral.getNombre( ), "El nombre del producto no es el esperado." );
        assertEquals( "gaseosa", gaseosa.getNombre( ), "El nombre del producto no es el esperado." );
    }

    @Test
    void testGetPrecio( )
    {
        assertEquals( 14000, corral.getPrecio( ), "El precio del producto no es el esperado." );
        assertEquals( 5000, gaseosa.getPrecio( ), "El precio del producto no es el esperado." );
    }

    @Test
    void testGetPrecioCero( )
    {
        ProductoMenu gratis = new ProductoMenu( "agua", 0 );
        assertEquals( 0, gratis.getPrecio( ), "Un producto con precio 0 debe retornar 0." );
    }

    @Test
    void testGenerarTextoFactura( )
    {
        String esperado = "corral\n" + "            14000\n";
        assertEquals( esperado, corral.generarTextoFactura( ), "El texto de la factura no es el esperado." );
    }

    @Test
    void testGenerarTextoFacturaContieneNombreYPrecio( )
    {
        String texto = gaseosa.generarTextoFactura( );
        assertTrue( texto.contains( "gaseosa" ), "La factura debe incluir el nombre del producto." );
        assertTrue( texto.contains( "5000" ), "La factura debe incluir el precio del producto." );
    }
}
