package com.chelz.foodorganizer.utils.flowBinders

import androidx.lifecycle.LifecycleCoroutineScope
import com.google.android.material.chip.Chip
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun Chip.bindFlow(scope: LifecycleCoroutineScope, flow: MutableStateFlow<String?>) {
	asFlow().filter {
		it.takeIf { it != "" } != flow.value
	}.onEach {
		flow.value = it
	}.launchIn(scope)
}

private fun Chip.asFlow(): Flow<String> = callbackFlow {
	setOnCheckedChangeListener { compoundButton, _ ->
		trySend(compoundButton.text.toString())
	}
	awaitClose { setOnCheckedChangeListener { _, _ -> } }
}