-- 1. Os últimos 3 comentários introduzidos para um vídeo;
SELECT * FROM commentVideo WHERE videoId = 4 LIMIT 3;

-- 2. Lista das tags de determinado vídeo;
SELECT tags FROM video WHERE id = 6;

-- 3. Todos os vídeos com a tag Aveiro;
-- It is only possible if we create an index on the tags field
SELECT * FROM video WHERE tags CONTAINS 'Aveiro';

-- 4. Os últimos 5 eventos de determinado vídeo realizados por um utilizador;
SELECT * FROM event WHERE videoId = 3 AND username = 'emily_wilson' LIMIT 5;

-- 5. Vídeos partilhados por determinado utilizador (maria1987, por exemplo) num determinado período de tempo (Agosto de 2017, por exemplo);
-- It uses ALLOW FILTERING
SELECT * FROM video WHERE author = 'alex_123' AND uploadTime > '2023-11-01' AND uploadTime < '2023-11-30' ALLOW FILTERING;

-- 6. Os últimos 10 vídeos, ordenado inversamente pela data da partilhada;
-- Não é possível, uma vez que para ordenar necessita que a Partion Key seja restrita (por EQ ou IN)

-- 7. Todos os seguidores (followers) de determinado vídeo;
SELECT * FROM follower WHERE videoId = 1;

-- 8. Todos os comentários (dos vídeos) que determinado utilizador está a seguir (following);
-- Não é possível, uma vez que seria necessário fazer um JOIN entre a tabela follower e commentVideo, o que não é possível

-- 9. Os 5 vídeos com maior rating;
-- Não é possível, uma vez que para ordenar o rating seria necessário que este fosse a Clustering Key, e mesmo assim, teriamos de restringir com a Partition Key.

-- 10. Uma query que retorne todos os vídeos e que mostre claramente a forma pela qual estão ordenados;
-- Não é possível, mais uma vez, é necessário que a Partition Key seja restrita (por EQ ou IN)

-- 11. Lista com as Tags existentes e o número de vídeos catalogados com cada uma delas;
-- Seria algo como: SELECT tags, COUNT(*) FROM video GROUP BY tags;, no entanto, não é possível, uma vez que GROUP BY apenas funciona com a Partition Key.

-- 12. Rating médio de um vídeo e quantos votos teve;
SELECT videoId, AVG(rating) AS average_rating, COUNT(rating) AS number_of_votes FROM rating where videoid = 1;

-- 13. Lista de todos os eventos de um utilizador num determinado vídeo, ordenados do mais recente para o mais antigo;
SELECT * FROM event WHERE videoId = 1 AND username = 'john_doe' ORDER BY eventTime;


