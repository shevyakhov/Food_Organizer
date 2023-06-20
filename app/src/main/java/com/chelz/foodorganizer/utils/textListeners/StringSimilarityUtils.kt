package com.chelz.foodorganizer.utils.textListeners

import kotlin.math.floor

internal object StringSimilarity {


	fun jaroDistance(s1: String, s2: String): Double {
		if (s1 === s2) return 1.0
		val len1 = s1.length
		val len2 = s2.length
		if (len1 == 0 || len2 == 0) return 0.0
		val maxDist = floor((len1.coerceAtLeast(len2) / 2).toDouble()).toInt() - 1

		var match = 0

		val hashS1 = IntArray(s1.length)
		val hashS2 = IntArray(s2.length)

		for (i in 0 until len1) {

			for (j in 0.coerceAtLeast(i - maxDist) until len2.coerceAtMost(i + maxDist + 1))
				if (s1[i] == s2[j] &&
					hashS2[j] == 0
				) {
					hashS1[i] = 1
					hashS2[j] = 1
					match++
					break
				}
		}

		if (match == 0) return 0.0

		var t = 0.0
		var point = 0
		for (i in 0 until len1) if (hashS1[i] == 1) {

			while (hashS2[point] == 0) point++
			if (s1[i] != s2[point++]) t++
		}
		t /= 2.0

		return ((match.toDouble() / len1.toDouble() + match.toDouble() / len2.toDouble() + (match.toDouble() - t) / match.toDouble()) / 3.0)
	}

	fun jaroWinkler(s1: String, s2: String): Double {
		var jaroDist = jaroDistance(s1, s2)

		if (jaroDist > 0.7) {

			var prefix = 0
			for (i in 0 until s1.length.coerceAtMost(s2.length)) {

				if (s1[i] == s2[i]) prefix++ else break
			}
			prefix = 4.coerceAtMost(prefix)

			jaroDist += 0.1 * prefix * (1 - jaroDist)
		}
		return jaroDist
	}
}