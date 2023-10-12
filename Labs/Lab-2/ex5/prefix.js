prefix = function() {
    return db.phones.aggregate([{$group: {_id: "$components.prefix", nPhones: {$sum: 1}}}])
}

// Result:

// [
//     { _id: 234, nPhones: 33252 },
//     { _id: 232, nPhones: 33481 },
//     { _id: 231, nPhones: 33153 },
//     { _id: 233, nPhones: 33411 },
//     { _id: 21, nPhones: 33301 },
//     { _id: 22, nPhones: 33402 }
//   ]
  