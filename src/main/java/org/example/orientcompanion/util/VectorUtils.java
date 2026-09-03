package org.example.orientcompanion.util;

public final class VectorUtils {

    private VectorUtils() {
    }

    /**
     * Calcule la similarité cosinus entre deux vecteurs.
     * Retourne une valeur entre -1 et 1 (généralement entre 0 et 1
     * pour des embeddings de texte), où 1 signifie une similarité parfaite.
     */
    public static double cosineSimilarity(float[] a, float[] b) {
        if (a == null || b == null || a.length != b.length || a.length == 0) {
            throw new IllegalArgumentException("Les vecteurs doivent être non nuls et de même dimension");
        }

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < a.length; i++) {
            dotProduct += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }

        if (normA == 0.0 || normB == 0.0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}