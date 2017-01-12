# Borra todos los pedidos de la tabla correspondiente

sqlplus<<EOF 
root/hp

DROP INDEX ind_cliente;
DELETE FROM pedidos;
UPDATE clientes SET pedidos_pendientes=0;
INSERT INTO pedidos VALUES (1,1,1,1,1,1,'2000-1-1','2000-1-1');
UPDATE clientes SET pedidos_pendientes=1 WHERE id=1;
CREATE INDEX ind_cliente ON pedidos(cliente);

EOF

