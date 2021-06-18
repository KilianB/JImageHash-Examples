# JImageHash - Examples


Examples for [JImageHash](https://github.com/KilianB/JImageHash)

 File | Content 
--- | --- 
[CompareImages.java](src/main/java/dev/brachtendorf/jimagehash_examples/CompareImages.java) | Compare the similarity of two images using a single algorithm and a custom threshold
[ChainAlgorithms.java](src/main/java/dev/brachtendorf/jimagehash_examples/ChainAlgorithms.java) | Chain multiple algorithms to achieve a better precision & recall.
[MatchMultipleImages.java](src/main/java/dev/brachtendorf/jimagehash_examples/MatchMultipleImages.java) | Precompute the hash of multiple images to retrieve all relevant images in a batch.
[DatabaseExample](src/main/java/dev/brachtendorf/jimagehash_examples/DatabaseExample.java) | Store hashes persistently in a database. Serialize and Deserialize the matcher..
[AlgorithmBenchmark.java](src/main/java/dev/brachtendorf/jimagehash_examples/AlgorithmBenchmark.java) | Test different algorithm/setting combinations against your images to see which settings give the best result.
[Clustering Example](src/main/java/dev/brachtendorf/jimagehash_examples/nineGagDuplicateDetectionAndMemeCategorize) | Extensive turotial matching 17.000 images . As described in the [blog](https://medium.com/@kilian.brachtendorf_83099/getting-tired-of-re-uploads-4a4f88908d52)