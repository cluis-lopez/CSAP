# Genera las tablas 

sqlplus<<EOF 
root/hp
DROP SEQUENCE pedidos_seq;
DROP TRIGGER pedidos_trig;
DROP TABLE pedidos;
DROP TABLE productos;
DROP TABLE proveedores;
DROP TABLE clientes;

CREATE TABLE proveedores (id NUMBER(10) PRIMARY KEY, nombre VARCHAR(50), pais VARCHAR(30), telefono NUMBER(9));

CREATE TABLE productos (id NUMBER(10) PRIMARY KEY, nombre VARCHAR(50), proveedor NUMBER(10) REFERENCES proveedores(id), precio NUMBER(10), descuento NUMBER(5));

CREATE TABLE clientes (id NUMBER(10) PRIMARY KEY, nombre VARCHAR(50), apellido VARCHAR(50), password VARCHAR(50), empresa VARCHAR(50), telefono NUMBER(9), pedidos_pendientes NUMBER(10));

CREATE TABLE pedidos (order_number NUMBER(10) PRIMARY KEY, cliente NUMBER(10) REFERENCES clientes(id), producto NUMBER(10) REFERENCES productos(id), proveedor NUMBER(10) REFERENCES proveedores(id), cantidad NUMBER(3), descuento NUMBER(3), fecha_entrada DATE, fecha_salida DATE);

CREATE SEQUENCE pedidos_seq START WITH 1 INCREMENT BY 1 NOMAXVALUE;

CREATE TRIGGER pedidos_trig BEFORE INSERT ON pedidos
FOR EACH ROW
BEGIN
SELECT pedidos_seq.nextval INTO :new.order_number FROM dual;
END;
/

EOF

echo "Alimentando las tablas ..."
echo " ... Proveedores"

java GeneraProveedores

echo " ... Productos"

java GeneraProductos

# Alimenta la tabla de clientes
# El parametro es el numero de clientes que se pretende generar
echo "Alimentando la tabla de clientes con $1 entradas"
java GeneraClientes $1

# Creacion de indices

echo "Creando indices en clientes(apellido) y en pedidos(cliente)"

sqlplus <<EOF
root/hp
INSERT INTO pedidos VALUES (1,1,1,1,1,1,'2000-1-1','2000-1-1');
UPDATE clientes SET pedidos_pendientes=1 WHERE id=1;
CREATE INDEX ind_apellido ON clientes(apellido);
CREATE INDEX ind_cliente ON pedidos(cliente);
EOF
