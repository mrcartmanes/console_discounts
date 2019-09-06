package com.consolediscounts.domain

import kotlin.coroutines.CoroutineContext

expect val threadContext: CoroutineContext
expect val uiContext: CoroutineContext