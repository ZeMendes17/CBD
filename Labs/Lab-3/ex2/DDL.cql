CREATE TABLE IF NOT EXISTS videos.user (
	username text,
	name text,
	email text,
	registryTime timestamp,

	PRIMARY KEY (username)
);

CREATE TABLE IF NOT EXISTS videos.video (
    id int,
	author text,
	name text,
	description text,
	tags set<text>,
	uploadTime timestamp,

    PRIMARY KEY ((id, author), uploadTime)
) WITH CLUSTERING ORDER BY (uploadTime DESC);

CREATE TABLE IF NOT EXISTS videos.comment_user(
	id int,
	author text,
	videoId int,
	content text,
	postTime timestamp,

	PRIMARY KEY ((author), postTime)
) WITH CLUSTERING ORDER BY (postTime DESC);

CREATE TABLE IF NOT EXISTS videos.comment_video(
	id int,
	author text,
	videoId int,
	content text,
	postTime timestamp,

	PRIMARY KEY ((videoId), postTime)
) WITH CLUSTERING ORDER BY (postTime DESC);

CREATE TABLE IF NOT EXISTS videos.follower (
	videoId int,
	users set<text>,

    PRIMARY KEY (videoId)
);

CREATE TABLE IF NOT EXISTS videos.event (
	videoId int,
	author text,
	eventType text,
	eventTime timestamp,
	moment timestamp,

	PRIMARY KEY ((videoId, author), eventTime)
);

CREATE TABLE IF NOT EXISTS videos.rating (
	id int,
	videoId int,
	rate int,

	PRIMARY KEY ((videoId), rate)
);


-- 7. Permitir a pesquisa de todos os vídeos de determinado autor;
CREATE INDEX IF NOT EXISTS ON video (author);

-- 8. Permitir a pesquisa de comentários por utilizador, ordenado inversamente pela data;
-- 9. Permitir a pesquisa de comentários por vídeos, ordenado inversamente pela data;
-- Para tal, é necessário criar duas tabelas, uma para cada tipo de pesquisa

-- 10. Permitir a pesquisa do rating médio de um vídeo e quantas vezes foi votado;
-- Exemplo de query:
-- SELECT videoId, AVG(rating) AS average_rating, COUNT(rating) AS number_of_votes FROM rating where videoid = 1;