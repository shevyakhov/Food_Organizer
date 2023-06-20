package com.chelz.foodorganizer.utils.flowBinders

import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun EditText.bindFlow(scope: LifecycleCoroutineScope, flow: MutableStateFlow<String?>) {
	asFlow().filter {
		it.takeIf { it != "" } != flow.value
	}.onEach {
		flow.value = it
	}.launchIn(scope)
}

private fun EditText.asFlow(): Flow<String> = callbackFlow {
	val callback = doAfterTextChanged { trySend(it?.toString() ?: "").isSuccess }
	awaitClose {
		removeTextChangedListener(callback)
	}
}