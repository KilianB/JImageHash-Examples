package dev.brachtendorf.jimagehash_examples;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.PriorityQueue;

import javax.imageio.ImageIO;

import dev.brachtendorf.jimagehash.datastructures.tree.Result;
import dev.brachtendorf.jimagehash.datastructures.tree.binaryTree.BinaryTree;
import dev.brachtendorf.jimagehash.hashAlgorithms.AverageHash;
import dev.brachtendorf.jimagehash.hashAlgorithms.HashingAlgorithm;
import dev.brachtendorf.jimagehash.hashAlgorithms.PerceptiveHash;
import dev.brachtendorf.jimagehash.matcher.cached.ConsecutiveMatcher;

/**
 * This example shows how a greater number of images can be compared to each
 * other without computing the hashes for the same image over and over. Be aware
 * that the tree only serves as an exemplary data structure (isn't necessarily
 * optimized) and can also be swapped out for a database. Be aware that the
 * hashes and buffered images are kept in memory all the time. Maybe it's a
 * valid approach to only save the file name of the image in the tree.
 * 
 * @author Kilian
 *
 */
public class MatchMultipleImages {

	private static BufferedImage ballon;
	// Similar images
	private static BufferedImage copyright;
	private static BufferedImage highQuality;
	private static BufferedImage lowQuality;
	private static BufferedImage thumbnail;

	public MatchMultipleImages() {
		loadImages();
		// Run examples
		matchMultipleImagesInMemory();

		matchMultipleImagesInMemoryManually(ballon, copyright, highQuality, lowQuality, thumbnail);
	}

	private void matchMultipleImagesInMemory() {

		System.out.println("MatchMultipleImagesInMemory():");

		ConsecutiveMatcher matcher = new ConsecutiveMatcher();
		matcher.addHashingAlgorithm(new AverageHash(64), .4);
		matcher.addHashingAlgorithm(new PerceptiveHash(32), .3);

		// Add all images of interest to the matcher and precalculate hashes
		matcher.addImages(ballon, copyright, highQuality, lowQuality, thumbnail);

		// Find all images which are similar to highQuality
		PriorityQueue<Result<BufferedImage>> similarImages = matcher.getMatchingImages(highQuality);

		System.out.println("Images Similar to high quality:");

		// Print out results
		similarImages.forEach(result -> {
			System.out.printf("Distance: %.3f Image: @%s%n", result.distance, System.identityHashCode(result.value));
		});

	}

	private void matchMultipleImagesInMemoryManually(BufferedImage... images) {

		System.out.println("\nMatchMultipleImagesInMemoryManually():");

		/*
		 * Only hashes produced by the same algorithm can be meaningfully compared. This
		 * flag ensures that all hashes added to a tree are generated by the same
		 * algorithm with the same settings or throws an error otherwise.
		 */
		boolean ensureHashConsistency = true;

		// Configure algorithms
		HashingAlgorithm aHasher = new AverageHash(32);
		HashingAlgorithm pHasher = new PerceptiveHash(32);

		// Choose a data structure/database to save the created hashes
		// This binary tree maps a hash to a value
		BinaryTree<BufferedImage> aHashTree = new BinaryTree<>(ensureHashConsistency);
		BinaryTree<BufferedImage> pHashTree = new BinaryTree<>(ensureHashConsistency);

		// Generate the hashes
		for (BufferedImage image : images) {
			// Insert -> [key:hash,value:image] into the tree
			aHashTree.addHash(aHasher.hash(image), image);
			pHashTree.addHash(pHasher.hash(image), image);
		}

		// Find all images which are similar to an image
		int rIndex = (int) (Math.random() * images.length);
		BufferedImage needle = images[rIndex];

		// Similarity score threshold
		int aThreshold = 15;
		int pThreshold = 10;

		// Retrieve all hashes which are within the threshold
		PriorityQueue<Result<BufferedImage>> aCandidates = aHashTree
				.getElementsWithinHammingDistance(aHasher.hash(needle), aThreshold);

		PriorityQueue<Result<BufferedImage>> pCandidates = pHashTree
				.getElementsWithinHammingDistance(pHasher.hash(needle), pThreshold);

		if (rIndex == 0) {
			System.out.println("We are matching against ballon: Only expecting a single image being returned. Itself");
		} else {
			System.out.println("We are matching one of the duplicate images. We are expecting multiple return values");
		}

		// Display some images ordered by similarity
		System.out.println("Matched the first algorithm:");
		aCandidates.forEach(result -> System.out.println(result));

		// Hashcode and equals are overwritten therefore we can check for equality
		pCandidates.retainAll(aCandidates);

		// All images which comply to both algorithms
		System.out.println("Matched both algorithms:");
		pCandidates.forEach(result -> System.out.println(result));
	}

	private void loadImages() {
		try {
			ballon = ImageIO.read(new File("src/main/resources/images/ballon.jpg"));
			copyright = ImageIO.read(new File("src/main/resources/images/copyright.jpg"));
			highQuality = ImageIO.read(new File("src/main/resources/images/highQuality.jpg"));
			lowQuality = ImageIO.read(new File("src/main/resources/images/lowQuality.jpg"));
			thumbnail = ImageIO.read(new File("src/main/resources/images/thumbnail.jpg"));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new MatchMultipleImages();
	}

}
