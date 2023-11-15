CREATE TABLE IF NOT EXISTS user (
	username text,
	name text,
	email text,
	registTime timestamp,

	PRIMARY KEY (username)
);

CREATE TABLE IF NOT EXISTS video (
	id int,
	author text,
	name text,
	description text,
	tags set<text>,
	uploadTime timestamp,
	
	PRIMARY KEY (id, author, uploadTime)
);

CREATE TABLE IF NOT EXISTS commentUser(
	id int,
	author text,
	videoId int,
	content text,
	postTime timestamp,

	PRIMARY KEY ((author), postTime)
) WITH CLUSTERING ORDER BY (postTime DESC);

CREATE TABLE IF NOT EXISTS commentVideo(
	id int,
	author text,
	videoId int,
	content text,
	postTime timestamp,

	PRIMARY KEY ((videoId), postTime)
) WITH CLUSTERING ORDER BY (postTime DESC);

CREATE TABLE IF NOT EXISTS follower (
	videoId int,
	username text,

	PRIMARY KEY (videoId, username)
);

CREATE TABLE IF NOT EXISTS event (
	videoId int,
	username text,
	eventType text,
	eventTime timestamp,
	moment int,

	PRIMARY KEY ((videoId, username), eventTime)
);

CREATE TABLE IF NOT EXISTS rating (
	id int,
	videoId int,
	rating int,

	PRIMARY KEY (videoId, id)
);


-- 7. Permitir a pesquisa de todos os vídeos de determinado autor;
CREATE INDEX IF NOT EXISTS ON video (author);

-- 8. Permitir a pesquisa de comentários por utilizador, ordenado inversamente pela data;
-- 9. Permitir a pesquisa de comentários por vídeos, ordenado inversamente pela data;
-- Para tal, é necessário criar duas tabelas, uma para cada tipo de pesquisa

-- 10. Permitir a pesquisa do rating médio de um vídeo e quantas vezes foi votado;
-- Exemplo de query:
-- SELECT videoId, AVG(rating) AS average_rating, COUNT(rating) AS number_of_votes FROM rating where videoid = 1;