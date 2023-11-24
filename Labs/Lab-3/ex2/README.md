# Exercício 2

Implementar uma base de dados que suporte um sistema de partilha de
vídeos utilizando a linguagem CQL (Cassandra Query Language).

## Alínea a)

### Criar um keyspace
    
```cql
CREATE KEYSPACE IF NOT EXISTS videos WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 3};
```

### Criar as tabelas necessárias para suportar os requisitos

#### Gestão de utilizadores

```cql
CREATE TABLE IF NOT EXISTS videos.user (
    username text,
	name text,
	email text,
	registryTime timestamp,

	PRIMARY KEY (username)
);
```

#### Gestão de vídeos

```cql
CREATE TABLE IF NOT EXISTS videos.video (
    id int,
	author text,
	name text,
	description text,
	tags set<text>,
	uploadTime timestamp,

    PRIMARY KEY ((id, author), uploadTime)
) WITH CLUSTERING ORDER BY (uploadTime DESC);
```

#### Gestão de comentários

```cql
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
```

#### Gestão de vídeo followers

```cql
CREATE TABLE IF NOT EXISTS videos.follower (
	videoId int,
	users set<text>,

    PRIMARY KEY (videoId)
);
```

#### Registo de eventos

```cql
CREATE TABLE IF NOT EXISTS videos.event (
	videoId int,
	author text,
	eventType text,
	eventTime timestamp,
	moment timestamp,

	PRIMARY KEY ((videoId, author), eventTime)
);
```

#### Rating de vídeos

```cql
CREATE TABLE IF NOT EXISTS videos.rating (
	id int,
	videoId int,
	rate int,

	PRIMARY KEY ((videoId), rate)
);
```

## Alínea b)

#### Inserir dados na tabela user

```cql
INSERT INTO videos.user (username, name, email, registryTime) VALUES ('john_doe', 'John Doe', 'john@example.com', toTimestamp(now()));
INSERT INTO videos.user (username, name, email, registryTime) VALUES ('jane_smith', 'Jane Smith', 'jane@example.com', toTimestamp(now()));
INSERT INTO videos.user (username, name, email, registryTime) VALUES ('alex_123', 'Alex Johnson', 'alex@example.com', toTimestamp(now()));
INSERT INTO videos.user (username, name, email, registryTime) VALUES ('emily_wilson', 'Emily Wilson', 'emily@example.com', toTimestamp(now()));
INSERT INTO videos.user (username, name, email, registryTime) VALUES ('michael_jones', 'Michael Jones', 'michael@example.com', toTimestamp(now()));
INSERT INTO videos.user (username, name, email, registryTime) VALUES ('sara_brown', 'Sara Brown', 'sara@example.com', toTimestamp(now()));
INSERT INTO videos.user (username, name, email, registryTime) VALUES ('david_roberts', 'David Roberts', 'david@example.com', toTimestamp(now()));
INSERT INTO videos.user (username, name, email, registryTime) VALUES ('olivia_smith', 'Olivia Smith', 'olivia@example.com', toTimestamp(now()));
INSERT INTO videos.user (username, name, email, registryTime) VALUES ('sam_wilson', 'Sam Wilson', 'sam@example.com', toTimestamp(now()));
INSERT INTO videos.user (username, name, email, registryTime) VALUES ('lucas_baker', 'Lucas Baker', 'lucas@example.com', toTimestamp(now()));
```

#### Inserir dados na tabela video

```cql
INSERT INTO videos.video (id, author, name, description, tags, uploadTime) VALUES (1, 'john_doe', 'First Video by John', 'My first video', {'vlog', 'fun'}, toTimestamp(now()));
INSERT INTO videos.video (id, author, name, description, tags, uploadTime) VALUES (2, 'john_doe', 'Cooking Tutorial', 'Learn how to cook a delicious meal', {'cooking', 'tutorial'}, toTimestamp(now()));
INSERT INTO videos.video (id, author, name, description, tags, uploadTime) VALUES (3, 'jane_smith', 'Fitness Tips', 'Get fit with these workout tips', {'fitness', 'health'}, toTimestamp(now()));
INSERT INTO videos.video (id, author, name, description, tags, uploadTime) VALUES (4, 'jane_smith', 'Travel Vlog', 'Exploring new places around the world', {'travel', 'vlog'}, toTimestamp(now()));
INSERT INTO videos.video (id, author, name, description, tags, uploadTime) VALUES (5, 'alex_123', 'Gaming Highlights', 'Exciting gaming moments', {'gaming', 'highlights'}, toTimestamp(now()));
INSERT INTO videos.video (id, author, name, description, tags, uploadTime) VALUES (6, 'alex_123', 'Programming Tutorial', 'Learn programming concepts', {'programming', 'tutorial'}, toTimestamp(now()));
INSERT INTO videos.video (id, author, name, description, tags, uploadTime) VALUES (7, 'emily_wilson', 'Fashion Tips', 'Latest fashion trends and tips', {'fashion', 'style'}, toTimestamp(now()));
INSERT INTO videos.video (id, author, name, description, tags, uploadTime) VALUES (8, 'emily_wilson', 'Art Showcase', 'Showcasing amazing artworks', {'art', 'showcase'}, toTimestamp(now()));
INSERT INTO videos.video (id, author, name, description, tags, uploadTime) VALUES (9, 'michael_jones', 'Science Experiments', 'Fun and interesting science experiments', {'science', 'experiments'}, toTimestamp(now()));
INSERT INTO videos.video (id, author, name, description, tags, uploadTime) VALUES (10, 'michael_jones', 'Pet Care Tips', 'Taking care of your pets', {'pets', 'care'}, toTimestamp(now()));
INSERT INTO videos.video (id, author, name, description, tags, uploadTime) VALUES (11, 'sara_brown', 'Aveiro Vlog', 'Exploring Aveiro', {'Aveiro', 'vlog'}, toTimestamp(now()));
INSERT INTO videos.video (id, author, name, description, tags, uploadTime) VALUES (12, 'sara_brown', 'Ovos Moles', 'Tasting traditional Aveiro food', {'food', 'Aveiro'}, toTimestamp(now()));
```

#### Inserir dados nas tabelas de comentários (comment_user e comment_video)

```cql
INSERT INTO videos.comment_user (id, author, videoId, content, postTime) VALUES (1, 'jane_smith', 1, 'Great video!', toTimestamp(now()));
INSERT INTO videos.comment_user (id, author, videoId, content, postTime) VALUES (2, 'alex_123', 1, 'Lowkey Bad.', toTimestamp(now()));
INSERT INTO videos.comment_user (id, author, videoId, content, postTime) VALUES (3, 'emily_wilson', 2, 'Awesome video!', toTimestamp(now()));
INSERT INTO videos.comment_user (id, author, videoId, content, postTime) VALUES (4, 'michael_jones', 3, 'Amazing video!', toTimestamp(now()));
INSERT INTO videos.comment_user (id, author, videoId, content, postTime) VALUES (5, 'sara_brown', 4, 'Cool video!', toTimestamp(now()));
INSERT INTO videos.comment_user (id, author, videoId, content, postTime) VALUES (6, 'david_roberts', 6, 'Meh', toTimestamp(now()));
INSERT INTO videos.comment_user (id, author, videoId, content, postTime) VALUES (7, 'jane_smith', 7, 'Nice video!', toTimestamp(now()));
INSERT INTO videos.comment_user (id, author, videoId, content, postTime) VALUES (8, 'john_doe', 8, 'My Art is Better!', toTimestamp(now()));
INSERT INTO videos.comment_user (id, author, videoId, content, postTime) VALUES (9, 'alex_123', 10, 'Coooool', toTimestamp(now()));
INSERT INTO videos.comment_user (id, author, videoId, content, postTime) VALUES (10, 'emily_wilson', 4, 'Great!', toTimestamp(now()));
INSERT INTO videos.comment_user (id, author, videoId, content, postTime) VALUES (11, 'john_doe', 4, 'Mid', toTimestamp(now()));

INSERT INTO videos.comment_video (id, author, videoId, content, postTime) VALUES (1, 'jane_smith', 1, 'Great video!', toTimestamp(now()));
INSERT INTO videos.comment_video (id, author, videoId, content, postTime) VALUES (2, 'alex_123', 1, 'Lowkey Bad.', toTimestamp(now()));
INSERT INTO videos.comment_video (id, author, videoId, content, postTime) VALUES (3, 'emily_wilson', 2, 'Awesome video!', toTimestamp(now()));
INSERT INTO videos.comment_video (id, author, videoId, content, postTime) VALUES (4, 'michael_jones', 3, 'Amazing video!', toTimestamp(now()));
INSERT INTO videos.comment_video (id, author, videoId, content, postTime) VALUES (5, 'sara_brown', 4, 'Cool video!', toTimestamp(now()));
INSERT INTO videos.comment_video (id, author, videoId, content, postTime) VALUES (6, 'david_roberts', 6, 'Meh', toTimestamp(now()));
INSERT INTO videos.comment_video (id, author, videoId, content, postTime) VALUES (7, 'jane_smith', 7, 'Nice video!', toTimestamp(now()));
INSERT INTO videos.comment_video (id, author, videoId, content, postTime) VALUES (8, 'john_doe', 8, 'My Art is Better!', toTimestamp(now()));
INSERT INTO videos.comment_video (id, author, videoId, content, postTime) VALUES (9, 'alex_123', 10, 'Coooool', toTimestamp(now()));
INSERT INTO videos.comment_video (id, author, videoId, content, postTime) VALUES (10, 'emily_wilson', 4, 'Great!', toTimestamp(now()));
INSERT INTO videos.comment_video (id, author, videoId, content, postTime) VALUES (11, 'john_doe', 4, 'Mid', toTimestamp(now()));
```

#### Inserir dados na tabela follower

```cql
INSERT INTO videos.follower (videoId, users) VALUES (1, {'jane_smith', 'alex_123', 'emily_wilson'});
INSERT INTO videos.follower (videoId, users) VALUES (2, {'emily_wilson'});
INSERT INTO videos.follower (videoId, users) VALUES (3, {'john_doe', 'olivia_smith', 'sam_wilson'});
INSERT INTO videos.follower (videoId, users) VALUES (4, {'alex_123', 'david_roberts', 'sara_brown'});
INSERT INTO videos.follower (videoId, users) VALUES (5, {'john_doe', 'emily_wilson', 'sam_wilson'});
INSERT INTO videos.follower (videoId, users) VALUES (6, {'lucas_baker', 'olivia_smith', 'david_roberts'});
INSERT INTO videos.follower (videoId, users) VALUES (7, {'john_doe', 'jane_smith'});
INSERT INTO videos.follower (videoId, users) VALUES (8, {'michael_jones', 'sara_brown'});
INSERT INTO videos.follower (videoId, users) VALUES (9, {'david_roberts'});
INSERT INTO videos.follower (videoId, users) VALUES (10, {'john_doe', 'jane_smith', 'alex_123', 'sara_brown'});
```

#### Inserir dados na tabela event

```cql
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (1, 'jane_smith', 'play', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (1, 'jane_smith', 'pause', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (8, 'alex_123', 'play', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (8, 'alex_123', 'stop', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (3, 'emily_wilson', 'play', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (3, 'emily_wilson', 'pause', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (3, 'emily_wilson', 'stop', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (3, 'emily_wilson', 'play', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (3, 'emily_wilson', 'stop', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (5, 'sara_brown', 'play', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (5, 'sara_brown', 'pause', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (5, 'sara_brown', 'play', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (5, 'sara_brown', 'stop', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (6, 'david_roberts', 'play', toTimestamp(now()), toTimestamp(now()));
INSERT INTO videos.event (videoId, author, eventType, eventTime, moment) VALUES (6, 'david_roberts', 'pause', toTimestamp(now()), toTimestamp(now()));
```

#### Inserir dados na tabela rating

```cql
INSERT INTO videos.rating (id, videoId, rate) VALUES (1, 1, 5);
INSERT INTO videos.rating (id, videoId, rate) VALUES (2, 1, 4);
INSERT INTO videos.rating (id, videoId, rate) VALUES (3, 2, 4);
INSERT INTO videos.rating (id, videoId, rate) VALUES (4, 2, 3);
INSERT INTO videos.rating (id, videoId, rate) VALUES (5, 3, 4);
INSERT INTO videos.rating (id, videoId, rate) VALUES (6, 3, 5);
INSERT INTO videos.rating (id, videoId, rate) VALUES (7, 9, 3);
INSERT INTO videos.rating (id, videoId, rate) VALUES (8, 9, 2);
INSERT INTO videos.rating (id, videoId, rate) VALUES (9, 6, 5);
INSERT INTO videos.rating (id, videoId, rate) VALUES (10, 5, 4);
INSERT INTO videos.rating (id, videoId, rate) VALUES (11, 1, 3);
INSERT INTO videos.rating (id, videoId, rate) VALUES (12, 10, 5);
INSERT INTO videos.rating (id, videoId, rate) VALUES (13, 10, 4);
INSERT INTO videos.rating (id, videoId, rate) VALUES (14, 7, 3);
INSERT INTO videos.rating (id, videoId, rate) VALUES (15, 6, 4);
```

### Registos em formato JSON

Para obter os registos em formato JSON para cada tabela, basta executar o
seguinte comando:

```cql
SELECT JSON * FROM videos.<table_name>;
```

## Alínea c)

Para demonstrar que que as pesquisas estão a funcionar corretamente,
temos os seguintes exemplos:

#### 7. Permitir a pesquisa de todos os vídeos de determinado autor

É necessário criar um índice na tabela video para a coluna author:

```cql
CREATE INDEX IF NOT EXISTS ON videos.video (author);
```

Assim, podemos executar a pesquisa:

```cql
SELECT * FROM videos.video WHERE author='jane_smith';
```

```
 id | author     | uploadtime                      | description                           | name         | tags
----+------------+---------------------------------+---------------------------------------+--------------+-----------------------
  4 | jane_smith | 2023-11-24 01:06:12.794000+0000 | Exploring new places around the world |  Travel Vlog |    {'travel', 'vlog'}
  3 | jane_smith | 2023-11-24 01:06:12.764000+0000 |       Get fit with these workout tips | Fitness Tips | {'fitness', 'health'}
```

#### 8. Permitir a pesquisa de comentários por utilizador, ordenado inversamente pela data

Uma vez que a tabela dos comentários já se econtra dividida em duas tabelas, podemos usar a tabela que possui o autor do comentário como PARTITION KEY, comment_user.
    
```cql
SELECT * FROM videos.comment_user WHERE author='john_doe';
```

```
  author   | posttime                        | content           | id | videoid
----------+---------------------------------+-------------------+----+---------
 john_doe | 2023-11-24 01:06:13.336000+0000 |               Mid | 11 |       4
 john_doe | 2023-11-24 01:06:13.267000+0000 | My Art is Better! |  8 |       8
```

Podemos verificar que os comentários estão ordenados inversamente pela data.

#### 9. Permitir a pesquisa de comentários por vídeos, ordenado inversamente pela data

Pelas mesmas razões que a pesquisa anterior, podemos usar a tabela comment_video.

```cql
SELECT * FROM videos.comment_video WHERE videoId=1;
```

```
 videoid | posttime                        | author     | content      | id
---------+---------------------------------+------------+--------------+----
       1 | 2023-11-24 01:06:13.384000+0000 |   alex_123 |  Lowkey Bad. |  2
       1 | 2023-11-24 01:06:13.359000+0000 | jane_smith | Great video! |  1
```

Mais uma vez, podemos verificar que os comentários estão ordenados inversamente pela data.

#### 10. Permitir a pesquisa do rating médio de um vídeo e quantas vezes foi votado

```cql
SELECT videoId, AVG(rate) AS average_rating, COUNT(rate) AS number_of_votes FROM videos.rating where videoId = 1;
```

```
 videoid | average_rating | number_of_votes
---------+----------------+-----------------
       1 |              4 |               3
```

## Alínea d)

#### 1. Os últimos 3 comentários introduzidos para um vídeo

```cql
SELECT * FROM videos.comment_video WHERE videoId = 4 LIMIT 3;
```

```
 videoid | posttime                        | author       | content     | id
---------+---------------------------------+--------------+-------------+----
       4 | 2023-11-24 01:06:13.584000+0000 |     john_doe |         Mid | 11
       4 | 2023-11-24 01:06:13.559000+0000 | emily_wilson |      Great! | 10
       4 | 2023-11-24 01:06:13.455000+0000 |   sara_brown | Cool video! |  5
```

#### 2. Lista das tags de determinado vídeo

```cql
SELECT tags FROM videos.video WHERE id = 6;
```

```
  tags
-----------------------------
 {'programming', 'tutorial'}
```

#### 3. Todos os vídeos com a tag Aveiro

Para executar esta pesquisa, é necessário criar um índice na tabela video para a coluna tags:

```cql
CREATE INDEX IF NOT EXISTS ON videos.video (tags);
```

```cql
SELECT * FROM videos.video WHERE tags CONTAINS 'Aveiro';
```

```
 id | author     | uploadtime                      | description                     | name        | tags
----+------------+---------------------------------+---------------------------------+-------------+--------------------
 11 | sara_brown | 2023-11-24 01:06:13.020000+0000 |                Exploring Aveiro | Aveiro Vlog | {'Aveiro', 'vlog'}
 12 | sara_brown | 2023-11-24 01:06:13.046000+0000 | Tasting traditional Aveiro food |  Ovos Moles | {'Aveiro', 'food'}
```

#### 4. Os últimos 5 eventos de determinado vídeo realizados por um utilizador

```cql
SELECT * FROM videos.event WHERE videoId = 3 AND author = 'emily_wilson' LIMIT 5;
```

```
 videoid | author       | eventtime                       | eventtype | moment
---------+--------------+---------------------------------+-----------+---------------------------------
       3 | emily_wilson | 2023-11-24 01:06:13.856000+0000 |      play | 2023-11-24 01:06:13.856000+0000
       3 | emily_wilson | 2023-11-24 01:06:13.875000+0000 |     pause | 2023-11-24 01:06:13.875000+0000
       3 | emily_wilson | 2023-11-24 01:06:13.894000+0000 |      stop | 2023-11-24 01:06:13.894000+0000
       3 | emily_wilson | 2023-11-24 01:06:13.916000+0000 |      play | 2023-11-24 01:06:13.916000+0000
       3 | emily_wilson | 2023-11-24 01:06:13.945000+0000 |      stop | 2023-11-24 01:06:13.945000+0000
```

#### 5. Vídeos partilhados por determinado utilizador (maria1987, por exemplo) num determinado período de tempo (Agosto de 2017, por exemplo)

Esta pesquisa apenas é possível se utilizarmos a opção ALLOW FILTERING, uma vez que não é possível filtrar por data.

```cql
SELECT * FROM videos.video WHERE author = 'alex_123' AND uploadTime > '2023-11-01' AND uploadTime < '2023-11-30' ALLOW FILTERING;
```

```
 id | author   | uploadtime                      | description                | name                 | tags
----+----------+---------------------------------+----------------------------+----------------------+-----------------------------
  5 | alex_123 | 2023-11-24 01:06:12.811000+0000 |    Exciting gaming moments |    Gaming Highlights |    {'gaming', 'highlights'}
  6 | alex_123 | 2023-11-24 01:06:12.829000+0000 | Learn programming concepts | Programming Tutorial | {'programming', 'tutorial'}
```

#### 6. Os últimos 10 vídeos, ordenado inversamente pela data da partilhada

Para permitir esta pesquisa a tabela video tem um CLUSTERING ORDER BY (uploadTime DESC).

```cql
SELECT * FROM videos.video LIMIT 10;
```


```
 id | author        | uploadtime                      | description                             | name                 | tags
----+---------------+---------------------------------+-----------------------------------------+----------------------+-----------------------------
  1 |      john_doe | 2023-11-24 02:32:45.750000+0000 |                          My first video |  First Video by John |             {'fun', 'vlog'}
  2 |      john_doe | 2023-11-24 02:32:45.752000+0000 |      Learn how to cook a delicious meal |     Cooking Tutorial |     {'cooking', 'tutorial'}
  7 |  emily_wilson | 2023-11-24 02:32:45.765000+0000 |          Latest fashion trends and tips |         Fashion Tips |        {'fashion', 'style'}
  9 | michael_jones | 2023-11-24 02:32:45.769000+0000 | Fun and interesting science experiments |  Science Experiments |  {'experiments', 'science'}
 10 | michael_jones | 2023-11-24 02:32:45.771000+0000 |                Taking care of your pets |        Pet Care Tips |            {'care', 'pets'}
  4 |    jane_smith | 2023-11-24 02:32:45.757000+0000 |   Exploring new places around the world |          Travel Vlog |          {'travel', 'vlog'}
  6 |      alex_123 | 2023-11-24 02:32:45.762000+0000 |              Learn programming concepts | Programming Tutorial | {'programming', 'tutorial'}
  3 |    jane_smith | 2023-11-24 02:32:45.755000+0000 |         Get fit with these workout tips |         Fitness Tips |       {'fitness', 'health'}
  8 |  emily_wilson | 2023-11-24 02:32:45.767000+0000 |             Showcasing amazing artworks |         Art Showcase |         {'art', 'showcase'}
 11 |    sara_brown | 2023-11-24 02:32:45.773000+0000 |                        Exploring Aveiro |          Aveiro Vlog |          {'Aveiro', 'vlog'}
```

#### 7. Todos os seguidores (followers) de determinado vídeo

```cql
SELECT users FROM videos.follower WHERE videoId = 1;
```

```
 users
--------------------------------------------
 {'alex_123', 'emily_wilson', 'jane_smith'}
```

#### 8. Todos os comentários (dos vídeos) que determinado utilizador está a seguir (following)

Não é possível executar esta pesquisa, uma vez que Cassandra não suporta JOINs. Para poder resolver esta querie seria necessário criar mais tabelas.

#### 9. Os 5 vídeos com maior rating

Não é possível, uma vez que para ordenar o rating seria necessário que este fosse a Clustering Key, e mesmo assim, teriamos de restringir com a Partition Key. E mais uma vez, Cassandra não suporta JOINs.

#### 10. Uma query que retorne todos os vídeos e que mostre claramente a forma pela qual estão ordenados

Não é possível, mais uma vez, é necessário que a Partition Key seja restrita (por EQ ou IN)

#### 11. Lista com as Tags existentes e o número de vídeos catalogados com cada uma delas

Não é possível, uma vez que Cassandra não possui JOINs e GROUP BY apenas funciona com a Partition Key.

### As minhas 2 queries

#### 12. Rating médio de um vídeo e quantos votos teve

```cql
SELECT videoId, AVG(rate) AS average_rating, COUNT(rate) AS number_of_votes FROM videos.rating where videoid = 1;
```

```
 videoid | average_rating | number_of_votes
---------+----------------+-----------------
       1 |              4 |               3
```

#### 13. Lista de todos os eventos de um utilizador num determinado vídeo, ordenados do mais recente para o mais antigo

```cql
SELECT * FROM videos.event WHERE videoId = 3 AND author = 'emily_wilson' ORDER BY eventTime;
```

```
  videoid | author       | eventtime                       | eventtype | moment
---------+--------------+---------------------------------+-----------+---------------------------------
       3 | emily_wilson | 2023-11-24 01:06:13.856000+0000 |      play | 2023-11-24 01:06:13.856000+0000
       3 | emily_wilson | 2023-11-24 01:06:13.875000+0000 |     pause | 2023-11-24 01:06:13.875000+0000
       3 | emily_wilson | 2023-11-24 01:06:13.894000+0000 |      stop | 2023-11-24 01:06:13.894000+0000
       3 | emily_wilson | 2023-11-24 01:06:13.916000+0000 |      play | 2023-11-24 01:06:13.916000+0000
       3 | emily_wilson | 2023-11-24 01:06:13.945000+0000 |      stop | 2023-11-24 01:06:13.945000+0000
```