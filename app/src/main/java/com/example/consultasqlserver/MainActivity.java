package com.example.consultasqlserver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    private EditText et_identificador;
    private EditText et_producto;
    private EditText et_precio;

    private Button btn_consultar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_identificador = findViewById(R.id.edt_Identificador);
        et_producto = findViewById(R.id.edt_Producto);
        et_precio = findViewById(R.id.edt_precio);

        btn_consultar = findViewById(R.id.btn_Consulta);
    }

    public void clickconsultarProducto(View parametro){
        consultaProducto();
    }

    public void clickInsertarProducto(View parametro){
        insertaProducto();
    }

    private void consultaProducto(){
        try {
            Statement stm = conexionBD().createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Producto WHERE id = "+et_identificador.getText().toString());
            if (rs.next()){
                et_producto.setText(rs.getString(2));
                et_precio.setText(rs.getString(3));
            }
            et_identificador.setText("");
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void insertaProducto(){
        try {
            String producto = et_producto.getText().toString();
            Float precio = Float.parseFloat(et_precio.getText().toString());
            //PreparedStatement pst = conexionBD().prepareStatement("INSERT INTO Producto(nombre,precio) values('"+producto+"',"+precio+")");
            PreparedStatement pst = conexionBD().prepareStatement("INSERT INTO Producto values(?,?)");
            pst.setString(1,producto);
            pst.setFloat(2,precio);
            pst.executeUpdate();

            Toast.makeText(getApplicationContext(), "Registro agregado", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public Connection conexionBD(){
        Connection conexion=null;
        try {
            //registramos permiso de acceso
            StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);

            //conectamos nuestra aplicacion
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conexion = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.1.7;databaseName=Facturacion;user=sa;password=Liguista_1");
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return conexion;
    }
}