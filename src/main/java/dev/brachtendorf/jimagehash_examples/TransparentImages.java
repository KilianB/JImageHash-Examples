package dev.brachtendorf.jimagehash_examples;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import com.github.kilianB.hashAlgorithms.HashingAlgorithm;
import com.github.kilianB.hashAlgorithms.PerceptiveHash;

public class TransparentImages {
    public static void main(String[] args) throws IOException {

        File[] transparentFiles = { new File("src/main/resources/images/transparent0.png"),
                new File("src/main/resources/images/transparent1.png"),
                new File("src/main/resources/images/ballon.jpg"),
                new File("src/main/resources/images/highQuality.jpg") };

        HashingAlgorithm pHash = new PerceptiveHash(32);
        var hashes = pHash.hash(transparentFiles);

        // Distance of 0 because background is by default transparent pixels are assumed
        // to be black
        System.out.println("Distance between transparent images" + hashes[0].normalizedHammingDistance(hashes[1]));

        System.out.println("Distance between non transparent images hq and ballon: "
                + hashes[2].normalizedHammingDistance(hashes[3]));
        System.out.println("Distance between transparent logo and non transparent hq: "
                + hashes[0].normalizedHammingDistance(hashes[2]));

        // Correctly handle transparent images
        HashingAlgorithm pHashWithTransparencySupport = new PerceptiveHash(32);

        // Replace each pixel < 200 alpha value as orange
        pHashWithTransparencySupport.setOpaqueHandling(200);
        // pHashWithTransparencySupport.setOpaqueHandling(Color.green, 200);
        var transparentHashes = pHashWithTransparencySupport.hash(transparentFiles);

        // Distance of 0.5 because background
        System.out.println("Distance between transparent images"
                + transparentHashes[0].normalizedHammingDistance(transparentHashes[1]));

        System.out.println("Distance between non transparent images hq and ballon: "
                + transparentHashes[2].normalizedHammingDistance(transparentHashes[3]));
        System.out.println("Distance between transparent logo and non transparent hq: "
                + transparentHashes[0].normalizedHammingDistance(transparentHashes[2]));

        // The distance between the non transparent image is the same for both
        // algorithms as only transparent images are replaced.
        // The second hashing algorithm correctly computes the values for the transparent images.

        try{
            System.out.println(hashes[0].hammingDistance(transparentHashes[0]));
        }catch(IllegalArgumentException e){
           System.out.println("The hashes of both algorithms are not compatible as they might produce different hashes for the same input.")
        }


    }
}
