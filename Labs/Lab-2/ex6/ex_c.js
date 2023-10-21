// 1. Liste os géneros, o elenco, o título e o ano, sem listar o _id, de todos os filmes
// de comédia que possuam o ator "Charles Chaplin" no elenco e que tenha sido lançado
// depois de 1935
first = function() {
    return db.movies.find(
        {genres: "Comedy",
        cast: "Charles Chaplin",
        year: {$gt: 1935}},
        
        {_id:0, genres:1, title:1, year:1, cast:1}
        )
}
// Resultado:
// [
//     {
//       year: 1936,
//       genres: [ 'Comedy', 'Drama' ],
//       title: 'Modern Times',
//       cast: [
//         'Charles Chaplin',
//         'Paulette Goddard',
//         'Henry Bergman',
//         'Tiny Sandford'
//       ]
//     },
//     {
//       genres: [ 'Comedy', 'Drama', 'War' ],
//       cast: [
//         'Charles Chaplin',
//         'Jack Oakie',
//         'Reginald Gardiner',
//         'Henry Daniell'
//       ],
//       title: 'The Great Dictator',
//       year: 1940
//     },
//     {
//       genres: [ 'Comedy', 'Crime', 'Drama' ],
//       cast: [
//         'Charles Chaplin',
//         'Mady Correll',
//         'Allison Roddan',
//         'Robert Lewis'
//       ],
//       title: 'Monsieur Verdoux',
//       year: 1947
//     },
//     {
//       genres: [ 'Comedy', 'Drama' ],
//       cast: [
//         'Charles Chaplin',
//         'Maxine Audley',
//         'Jerry Desmonde',
//         'Oliver Johnston'
//       ],
//       title: 'A King in New York',
//       year: 1957
//     }
// ]  

// 2. Liste apenas os títulos dos filmes que têm o diretor "James Cameron", tenha sido
// nomeado a mais de 20 prêmios, não possua os gêneros "Horror" e "Drama" ou tenha um
// rating do imdb superior a 8.0
second = function() {
    return db.movies.find(
        {directors: "James Cameron",
        "awards.nominations": {$gt: 20},
        $or: [
            {genres: {$not: {$in: ["Horror", "Drama"]}}},
            {"imdb.rating": {$gt: 8.0}}
        ]},
        
        {_id:0, title:1}
        )
}
// Resultado:
// [
//     { title: 'Aliens' },
//     { title: 'Terminator 2: Judgment Day' },
//     { title: 'Avatar' }
// ]

// 3. Liste o título, os géneros e o ano do filme mais antigo da categoria "Sci-Fi" e com o diretor "Steven Spielberg"
third = function() {
    return db.movies.find(
        {genres: "Sci-Fi",
        directors: "Steven Spielberg"},
        
        {_id:0, title:1, year:1, genres:1}
        ).sort({year:1}).limit(1)
}
// Resultado:
// [
//     {
//       year: 1977,
//       genres: [ 'Drama', 'Sci-Fi' ],
//       title: 'Close Encounters of the Third Kind'
//     }
// ]

// 4. Liste o título, o elenco e o rating imdb dos 5 primeiros filmes com o menor rating imdb
// em que o elenco possui 3 elementos, e o título não comece com a letra "A" ou "B"
fourth = function() {
    return db.movies.find(
        {cast: {$size: 3},
        title: {$not: {$regex: /^[AB]/}}},
        
        {_id:0, title:1, cast:1, "imdb.rating":1}
        ).sort({"imdb.rating":1}).limit(5)
}
// Resultado:
// [
//     {
//       cast: [ 'Tao Guo', 'Barbie Hsu', 'Suet Lam' ],
//       title: 'Croczilla',
//       imdb: { rating: 4.1 }
//     },
//     {
//       cast: [ 'Farrah Fawcett', 'Kirk Douglas', 'Harvey Keitel' ],
//       title: 'Saturn 3',
//       imdb: { rating: 5 }
//     },
//     {
//       cast: [ 'Louis Koo', 'Chapman To', 'Sandra Kwan Yue Ng' ],
//       title: 'Mr. and Mrs. Incredible',
//       imdb: { rating: 5.4 }
//     },
//     {
//       imdb: { rating: 5.4 },
//       title: 'The Last Man',
//       cast: [ 'David Arnott', 'Jeri Ryan', 'Dan Montgomery Jr.' ]
//     },
//     {
//       cast: [ 'Anta Aizupe', 'Maxim Lazarev', 'Aris Rozentals' ],
//       title: 'The Man in the Orange Jacket',
//       imdb: { rating: 5.5 }
//     }
// ]
  

// 5. Liste os filmes que sejam uma curtas-metragens (runtime menor que 42 minutos), com
// classificação "PG-13" e que não pertencem ao gênero "Crime" ou "Horror"
fifth = function() {
    return db.movies.find(
        {runtime: {$lt: 42},
        rated: "PG-13",
        genres: {$not: {$in: ["Crime", "Horror"]}}},
        
        {_id:0, title:1, genres:1, rated:1, runtime:1}
        )
}
// Resultado:
// [
//     {
//       genres: [ 'Action', 'Short', 'Sci-Fi' ],
//       runtime: 12,
//       rated: 'PG-13',
//       title: 'T2 3-D: Battle Across Time'
//     },
//     {
//       genres: [ 'Animation', 'Short', 'Western' ],
//       runtime: 13,
//       rated: 'PG-13',
//       title: 'DC Showcase: Jonah Hex'
//     }
// ]
  

// 6. Liste os filmes produzidos entre 1980 e 2000, que não sejam na linguagem "English",
// que no seu plot possua as palavras "Wild" ou "Jack" e seja um filme "Adventure" e
// "Action"
sixth = function() {
    return db.movies.find(
        {year: {$gte: 1980, $lte: 2000},
        language: {$ne: "English"},
        $or: [
            {plot: {$regex: /Wild/}},
            {plot: {$regex: /Jack/}}
        ],
        $and: [
            {genres: "Adventure"},
            {genres: "Action"}
        ]},
        
        {_id:0, title:1, genres:1, year:1, plot:1, language:1}
        )
}
// Resultado:
// [
//     {
//       plot: 'An idealistic, modern-day cowboy struggles to keep his Wild West show afloat in the face of hard luck and waning interest.',
//       genres: [ 'Action', 'Adventure', 'Comedy' ],
//       title: 'Bronco Billy',
//       year: 1980
//     },
//     {
//       plot: 'Jackie Chan and his bumbling sidekick are sent on a quest through Europe to find a mysterious treasure held by a shadowy organization of monks.',
//       genres: [ 'Action', 'Adventure', 'Comedy' ],
//       title: 'Armour of God',
//       year: 1986
//     },
//     {
//       plot: 'Agent Jackie is hired to find WWII Nazi gold hidden in the Sahara desert. He teams up with three bundling women (the 3 stooges?) who are all connected in some way. However a team of ...',
//       genres: [ 'Action', 'Adventure', 'Comedy' ],
//       title: 'Armour of God 2: Operation Condor',
//       year: 1991
//     },
//     {
//       plot: "Ballu (Sanjay Dutt), a notorious, cunning, and unscrupulous criminal who's wanted by the police for a plethora of murders and thefts. Bright and ambitious Inspector Ram (Jackie Shroff) ...",
//       genres: [ 'Action', 'Adventure', 'Crime' ],
//       title: 'Khal Nayak',
//       year: 1993
//     },
//     {
//       plot: 'Doc McCoy is put in prison because his partners chickened out and flew off without him after exchanging a prisoner with a lot of money. Doc knows Jack Benyon, a rich "business"-man, is up ...',
//       genres: [ 'Action', 'Adventure', 'Crime' ],
//       title: 'The Getaway',
//       year: 1994
//     },
//     {
//       plot: 'A Chinese man who travels to the Wild West to rescue a kidnapped princess. After teaming up with a train robber, the unlikely duo takes on a Chinese traitor and his corrupt boss.',
//       genres: [ 'Action', 'Adventure', 'Comedy' ],
//       title: 'Shanghai Noon',
//       year: 2000
//     }
// ]