package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ComboTest
{
    private ProductoMenu corral;
    private ProductoMenu papas;
    private ProductoMenu gaseosa;
    private ArrayList<ProductoMenu> items;
    private Combo combo;

    @BeforeEach
    void setUp( ) throws Exception
    {
        corral = new ProductoMenu( "corral", 14000 );
        papas = new ProductoMenu( "papas medianas", 5500 );
        gaseosa = new ProductoMenu( "gaseosa", 5000 );

        items = new ArrayList<ProductoMenu>( );
        items.add( corral );
        items.add( papas );
        items.add( gaseosa );

        combo = new Combo( "combo corral", 0.10, items );
    }

    @AfterEach
    void tearDown( ) throws Exception
    {
    }

    @Test
    void testGetNombre( )
    {
        assertEquals( "combo corral", combo.getNombre( ), "El nombre del combo no es el esperado." );
    }

    @Test
    void testGetPrecio( )
    {
        assertEquals( 22050, combo.getPrecio( ), "El precio debe ser la suma de los productos menos el descuento." );
    }

    @Test
    void testGetPrecioSinDescuento( )
    {
        Combo sinDescuento = new Combo( "sin descuento", 0, items );
        assertEquals( 24500, sinDescuento.getPrecio( ), "Con descuento 0 el precio debe ser la suma de los productos." );
    }

    @Test
    void testGetPrecioDescuentoTotal( )
    {
        Combo gratis = new Combo( "gratis", 1, items );
        assertEquals( 0, gratis.getPrecio( ), "Con descuento del 100% el precio debe ser 0." );
    }

    @Test
    void testGetPrecioDescuentoDecimal( )
    {
        ArrayList<ProductoMenu> itemsEspecial = new ArrayList<ProductoMenu>( );
        itemsEspecial.add( new ProductoMenu( "especial", 24000 ) );
        itemsEspecial.add( papas );
        itemsEspecial.add( gaseosa );
        Combo especial = new Combo( "combo especial", 0.095, itemsEspecial );
        assertEquals( 31222, especial.getPrecio( ), "El precio con descuento decimal no es el esperado." );
    }

    @Test
    void testGetPrecioComboVacio( )
    {
        Combo vacio = new Combo( "vacio", 0.10, new ArrayList<ProductoMenu>( ) );
        assertEquals( 0, vacio.getPrecio( ), "Un combo sin productos debe costar 0." );
    }

    @Test
    void testCopiaDeLaLista( )
    {
        items.add( new ProductoMenu( "papas grandes", 6900 ) );
        assertEquals( 22050, combo.getPrecio( ), "El combo no debe verse afectado por cambios en la lista original." );
    }

    @Test
    void testGenerarTextoFactura( )
    {
        String esperado = "Combo combo corral\n" + " Descuento: 0.1\n" + "            22050\n";
        assertEquals( esperado, combo.generarTextoFactura( ), "El texto de la factura no es el esperado." );
    }
}
