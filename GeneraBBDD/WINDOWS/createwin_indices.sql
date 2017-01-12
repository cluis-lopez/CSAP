ALTER SESSION SET NLS_DATE_FORMAT='YYYY-MM-DD';
INSERT INTO pedidos VALUES (1,1,1,1,1,1,'2000-1-1','2000-1-1');
UPDATE clientes SET pedidos_pendientes=1 WHERE id=1;
CREATE INDEX ind_apellido ON clientes(apellido);
CREATE INDEX ind_cliente ON pedidos(cliente);

