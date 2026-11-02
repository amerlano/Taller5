package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoAjustadoTest
{
    private ProductoMenu corral;
    private ProductoAjustado ajustado;
    private Ingrediente queso;
    private Ingrediente tocineta;
    private Ingrediente tomate;

    @BeforeEach
    void setUp( ) throws Exception
    {
        corral = new ProductoMenu( "corral", 14000 );
        ajustado = new ProductoAjustado( corral );
        queso = new Ingrediente( "queso americano", 2500 );
        tocineta = new Ingrediente( "tocineta express", 2500 );
        tomate = new Ingrediente( "tomate", 1000 );
    }

    @AfterEach
    void tearDown( ) throws Exception
    {
    }

    @Test
    void testGetNombre( )
    {
        assertEquals( "corral", ajustado.getNombre( ), "El nombre debe ser el del producto base." );
    }

    @Test
    void testGetPrecioSinAjustes( )
    {
        assertEquals( 14000, ajustado.getPrecio( ), "Sin ajustes, el precio debe ser el del producto base." );
    }

    @Test
    void testGetPrecioConAgregados( )
    {
        ajustado.agregarIngrediente( queso );
        ajustado.agregarIngrediente( tocineta );
        assertEquals( 14000 + 2500 + 2500, ajustado.getPrecio( ), "El precio debe sumar el costo de los ingredientes agregados." );
    }

    @Test
    void testGetPrecioIngredienteRepetido( )
    {
        ajustado.agregarIngrediente( queso );
        ajustado.agregarIngrediente( queso );
        assertEquals( 14000 + 2500 * 2, ajustado.getPrecio( ), "Cada adición del mismo ingrediente debe cobrarse." );
    }

    @Test
    void testGetPrecioConEliminados( )
    {
        ajustado.eliminarIngrediente( tomate );
        assertEquals( 14000, ajustado.getPrecio( ), "Eliminar ingredientes no debe cambiar el precio." );
    }

    @Test
    void testGetPrecioAgregadosYEliminados( )
    {
        ajustado.agregarIngrediente( queso );
        ajustado.eliminarIngrediente( tomate );
        assertEquals( 14000 + 2500, ajustado.getPrecio( ), "Sólo los agregados deben sumarse al precio." );
    }

    @Test
    void testNoModificaProductoBase( )
    {
        ajustado.agregarIngrediente( queso );
        assertEquals( 14000, corral.getPrecio( ), "Ajustar un producto no debe modificar el producto base del menú." );
    }

    @Test
    void testGenerarTextoFacturaSinAjustes( )
    {
        String esperado = "corral\n" + "            14000\n";
        assertEquals( esperado, ajustado.generarTextoFactura( ), "El texto de la factura no es el esperado." );
    }

    @Test
    void testGenerarTextoFacturaConAjustes( )
    {
        ajustado.agregarIngrediente( queso );
        ajustado.eliminarIngrediente( tomate );

        String esperado = "corral\n" + "    +queso americano                2500\n" + "    -tomate\n" + "            16500\n";
        assertEquals( esperado, ajustado.generarTextoFactura( ), "El texto de la factura no es el esperado." );
    }

    @Test
    void testGenerarTextoFacturaNoUsaToString( )
    {
        String texto = ajustado.generarTextoFactura( );
        assertFalse( texto.contains( "ProductoMenu@" ), "La factura no debe mostrar el toString del objeto." );
        assertTrue( texto.startsWith( "corral" ), "La factura debe empezar con el nombre del producto." );
    }
}
