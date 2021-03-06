DATABASE=CBS
URL=XXXX.azure.com
USER=ZZZZ@YYYY
PASSWORD=WWWWWW

mysqladmin -h $URL --user=$USER --password=$PASSWORD -f drop $DATABASE
mysqladmin -h $URL --user=$USER --password=$PASSWORD create $DATABASE

# Crea las distintas tablas 

mysql -h $URL --user=$USER --password=$PASSWORD -D $DATABASE -e "DROP TABLE pedidos;"
mysql -h $URL --user=$USER --password=$PASSWORD -D $DATABASE -e "DROP TABLE productos;"
mysql -h $URL --user=$USER --password=$PASSWORD -D $DATABASE -e "DROP TABLE proveedores;"
mysql -h $URL --user=$USER --password=$PASSWORD -D $DATABASE -e "DROP TABLE clientes;"


mysql -h $URL --user=$USER --password=$PASSWORD -D $DATABASE -e "CREATE TABLE proveedores (id INT(10) NOT NULL PRIMARY KEY, nombre VARCHAR(50), pais VARCHAR(30), telefono INT(9)) ENGINE=INNODB;"


mysql -h $URL --user=$USER --password=$PASSWORD -D $DATABASE -e"CREATE TABLE productos (id INT(10) NOT NULL PRIMARY KEY, nombre VARCHAR(50), proveedor INT(10) REFERENCES proveedores(id), precio INT(10), descuento INT(5)) ENGINE=INNODB;"


mysql -h $URL --user=$USER --password=$PASSWORD -D $DATABASE -e"CREATE TABLE clientes (id INT(10) NOT NULL PRIMARY KEY, nombre VARCHAR(50), apellido VARCHAR(50), password VARCHAR(50), empresa VARCHAR(50), telefono INT(9), pedidos_pendientes INT(10) ) ENGINE=INNODB;"


mysql -h $URL --user=$USER --password=$PASSWORD -D $DATABASE -e"CREATE TABLE pedidos (order_number INT(10) NOT NULL PRIMARY KEY auto_increment, cliente INT(10), FOREIGN KEY (cliente) REFERENCES clientes(id) ON UPDATE CASCADE ON DELETE RESTRICT, producto INT(10), FOREIGN KEY (producto) REFERENCES productos(id) ON UPDATE CASCADE ON DELETE RESTRICT, proveedor INT(10), FOREIGN KEY (proveedor) REFERENCES proveedores(id) ON UPDATE CASCADE ON DELETE RESTRICT, cantidad INT(3), descuento INT(3), fecha_entrada DATE, fecha_salida DATE) ENGINE=INNODB;"

echo " ... Proveedores"
java GeneraProveedores

# Genera la tabla de productos

echo " ... Productos"
java GeneraProductos

# Genera la tabla de clientes
# El parametro es el numero de clientes que se pretende generar
echo "Alimentando la tabla de clientes con $1 entradas"
java GeneraClientes $1


# Creacion de indices
echo "Creando indice en clientes(apellido)"
mysql -h $URL --user=$USER --password=$PASSWORD -D $DATABASE -e "CREATE INDEX ind_apellido ON clientes(apellido);"
echo "Creando indice en pedidos(cliente)"
mysql -h $URL --user=$USER --password=$PASSWORD -D $DATABASE -e "CREATE INDEX ind_cliente ON pedidos(cliente);"

# Crea una linea en la tabla de pedidos ...

echo "Anadiendo una linea dummy a pedidos ..."
mysql -h $URL --user=$USER --password=$PASSWORD -D $DATABASE -e "INSERT INTO pedidos VALUES (1,1,1,1,1,1,'1999-12-31','2000-1-1');"
# Como le hemos asignado un pedido dummy al cliente con id=1, actualizamos
# el campo correspondiente en la tabla de clientes

mysql -h $URL --user=$USER --password=$PASSWORD -D $DATABASE -e "UPDATE clientes SET pedidos_pendientes=1 WHERE id=1;"
