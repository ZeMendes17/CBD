// 1. Quais são os 10 atores/atrizes com mais filmes?
first = function() {
    return db.movies.aggregate([
        {$unwind: "$cast"},
        {$group: {_id: "$cast", count: {$sum: 1}}},
        {$sort: {count: -1}},
        {$limit: 10}
    ])
}
// Resultado:
// [
//     { _id: 'Gèrard Depardieu', count: 68 },
//     { _id: 'Robert De Niro', count: 60 },
//     { _id: 'Michael Caine', count: 53 },
//     { _id: 'Marcello Mastroianni', count: 50 },
//     { _id: 'Max von Sydow', count: 49 },
//     { _id: 'Bruce Willis', count: 49 },
//     { _id: 'Samuel L. Jackson', count: 48 },
//     { _id: 'Morgan Freeman', count: 48 },
//     { _id: 'Christopher Plummer', count: 47 },
//     { _id: 'Gene Hackman', count: 46 }
// ]
  
// 2. Qual a média de prêmios recebidos por cada género de filme?
second = function() {
    return db.movies.aggregate([
        {$unwind: "$genres"},
        {$group: {_id: "$genres", avgAwards: {$avg: "$awards.wins"}}}
    ])
}
// Resultado:
// [
//     { _id: 'War', avgAwards: 5.420654911838791 },
//     { _id: 'Adventure', avgAwards: 4.453789731051344 },
//     { _id: 'Short', avgAwards: 3.3849372384937237 },
//     { _id: 'Fantasy', avgAwards: 4.090199479618387 },
//     { _id: 'Thriller', avgAwards: 3.8378480060195637 },
//     { _id: 'Sci-Fi', avgAwards: 3.857833655705996 },
//     { _id: 'News', avgAwards: 2.9411764705882355 },
//     { _id: 'History', avgAwards: 6.223223223223223 },
//     { _id: 'Music', avgAwards: 4.5285714285714285 },
//     { _id: 'Drama', avgAwards: 5.111175574733483 },
//     { _id: 'Musical', avgAwards: 4.137577002053388 },
//     { _id: 'Film-Noir', avgAwards: 2.3714285714285714 },
//     { _id: 'Sport', avgAwards: 3.876923076923077 },
//     { _id: 'Talk-Show', avgAwards: 5 },
//     { _id: 'Biography', avgAwards: 6.886752136752137 },
//     { _id: 'Documentary', avgAwards: 2.754814466885862 },
//     { _id: 'Family', avgAwards: 2.7986270022883297 },
//     { _id: 'Mystery', avgAwards: 3.897537728355838 },
//     { _id: 'Comedy', avgAwards: 3.2989749430523916 },
//     { _id: 'Horror', avgAwards: 2.1256605989430417 }
// ]
  
// 3. Qual o diretor com mais filmes depois de 1990?
third = function() {
    return db.movies.aggregate([
        {$match: {year: {$gt: 1990}}},
        {$unwind: "$directors"},
        {$group: {_id: "$directors", count: {$sum: 1}}},
        {$sort: {count: -1}},
        {$limit: 1}
    ])
}
// Resultado:
// [ { _id: 'Takashi Miike', count: 34 } ]

// 4. Os 3 atores com mais runtime ao longo de todos os filmes? Mostre em quantos filmes cada um participou
fourth = function() {
    return db.movies.aggregate([
        {$unwind: "$cast"},
        {$group: {_id: "$cast", runtime: {$sum: "$runtime"}, count: {$sum: 1}}},
        {$sort: {runtime: -1}},
        {$limit: 3}
    ])
}
// Resultado:
// [
//     { _id: 'Gèrard Depardieu', runtime: 7886, count: 68 },
//     { _id: 'Robert De Niro', runtime: 7374, count: 60 },
//     { _id: 'Shah Rukh Khan', runtime: 6423, count: 37 }
// ]

// 5. Apresente por ordem decrescente os filmes com maior cast
fifth = function() {
    return db.movies.aggregate([
        {$unwind: "$cast"},
        {$group: {_id: "$title", cast: {$sum: 1}}},
        {$sort: {cast: -1}}
    ])
}

// Resultado:
// [
//     { _id: 'The Hole', cast: 24 },
//     { _id: 'The Terrorist', cast: 24 },
//     { _id: 'Jane Eyre', cast: 24 },
//     { _id: 'Hamlet', cast: 24 },
//     { _id: 'The Journey', cast: 23 },
//     { _id: 'Les Misèrables', cast: 20 },
//     { _id: 'The New Land', cast: 20 },
//     { _id: 'Alice in Wonderland', cast: 20 },
//     { _id: 'Madame Bovary', cast: 20 },
//     { _id: 'Wolf', cast: 20 },
//     { _id: 'Creature', cast: 20 },
//     { _id: 'The Stranger', cast: 20 },
//     { _id: 'The Emigrants', cast: 20 },
//     { _id: 'Love', cast: 20 },
//     { _id: 'Peter Pan', cast: 20 },
//     { _id: 'Mad As Hell', cast: 20 },
//     { _id: 'Macbeth', cast: 20 },
//     { _id: 'Inside', cast: 16 },
//     { _id: 'Beauty and the Beast', cast: 16 },
//     { _id: 'Carrie', cast: 16 },
//     (...)
// ]